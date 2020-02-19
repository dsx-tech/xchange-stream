package info.bitrich.xchangestream.krakenFutures.dto;

import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesFeed;

import java.beans.ConstructorProperties;

/**
 * @author pchertalev
 */
public class KrakenFutureOrderBook extends KrakenFuturesProductUpdateMessage {

    private Long seq;

    private KrakenFutureOrderBookItem[] asks;
    private KrakenFutureOrderBookItem[] bids;

    private Long timestamp;

    @ConstructorProperties({"feed", "timestamp", "product_id", "seq", "asks", "bids"})
    public KrakenFutureOrderBook(KrakenFuturesFeed feed, Long timestamp, String productId, Long seq, KrakenFutureOrderBookItem[] asks, KrakenFutureOrderBookItem[] bids) {
        super(feed, productId);
        this.seq = seq;
        this.asks = asks;
        this.bids = bids;
        this.timestamp = timestamp;
    }

    public Long getSeq() {
        return seq;
    }

    public KrakenFutureOrderBookItem[] getAsks() {
        return asks;
    }

    public KrakenFutureOrderBookItem[] getBids() {
        return bids;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
