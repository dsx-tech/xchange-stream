package info.bitrich.xchangestream.krakenFutures.dto.trading;

import info.bitrich.xchangestream.krakenFutures.dto.KrakenFuturesFeedMessage;
import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesFeed;

import java.beans.ConstructorProperties;
import java.util.List;

public class KrakenFuturesOpenPositions extends KrakenFuturesFeedMessage {

    /**
     * The user account
     */
    private final String account;

    /**
     * A list containing the user open positions
     */
    private final List<KrakenFuturesOpenPosition> positions;

    @ConstructorProperties({"feed", "account", "positions"})
    public KrakenFuturesOpenPositions(KrakenFuturesFeed feed, String account, List<KrakenFuturesOpenPosition> positions) {
        super(feed);
        this.account = account;
        this.positions = positions;
    }

    public String getAccount() {
        return account;
    }

    public List<KrakenFuturesOpenPosition> getPositions() {
        return positions;
    }
}
