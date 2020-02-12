package info.bitrich.xchangestream.kraken.futures;

import info.bitrich.xchangestream.core.StreamingMarketDataService;
import info.bitrich.xchangestream.kraken.KrakenException;
import info.bitrich.xchangestream.kraken.KrakenOrderBookStorage;
import info.bitrich.xchangestream.kraken.futures.dto.KrakenFutureOrderBook;
import info.bitrich.xchangestream.kraken.futures.dto.KrakenFutureOrderBookUpdate;
import info.bitrich.xchangestream.kraken.futures.enums.KrakenFuturesFeed;
import info.bitrich.xchangestream.kraken.futures.enums.KrakenFuturesProduct;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import io.reactivex.Observable;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.kraken.KrakenAdapters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author makarid, pchertalev
 */
public class KrakenFuturesStreamingMarketDataService implements StreamingMarketDataService {

    private static final Logger LOG = LoggerFactory.getLogger(KrakenFuturesStreamingMarketDataService.class);

    private static final int ORDER_BOOK_SIZE_DEFAULT = 25;
    private static final int[] KRAKEN_VALID_ORDER_BOOK_SIZES = {10, 25, 100, 500, 1000};
    private static final int MIN_DATA_ARRAY_SIZE = 4;

    public static final String KRAKEN_CHANNEL_DELIMITER = "-";

    private final KrakenFuturesStreamingService service;
    private final Map<String, KrakenOrderBookStorage> orderBooks = new ConcurrentHashMap<>();

    public KrakenFuturesStreamingMarketDataService(KrakenFuturesStreamingService service) {
        this.service = service;
    }

    @Override
    public Observable<OrderBook> getOrderBook(CurrencyPair currencyPair, Object... args) {
        validateCurrencyPair(currencyPair);
        KrakenFuturesProduct product = getIndexedValue("product", 0, KrakenFuturesProduct.class, null, true, args);

        LocalDate maturityDate = null;
        if (product.mustHaveMaturityDate) {
            maturityDate = getIndexedValue("maturityDate", 1, LocalDate.class, null, true, args);
        }

        String channelName = getChannelName(KrakenFuturesFeed.book, currencyPair, product, maturityDate);
        return service.subscribeChannel(channelName, args)
                .filter(jsonMessage -> KrakenFuturesFeed.book.equalsJsonNode(jsonMessage) || KrakenFuturesFeed.book_snapshot.equalsJsonNode(jsonMessage))
                .map(jsonMessage -> {
                    if (KrakenFuturesFeed.book.equalsJsonNode(jsonMessage)) {
                        return KrakenFuturesUtils.convertFrom(StreamingObjectMapperHelper.getObjectMapper().treeToValue(jsonMessage, KrakenFutureOrderBookUpdate.class));
                    }
                    return KrakenFuturesUtils.convertFrom(StreamingObjectMapperHelper.getObjectMapper().treeToValue(jsonMessage, KrakenFutureOrderBook.class));
                })
                .map(ob -> {
                    KrakenOrderBookStorage orderBook = ob.toKrakenOrderBook(orderBooks.get(channelName), 1000);
                    orderBooks.put(channelName, orderBook);
                    return KrakenAdapters.adaptOrderBook(orderBook.toKrakenDepth(), currencyPair);
                });
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

    @Override
    @SuppressWarnings("unchecked")
    public Observable<Ticker> getTicker(CurrencyPair currencyPair, Object... args) {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Observable<Trade> getTrades(CurrencyPair currencyPair, Object... args) {
        return null;
    }

    private String getChannelName(KrakenFuturesFeed feed, CurrencyPair currencyPair, KrakenFuturesProduct product, LocalDate maturityDate) {
        return feed + KRAKEN_CHANNEL_DELIMITER + product.formatProductId(currencyPair, maturityDate);
    }

}
