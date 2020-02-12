package info.bitrich.xchangestream.kraken.futures.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import info.bitrich.xchangestream.kraken.futures.enums.KrakenFuturesFeed;

import java.beans.ConstructorProperties;

/**
 * @author pchertalev
 */
public class KrakenFuturesProductUpdateMessage {

    @JsonProperty
    private final KrakenFuturesFeed feed;

    @JsonProperty(value = "product_id", required = true)
    private final String productId;

    @JsonCreator
    @ConstructorProperties({"event", "product_ids"})
    public KrakenFuturesProductUpdateMessage(@JsonProperty("feed") KrakenFuturesFeed feed, @JsonProperty("product_id") String productId) {
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
