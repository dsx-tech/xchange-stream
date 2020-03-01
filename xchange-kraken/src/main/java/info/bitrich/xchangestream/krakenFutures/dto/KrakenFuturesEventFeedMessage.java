package info.bitrich.xchangestream.krakenFutures.dto;

import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesEventType;
import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesFeed;

import java.beans.ConstructorProperties;

/**
 * @author pchertalev
 */
public class KrakenFuturesEventFeedMessage extends KrakenFuturesEventMessage {

    private final KrakenFuturesFeed feed;

    @ConstructorProperties({"event", "feed"})
    public KrakenFuturesEventFeedMessage(KrakenFuturesEventType event, KrakenFuturesFeed feed) {
        super(event);
        this.feed = feed;
    }

    public KrakenFuturesFeed getFeed() {
        return feed;
    }
}
