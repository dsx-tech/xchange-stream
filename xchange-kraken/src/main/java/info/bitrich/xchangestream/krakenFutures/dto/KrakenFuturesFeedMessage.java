package info.bitrich.xchangestream.krakenFutures.dto;

import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesFeed;

import java.beans.ConstructorProperties;

/**
 * @author pchertalev
 */
public class KrakenFuturesFeedMessage extends KrakenFuturesMessage {

    private final KrakenFuturesFeed feed;

    @ConstructorProperties("feed")
    public KrakenFuturesFeedMessage(KrakenFuturesFeed feed) {
        this.feed = feed;
    }

    public KrakenFuturesFeed getFeed() {
        return feed;
    }

}