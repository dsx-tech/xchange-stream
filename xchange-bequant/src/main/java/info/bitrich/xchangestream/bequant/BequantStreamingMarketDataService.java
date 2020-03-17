package info.bitrich.xchangestream.bequant;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.bequant.dto.BequantWebSocketOrderBook;
import info.bitrich.xchangestream.bequant.dto.BequantWebSocketOrderBookTransaction;
import info.bitrich.xchangestream.bequant.dto.BequantWebSocketTickerTransaction;
import info.bitrich.xchangestream.bequant.dto.BequantWebSocketTradeParams;
import info.bitrich.xchangestream.bequant.dto.BequantWebSocketTradesTransaction;
import info.bitrich.xchangestream.core.StreamingMarketDataService;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import io.reactivex.Observable;
import org.knowm.xchange.bequant.v2.BequantAdapters;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.dto.marketdata.Trades;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Pavel Chertalev on 15.03.2018.
 */
public class BequantStreamingMarketDataService implements StreamingMarketDataService {

    private final BequantStreamingService service;
    private Map<CurrencyPair, BequantWebSocketOrderBook> orderbooks = new HashMap<>();

    public BequantStreamingMarketDataService(BequantStreamingService service) {
        this.service = service;
    }

    @Override
    public Observable<OrderBook> getOrderBook(CurrencyPair currencyPair, Object... args) {
        String pair = currencyPair.base.toString() + currencyPair.counter.toString();
        String channelName = getChannelName("orderbook", pair);
        final ObjectMapper mapper = StreamingObjectMapperHelper.getObjectMapper();

        Observable<JsonNode> jsonNodeObservable = service.subscribeChannel(channelName);
        return jsonNodeObservable
                .map(s -> mapper.readValue(s.toString(), BequantWebSocketOrderBookTransaction.class))
                .map(s -> {
                    BequantWebSocketOrderBook bequantOrderBook = s.toBequantOrderBook(orderbooks.getOrDefault(currencyPair, null));
                    orderbooks.put(currencyPair, bequantOrderBook);
                    return BequantAdapters.adaptOrderBook(bequantOrderBook.toBequantOrderBook(), currencyPair);
                });
    }

    @Override
    public Observable<Trade> getTrades(CurrencyPair currencyPair, Object... args) {
        String pair = currencyPair.base.toString() + currencyPair.counter.toString();
        String channelName = getChannelName("trades", pair);
        final ObjectMapper mapper = StreamingObjectMapperHelper.getObjectMapper();

        return service.subscribeChannel(channelName)
                .map(s -> mapper.readValue(s.toString(), BequantWebSocketTradesTransaction.class))
                .map(BequantWebSocketTradesTransaction::getParams)
                .filter(Objects::nonNull)
                .map(BequantWebSocketTradeParams::getData)
                .filter(Objects::nonNull)
                .map(Arrays::asList)
                .flatMapIterable(s -> {
                    Trades adaptedTrades = BequantAdapters.adaptTrades(s, currencyPair);
                    return adaptedTrades.getTrades();
                });
    }

    @Override
    public Observable<Ticker> getTicker(CurrencyPair currencyPair, Object... args) {
        String pair = currencyPair.base.toString() + currencyPair.counter.toString();
        String channelName = getChannelName("ticker", pair);
        final ObjectMapper mapper = StreamingObjectMapperHelper.getObjectMapper();

        return service.subscribeChannel(channelName)
                .map(s -> mapper.readValue(s.toString(), BequantWebSocketTickerTransaction.class))
                .map(s -> BequantAdapters.adaptTicker(s.getParams(), currencyPair));
    }

    private String getChannelName(String entityName, String pair) {
        return entityName + "-" + pair;
    }

}
