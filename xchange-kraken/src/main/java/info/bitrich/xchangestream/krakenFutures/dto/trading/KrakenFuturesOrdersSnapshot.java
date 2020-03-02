package info.bitrich.xchangestream.krakenFutures.dto.trading;

import info.bitrich.xchangestream.krakenFutures.dto.KrakenFuturesFeedMessage;
import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesFeed;

import java.beans.ConstructorProperties;
import java.util.List;

public class KrakenFuturesOrdersSnapshot extends KrakenFuturesFeedMessage {

    /**
     * The user account
     */
    private final String account;

    /**
     * A list containing the user open orders
     */
    private final List<KrakenFuturesOrder> orders;


    @ConstructorProperties({"feed", "account", "orders"})
    public KrakenFuturesOrdersSnapshot(KrakenFuturesFeed feed, String account, List<KrakenFuturesOrder> orders) {
        super(feed);
        this.account = account;
        this.orders = orders;
    }

    public String getAccount() {
        return account;
    }

    public List<KrakenFuturesOrder> getOrders() {
        return orders;
    }
}