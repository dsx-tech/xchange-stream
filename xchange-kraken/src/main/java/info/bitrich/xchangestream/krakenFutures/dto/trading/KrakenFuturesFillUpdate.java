package info.bitrich.xchangestream.krakenFutures.dto.trading;

import info.bitrich.xchangestream.krakenFutures.dto.KrakenFuturesFeedMessage;
import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesFeed;

import java.beans.ConstructorProperties;
import java.util.List;

public class KrakenFuturesFillUpdate extends KrakenFuturesFeedMessage {

    /**
     * The user name
     */
    private final String username;

    /**
     * A list containing fill elements of the user account
     */
    private final List<KrakenFuturesFill> fills;

    @ConstructorProperties({"feed", "account", "fills"})
    public KrakenFuturesFillUpdate(KrakenFuturesFeed feed, String username, List<KrakenFuturesFill> fills) {
        super(feed);
        this.username = username;
        this.fills = fills;
    }

    public String getUsername() {
        return username;
    }

    public List<KrakenFuturesFill> getFills() {
        return fills;
    }
}
