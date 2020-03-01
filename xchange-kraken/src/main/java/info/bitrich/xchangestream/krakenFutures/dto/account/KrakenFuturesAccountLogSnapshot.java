package info.bitrich.xchangestream.krakenFutures.dto.account;

import info.bitrich.xchangestream.krakenFutures.dto.KrakenFuturesFeedMessage;
import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesFeed;

import java.beans.ConstructorProperties;
import java.util.List;

public class KrakenFuturesAccountLogSnapshot extends KrakenFuturesFeedMessage {

    private final List<KrakenFuturesAccountLog> logs;

    @ConstructorProperties({"feed", "logs"})
    public KrakenFuturesAccountLogSnapshot(KrakenFuturesFeed feed, List<KrakenFuturesAccountLog> logs) {
        super(feed);
        this.logs = logs;
    }

    public List<KrakenFuturesAccountLog> getLogs() {
        return logs;
    }
}
