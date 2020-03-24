package info.bitrich.xchangestream.dsx;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.core.StreamingMarketDataService;
import info.bitrich.xchangestream.dsx.dto.*;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import io.reactivex.Observable;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.dsx.v2.DsxAdapters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Pavel Chertalev on 15.03.2018.
 */
public class DsxStreamingMarketDataService implements StreamingMarketDataService {

    private final DsxStreamingService service;
    private Map<CurrencyPair, DsxWebSocketOrderBook> orderbooks = new HashMap<>();

    public DsxStreamingMarketDataService(DsxStreamingService service) {
        this.service = service;
    }

    @Override
    public Observable<OrderBook> getOrderBook(CurrencyPair currencyPair, Object... args) {
        String pair = currencyPair.base.toString() + currencyPair.counter.toString();
        String channelName = getChannelName("orderbook", pair);
        final ObjectMapper mapper = StreamingObjectMapperHelper.getObjectMapper();

        Observable<JsonNode> jsonNodeObservable = service.subscribeChannel(channelName);
        return jsonNodeObservable
                .map(s -> mapper.readValue(s.toString(), DsxWebSocketOrderBookTransaction.class))
                .map(s -> {
                    DsxWebSocketOrderBook dsxOrderBook = s.toDsxOrderBook(orderbooks.getOrDefault(currencyPair, null));
                    orderbooks.put(currencyPair, dsxOrderBook);
                    return DsxAdapters.adaptOrderBook(dsxOrderBook.toDsxOrderBook(), currencyPair);
                });
    }

    @Override
    public Observable<Trade> getTrades(CurrencyPair currencyPair, Object... args) {
        String pair = currencyPair.base.toString() + currencyPair.counter.toString();
        String channelName = getChannelName("trades", pair);
        final ObjectMapper mapper = StreamingObjectMapperHelper.getObjectMapper();

        return service.subscribeChannel(channelName)
                .map(s -> mapper.readValue(s.toString(), DsxWebSocketTradesTransaction.class))
                .map(DsxWebSocketTradesTransaction::getParams)
                .filter(Objects::nonNull)
                .map(DsxWebSocketTradeParams::getData)
                .filter(Objects::nonNull)
                .map(Arrays::asList)
                .flatMapIterable(s -> {
                    Trades adaptedTrades = DsxAdapters.adaptTrades(s, currencyPair);
                    return adaptedTrades.getTrades();
                });
    }

    @Override
    public Observable<Ticker> getTicker(CurrencyPair currencyPair, Object... args) {
        String pair = currencyPair.base.toString() + currencyPair.counter.toString();
        String channelName = getChannelName("ticker", pair);
        final ObjectMapper mapper = StreamingObjectMapperHelper.getObjectMapper();

        return service.subscribeChannel(channelName)
                .map(s -> mapper.readValue(s.toString(), DsxWebSocketTickerTransaction.class))
                .map(s -> DsxAdapters.adaptTicker(s.getParams(), currencyPair));
    }

    private String getChannelName(String entityName, String pair) {
        return entityName + "-" + pair;
    }

}
