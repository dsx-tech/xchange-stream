package info.bitrich.xchangestream.krakenFutures.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import info.bitrich.xchangestream.krakenFutures.dto.KrakenFuturesFeedMessage;
import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesFeed;

import java.beans.ConstructorProperties;
import java.util.List;

public class KrakenFuturesAccountBalances extends KrakenFuturesFeedMessage {

    /**
     * The subscription message sequence number
     */
    private final Long seq;

    /**
     * The user account name
     */
    private final String account;

    @JsonProperty("margin_accounts")
    private final List<KrakenFuturesMarginAccount> marginAccounts;

    @ConstructorProperties({"feed", "seq", "account", "margin_accounts"})
    public KrakenFuturesAccountBalances(KrakenFuturesFeed feed, Long seq, String account, List<KrakenFuturesMarginAccount> marginAccounts) {
        super(feed);
        this.seq = seq;
        this.account = account;
        this.marginAccounts = marginAccounts;
    }

    public Long getSeq() {
        return seq;
    }

    public String getAccount() {
        return account;
    }

    public List<KrakenFuturesMarginAccount> getMarginAccounts() {
        return marginAccounts;
    }
}
