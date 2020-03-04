package info.bitrich.xchangestream.krakenFutures;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.core.StreamingMarketDataService;
import info.bitrich.xchangestream.kraken.KrakenOrderBookStorage;
import info.bitrich.xchangestream.kraken.dto.KrakenOrderBook;
import info.bitrich.xchangestream.krakenFutures.dto.marketdata.KrakenFutureOrderBook;
import info.bitrich.xchangestream.krakenFutures.dto.marketdata.KrakenFutureOrderBookUpdate;
import info.bitrich.xchangestream.krakenFutures.dto.marketdata.KrakenFutureTicker;
import info.bitrich.xchangestream.krakenFutures.dto.marketdata.KrakenFutureTrade;
import info.bitrich.xchangestream.krakenFutures.dto.marketdata.KrakenFutureTradeSnapshot;
import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesFeed;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import io.reactivex.Observable;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.kraken.KrakenAdapters;
import org.knowm.xchange.krakenFutures.dto.enums.KrakenFuturesProduct;
import org.knowm.xchange.krakenFutures.dto.enums.KrakenFuturesSide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static info.bitrich.xchangestream.krakenFutures.KrakenFuturesUtils.getChannelName;
import static info.bitrich.xchangestream.krakenFutures.KrakenFuturesUtils.validateAndPrepareSubscriptionParams;

/**
 * @author pchertalev
 */
public class KrakenFuturesStreamingMarketDataService implements StreamingMarketDataService {

    private static final Logger LOG = LoggerFactory.getLogger(KrakenFuturesStreamingMarketDataService.class);

    public static final ObjectMapper MAPPER = StreamingObjectMapperHelper.getObjectMapper();

    private final KrakenFuturesStreamingService service;
    private final Map<String, KrakenOrderBookStorage> orderBooks = new ConcurrentHashMap<>();

    public KrakenFuturesStreamingMarketDataService(KrakenFuturesStreamingService service) {
        this.service = service;
    }

    @Override
    public Observable<OrderBook> getOrderBook(CurrencyPair currencyPair, Object... args) {
        ImmutablePair<KrakenFuturesProduct, LocalDate> params = validateAndPrepareSubscriptionParams(currencyPair, args);
        String channelName = getChannelName(KrakenFuturesFeed.book, currencyPair, params.left, params.right);
        return service.subscribeChannel(channelName, args)
                .filter(jsonMessage -> KrakenFuturesFeed.book.equalsJsonNode(jsonMessage) || KrakenFuturesFeed.book_snapshot.equalsJsonNode(jsonMessage))
                .map(jsonMessage -> {
                    if (KrakenFuturesFeed.book.equalsJsonNode(jsonMessage)) {
                        KrakenFutureOrderBookUpdate futureOrderBookUpdate = MAPPER.treeToValue(jsonMessage, KrakenFutureOrderBookUpdate.class);
                        KrakenOrderBook krakenOrderBook = KrakenFuturesUtils.convertFrom(futureOrderBookUpdate);
                        return ImmutablePair.of(futureOrderBookUpdate.getSeq(), krakenOrderBook);
                    }
                    KrakenFutureOrderBook futureOrderBook = MAPPER.treeToValue(jsonMessage, KrakenFutureOrderBook.class);
                    return ImmutablePair.of(futureOrderBook.getSeq(), KrakenFuturesUtils.convertFrom(futureOrderBook));
                })
                .map(ob -> ob.right)
                .map(ob -> {
                    KrakenOrderBookStorage orderBook = ob.toKrakenOrderBook(orderBooks.get(channelName), 1000);
                    orderBooks.put(channelName, orderBook);
                    return KrakenAdapters.adaptOrderBook(orderBook.toKrakenDepth(), currencyPair);
                });
    }

    @Override
    public Observable<Ticker> getTicker(CurrencyPair currencyPair, Object... args) {
        ImmutablePair<KrakenFuturesProduct, LocalDate> params = validateAndPrepareSubscriptionParams(currencyPair, args);
        String channelName = getChannelName(KrakenFuturesFeed.ticker, currencyPair, params.left, params.right);
        return service.subscribeChannel(channelName, args)
                .map(jsonMessage -> MAPPER.treeToValue(jsonMessage, KrakenFutureTicker.class))
                .map(ob -> {
                    Ticker.Builder builder = new Ticker.Builder();
                    builder.ask(ob.getAsk());
                    builder.bid(ob.getBid());
                    builder.askSize(ob.getAskSize());
                    builder.bidSize(ob.getBidSize());
                    builder.volume(ob.getVolume());
                    builder.last(ob.getLast());
                    builder.timestamp(new Date(ob.getTime()));
                    builder.currencyPair(currencyPair);
                    return builder.build();
                });
    }

    @Override
    public Observable<Trade> getTrades(CurrencyPair currencyPair, Object... args) {
        ImmutablePair<KrakenFuturesProduct, LocalDate> params = validateAndPrepareSubscriptionParams(currencyPair, args);
        String channelName = getChannelName(KrakenFuturesFeed.trade, currencyPair, params.left, params.right);
        return service.subscribeChannel(channelName, args)
                .filter(jsonMessage -> KrakenFuturesFeed.trade.equalsJsonNode(jsonMessage) || KrakenFuturesFeed.trade_snapshot.equalsJsonNode(jsonMessage))
                .flatMap(jsonMessage -> {
                    if (KrakenFuturesFeed.trade_snapshot.equalsJsonNode(jsonMessage)) {
                        return Observable.fromIterable(MAPPER.treeToValue(jsonMessage, KrakenFutureTradeSnapshot.class)
                                .getTrades()).sorted(Comparator.comparingLong(KrakenFutureTrade::getSeq));
                    }
                    return Observable.fromArray(MAPPER.treeToValue(jsonMessage, KrakenFutureTrade.class));
                })
                .map(ob -> {
                    Trade.Builder builder = new Trade.Builder();
                    builder.currencyPair(currencyPair);
                    builder.price(ob.getPrice());
                    builder.type(ob.getSide() == KrakenFuturesSide.sell ? Order.OrderType.ASK : Order.OrderType.BID);
                    builder.originalAmount(ob.getQty());
                    builder.timestamp(new Date(ob.getTime()));
                    return builder.build();
                });
    }

}
