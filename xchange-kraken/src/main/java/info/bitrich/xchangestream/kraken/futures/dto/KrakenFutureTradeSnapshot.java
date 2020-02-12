package info.bitrich.xchangestream.kraken.futures.dto;

import info.bitrich.xchangestream.kraken.futures.enums.KrakenFuturesFeed;

import java.beans.ConstructorProperties;
import java.util.List;

/**
 * @author pchertalev
 */
public class KrakenFutureTradeSnapshot extends KrakenFuturesProductUpdateMessage {

    private final List<KrakenFutureTrade> trades;

    @ConstructorProperties({"feed", "product_id", "trades"})
    public KrakenFutureTradeSnapshot(KrakenFuturesFeed feed, String productId, List<KrakenFutureTrade> trades) {
        super(feed, productId);
        this.trades = trades;
    }

    public List<KrakenFutureTrade> getTrades() {
        return trades;
    }
}
