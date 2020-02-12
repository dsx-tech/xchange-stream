package info.bitrich.xchangestream.kraken.futures.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import info.bitrich.xchangestream.kraken.futures.enums.KrakenFuturesEventType;
import info.bitrich.xchangestream.kraken.futures.enums.KrakenFuturesFeed;

import java.beans.ConstructorProperties;
import java.util.List;

/**
 * @author pchertalev
 */
public class KrakenFuturesProductMessage extends KrakenFuturesEventMessage {

    @JsonProperty
    private final KrakenFuturesFeed feed;

    @JsonProperty(value = "product_ids", required = true)
    private final List<String> productIds;

    @ConstructorProperties({"event", "feed", "product_ids"})
    public KrakenFuturesProductMessage(KrakenFuturesEventType event, KrakenFuturesFeed feed, List<String> productIds) {
        super(event);
        this.feed = feed;
        this.productIds = productIds;
    }

    public KrakenFuturesFeed getFeed() {
        return feed;
    }

    public List<String> getProductIds() {
        return productIds;
    }
}
