package info.bitrich.xchangestream.krakenFutures.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import info.bitrich.xchangestream.krakenFutures.dto.KrakenFuturesFeedMessage;
import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesFeed;

import java.beans.ConstructorProperties;

public class KrakenFuturesAccountLogUpdate extends KrakenFuturesFeedMessage {

    @JsonProperty("new_entry")
    private final KrakenFuturesAccountLog newEntry;

    @ConstructorProperties({"feed", "new_entry"})
    public KrakenFuturesAccountLogUpdate(KrakenFuturesFeed feed, KrakenFuturesAccountLog newEntry) {
        super(feed);
        this.newEntry = newEntry;
    }

    public KrakenFuturesAccountLog getNewEntry() {
        return newEntry;
    }
}
