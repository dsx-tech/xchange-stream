package info.bitrich.xchangestream.krakenFutures.dto;

import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesFeed;

import java.beans.ConstructorProperties;

/**
 * @author pchertalev
 */
public class KrakenFuturesProductUpdateMessage extends KrakenFuturesFeedMessage {

    private final String productId;

    @ConstructorProperties({"feed", "product_id"})
    public KrakenFuturesProductUpdateMessage(KrakenFuturesFeed feed, String productId) {
        super(feed);
        this.productId = productId;
    }

    public String getProductIds() {
        return productId;
    }
}
