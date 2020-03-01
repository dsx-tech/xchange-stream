package info.bitrich.xchangestream.krakenFutures.dto.trading;

import info.bitrich.xchangestream.krakenFutures.dto.KrakenFuturesFeedMessage;
import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesFeed;

import java.beans.ConstructorProperties;
import java.util.List;

public class KrakenFuturesFillSnapshot extends KrakenFuturesFeedMessage {

    /**
     * The user account
     */
    private final String account;

    /**
     * A list containing fill elements of the user account
     */
    private final List<KrakenFuturesFill> fills;

    @ConstructorProperties({"feed", "account", "fills"})
    public KrakenFuturesFillSnapshot(KrakenFuturesFeed feed, String account, List<KrakenFuturesFill> fills) {
        super(feed);
        this.account = account;
        this.fills = fills;
    }

    public String getAccount() {
        return account;
    }

    public List<KrakenFuturesFill> getFills() {
        return fills;
    }
}
