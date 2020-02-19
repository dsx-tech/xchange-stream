package info.bitrich.xchangestream.krakenFutures;

import info.bitrich.xchangestream.core.StreamingMarketDataService;
import info.bitrich.xchangestream.kraken.KrakenException;
import info.bitrich.xchangestream.kraken.KrakenOrderBookStorage;
import info.bitrich.xchangestream.kraken.dto.KrakenOrderBook;
import info.bitrich.xchangestream.krakenFutures.dto.KrakenFutureOrderBook;
import info.bitrich.xchangestream.krakenFutures.dto.KrakenFutureOrderBookUpdate;
import info.bitrich.xchangestream.krakenFutures.dto.KrakenFutureTicker;
import info.bitrich.xchangestream.krakenFutures.dto.KrakenFutureTrade;
import info.bitrich.xchangestream.krakenFutures.dto.KrakenFutureTradeSnapshot;
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

import static info.bitrich.xchangestream.kraken.KrakenConstants.KRAKEN_CHANNEL_DELIMITER;

/**
 * @author pchertalev
 */
public class KrakenFuturesStreamingMarketDataService implements StreamingMarketDataService {

    private static final Logger LOG = LoggerFactory.getLogger(KrakenFuturesStreamingMarketDataService.class);

    private final KrakenFuturesStreamingService service;
    private final Map<String, KrakenOrderBookStorage> orderBooks = new ConcurrentHashMap<>();
    private volatile long lastTradeSeq = 0;
    private volatile long lastOrderBookSeq = 0;

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
                        KrakenFutureOrderBookUpdate futureOrderBookUpdate = StreamingObjectMapperHelper.getObjectMapper().treeToValue(jsonMessage, KrakenFutureOrderBookUpdate.class);
                        KrakenOrderBook krakenOrderBook = KrakenFuturesUtils.convertFrom(futureOrderBookUpdate);
                        return ImmutablePair.of(futureOrderBookUpdate.getSeq(), krakenOrderBook);
                    }
                    KrakenFutureOrderBook futureOrderBook = StreamingObjectMapperHelper.getObjectMapper().treeToValue(jsonMessage, KrakenFutureOrderBook.class);
                    return ImmutablePair.of(futureOrderBook.getSeq(), KrakenFuturesUtils.convertFrom(futureOrderBook));
                })
                .filter(ob -> ob.getLeft() > lastOrderBookSeq)
                .doOnNext(ob -> lastOrderBookSeq = ob.left)
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
                .map(jsonMessage -> StreamingObjectMapperHelper.getObjectMapper().treeToValue(jsonMessage, KrakenFutureTicker.class))
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
                        return Observable.fromIterable(StreamingObjectMapperHelper.getObjectMapper().treeToValue(jsonMessage, KrakenFutureTradeSnapshot.class)
                                .getTrades()).sorted(Comparator.comparingLong(KrakenFutureTrade::getSeq));
                    }
                    return Observable.fromArray(StreamingObjectMapperHelper.getObjectMapper().treeToValue(jsonMessage, KrakenFutureTrade.class));
                })
                .filter(ob -> ob.getSeq() > lastTradeSeq)
                .doOnNext(ob -> lastTradeSeq = ob.getSeq())
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

    private ImmutablePair<KrakenFuturesProduct, LocalDate> validateAndPrepareSubscriptionParams(CurrencyPair currencyPair, Object [] args) {
        validateCurrencyPair(currencyPair);
        KrakenFuturesProduct product = getIndexedValue("product", 0, KrakenFuturesProduct.class, null, true, args);

        LocalDate maturityDate = null;
        if (product.mustHaveMaturityDate) {
            maturityDate = getIndexedValue("maturityDate", 1, LocalDate.class, null, true, args);
        }
        return ImmutablePair.of(product, maturityDate);
    }

    private void validateCurrencyPair(CurrencyPair currencyPair) {
        if (currencyPair == null) {
            throw new KrakenException("Currency pair is mandatory");
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T getIndexedValue(String name, int index, Class<T> expectedClass, T defaultValue, boolean failIfMissing, Object... args) throws KrakenException {
        if (args.length >= index + 1 && expectedClass != null) {
            if (expectedClass.isAssignableFrom(args[index].getClass())) {
                return (T) args[index];
            } else {
                throw new KrakenException(
                        String.format("Invalid type of parameter #%d (%s): expected %s but actual %s",
                                index, name, expectedClass.getName(), args[index].getClass()));
            }
        }
        if (defaultValue != null) {
            LOG.warn("Parameter '{}' was not correctly specified, so the default value {} is used", name, defaultValue);
            return defaultValue;
        }
        if (failIfMissing) {
            throw new KrakenException(String.format("Parameter #%d (%s) is not specified", index, name));
        }
        return null;
    }

    private String getChannelName(KrakenFuturesFeed feed, CurrencyPair currencyPair, KrakenFuturesProduct product, LocalDate maturityDate) {
        return feed + KRAKEN_CHANNEL_DELIMITER + product.formatProductId(currencyPair, maturityDate);
    }

}
