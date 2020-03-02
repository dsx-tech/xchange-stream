package info.bitrich.xchangestream.krakenFutures.enums;

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

    account_balances_and_margins(true),

    account_log(true),
    account_log_snapshot(account_log),

    deposits_withdrawals(true),

    fills(true),
    fills_snapshot(fills),

    open_positions(true),

    open_orders(true),
    open_orders_snapshot(open_orders),

    notifications_auth(true),

    heartbeat;

    public final KrakenFuturesFeed sourceFeed;
    public final boolean auth;


    KrakenFuturesFeed() {
        this.sourceFeed = this;
        this.auth = false;
    }

    KrakenFuturesFeed(boolean isAuth) {
        this.sourceFeed = this;
        this.auth = isAuth;
    }

    KrakenFuturesFeed(KrakenFuturesFeed sourceFeed) {
        this(sourceFeed, false);
    }

    KrakenFuturesFeed(KrakenFuturesFeed sourceFeed, boolean isAuth) {
        this.sourceFeed = sourceFeed;
        this.auth = isAuth;
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
