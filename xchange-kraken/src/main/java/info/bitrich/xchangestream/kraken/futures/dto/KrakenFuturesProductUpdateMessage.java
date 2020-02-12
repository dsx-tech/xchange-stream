package info.bitrich.xchangestream.kraken.futures.dto;

import info.bitrich.xchangestream.kraken.futures.enums.KrakenFuturesFeed;

import java.beans.ConstructorProperties;

/**
 * @author pchertalev
 */
public class KrakenFuturesProductUpdateMessage {

    private final KrakenFuturesFeed feed;

    private final String productId;

    @ConstructorProperties({"event", "product_id"})
    public KrakenFuturesProductUpdateMessage(KrakenFuturesFeed feed, String productId) {
        this.feed = feed;
        this.productId = productId;
    }

    public KrakenFuturesFeed getFeed() {
        return feed;
    }

    public String getProductIds() {
        return productId;
    }
}
