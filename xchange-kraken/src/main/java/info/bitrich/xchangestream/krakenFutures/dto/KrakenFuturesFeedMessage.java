package info.bitrich.xchangestream.krakenFutures.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesEventType;
import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesFeed;

import java.beans.ConstructorProperties;

/**
 * @author pchertalev
 */
public class KrakenFuturesFeedMessage extends KrakenFuturesEventMessage {

    @JsonProperty("feed")
    private final KrakenFuturesFeed feed;

    @ConstructorProperties({"event", "feed"})
    public KrakenFuturesFeedMessage(KrakenFuturesEventType event, KrakenFuturesFeed feed) {
        super(event);
        this.feed = feed;
    }

    public KrakenFuturesFeed getFeed() {
        return feed;
    }
}
