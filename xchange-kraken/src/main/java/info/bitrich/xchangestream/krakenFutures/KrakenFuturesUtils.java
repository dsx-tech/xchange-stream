package info.bitrich.xchangestream.krakenFutures;

import info.bitrich.xchangestream.kraken.KrakenException;
import info.bitrich.xchangestream.kraken.dto.KrakenOrderBook;
import info.bitrich.xchangestream.kraken.dto.enums.KrakenOrderBookMessageType;
import info.bitrich.xchangestream.krakenFutures.dto.marketdata.KrakenFutureOrderBook;
import info.bitrich.xchangestream.krakenFutures.dto.marketdata.KrakenFutureOrderBookUpdate;
import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesFeed;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.kraken.dto.marketdata.KrakenPublicOrder;
import org.knowm.xchange.krakenFutures.dto.enums.KrakenFuturesProduct;
import org.knowm.xchange.krakenFutures.dto.enums.KrakenFuturesSide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Arrays;

import static info.bitrich.xchangestream.kraken.KrakenConstants.KRAKEN_CHANNEL_DELIMITER;

/**
 * @author pchertalev
 */
public class KrakenFuturesUtils {

    private static final Logger LOG = LoggerFactory.getLogger(KrakenFuturesUtils.class);

    public static KrakenOrderBook convertFrom(KrakenFutureOrderBook futureOrderBook) {
        KrakenPublicOrder[] asks = Arrays.stream(futureOrderBook.getAsks())
                .map(item -> new KrakenPublicOrder(item.getPrice(), item.getQty(), futureOrderBook.getTimestamp()))
                .toArray(KrakenPublicOrder[]::new);

        KrakenPublicOrder[] bids = Arrays.stream(futureOrderBook.getBids())
                .map(item -> new KrakenPublicOrder(item.getPrice(), item.getQty(), futureOrderBook.getTimestamp()))
                .toArray(KrakenPublicOrder[]::new);

        return new KrakenOrderBook(KrakenOrderBookMessageType.SNAPSHOT, asks, bids);
    }

    public static KrakenOrderBook convertFrom(KrakenFutureOrderBookUpdate futureOrderBookUpdate) {
        KrakenPublicOrder[] asks;
        KrakenPublicOrder[] bids;
        if (futureOrderBookUpdate.getSide() == KrakenFuturesSide.sell) {
            asks = new KrakenPublicOrder[]{new KrakenPublicOrder(futureOrderBookUpdate.getPrice(), futureOrderBookUpdate.getQty(), futureOrderBookUpdate.getTimestamp())};
            bids = new KrakenPublicOrder[0];
        } else {
            asks = new KrakenPublicOrder[0];
            bids = new KrakenPublicOrder[]{new KrakenPublicOrder(futureOrderBookUpdate.getPrice(), futureOrderBookUpdate.getQty(), futureOrderBookUpdate.getTimestamp())};
        }
        return new KrakenOrderBook(KrakenOrderBookMessageType.UPDATE, asks, bids);
    }

    public static ImmutablePair<KrakenFuturesProduct, LocalDate> validateAndPrepareSubscriptionParams(CurrencyPair currencyPair, Object [] args) {
        validateCurrencyPair(currencyPair);
        KrakenFuturesProduct product = getIndexedValue("product", 0, KrakenFuturesProduct.class, null, true, args);

        LocalDate maturityDate = null;
        if (product.mustHaveMaturityDate) {
            maturityDate = getIndexedValue("maturityDate", 1, LocalDate.class, null, true, args);
        }
        return ImmutablePair.of(product, maturityDate);
    }

    public static void validateCurrencyPair(CurrencyPair currencyPair) {
        if (currencyPair == null) {
            throw new KrakenException("Currency pair is mandatory");
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getIndexedValue(String name, int index, Class<T> expectedClass, T defaultValue, boolean failIfMissing, Object... args) throws KrakenException {
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

    public static String getChannelName(KrakenFuturesFeed feed, CurrencyPair currencyPair, KrakenFuturesProduct product, LocalDate maturityDate) {
        return feed + KRAKEN_CHANNEL_DELIMITER + product.formatProductId(currencyPair, maturityDate);
    }


}
