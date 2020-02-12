package info.bitrich.xchangestream.kraken.futures;

import info.bitrich.xchangestream.kraken.dto.KrakenOrderBook;
import info.bitrich.xchangestream.kraken.dto.enums.KrakenOrderBookMessageType;
import info.bitrich.xchangestream.kraken.futures.dto.KrakenFutureOrderBook;
import info.bitrich.xchangestream.kraken.futures.dto.KrakenFutureOrderBookUpdate;
import info.bitrich.xchangestream.kraken.futures.enums.KrakenFuturesSide;
import org.knowm.xchange.kraken.dto.marketdata.KrakenPublicOrder;

import java.util.Arrays;

public class KrakenFuturesUtils {

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

}
