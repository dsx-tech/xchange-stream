package info.bitrich.xchangestream.kraken.futures.enums;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

import static info.bitrich.xchangestream.kraken.KrakenConstants.KRAKEN_FUTURES_FEED;

public enum KrakenFuturesFeed {
    book,
    book_snapshot(book),
    ticker,
    trade,
    trade_snapshot(trade),
    heartbeat;

    public final KrakenFuturesFeed sourceFeed;


    KrakenFuturesFeed() {
        this.sourceFeed = this;
    }

    KrakenFuturesFeed(KrakenFuturesFeed sourceFeed) {
        this.sourceFeed = sourceFeed;
    }

    public boolean equalsJsonNode(JsonNode jsonNode) {
        if (jsonNode.has(KRAKEN_FUTURES_FEED)) {
            JsonNode feedNode = jsonNode.get(KRAKEN_FUTURES_FEED);
            return this.name().equals(feedNode.asText());
        }
        return false;
    }

    public static KrakenFuturesFeed getFeed(String name) {
        return Arrays.stream(KrakenFuturesFeed.values())
                .filter(feed -> StringUtils.containsIgnoreCase(feed.name(), name))
                .findFirst()
                .orElse(null);
    }

    public static KrakenFuturesFeed getFeed(JsonNode jsonNode) {
        if (jsonNode.has(KRAKEN_FUTURES_FEED)) {
            JsonNode feedNode = jsonNode.get(KRAKEN_FUTURES_FEED);
            return getFeed(feedNode.asText());
        }
        return null;
    }

}
