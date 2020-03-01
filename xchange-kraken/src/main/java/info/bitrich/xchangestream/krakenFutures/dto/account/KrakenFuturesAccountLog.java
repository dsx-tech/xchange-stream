package info.bitrich.xchangestream.krakenFutures.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import info.bitrich.xchangestream.krakenFutures.dto.KrakenFuturesMessage;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;
import java.util.Date;

public class KrakenFuturesAccountLog extends KrakenFuturesMessage {

    /**
     * The identifier of the log
     */
    private final Long id;

    /**
     * The creation time of the log according to server date and time
     */
    private final Date date;

    /**
     * The asset related of the booking
     */
    private final String asset;

    /**
     * A description of the booking
     */
    private final String info;

    /**
     * The unique id of the booking
     */
    @JsonProperty("booking_uid")
    private final String bookingUid;

    /**
     * The account name
     */
    @JsonProperty("margin_account")
    private final String marginAccount;

    /**
     * 	The account balance before the described in info action
     */
    @JsonProperty("old_balance")
    private final BigDecimal oldBalance;

    /**
     * The portfolio value calculated as balance plus unrealized pnl value
     */
    @JsonProperty("new_balance")
    private final BigDecimal newBalance;

    /**
     * The average entry price of the position prior to this trade
     */
    @JsonProperty("old_average_entry_price")
    private final BigDecimal oldAverageEntryPrice;

    /**
     * The average entry price of the position after this trade
     */
    @JsonProperty("new_average_entry_price")
    private final BigDecimal newAverageEntryPrice;

    /**
     * The price the trade was executed at
     */
    @JsonProperty("trade_price")
    private final BigDecimal tradePrice;

    /**
     * The mark price at the time the trade was executed
     */
    @JsonProperty("mark_price")
    private final BigDecimal markPrice;

    /**
     * The pnl that is realized by reducing the position
     */
    @JsonProperty("realized_pnl")
    private final BigDecimal realizedPnl;

    /**
     * The fee paid
     */
    private final BigDecimal fee;

    /**
     * The uid of the associated execution
     */
    private final String execution;

    /**
     * The currency of the associated entry
     */
    private final String collateral;

    /**
     *
     * The absolute funding rate
     */
    @JsonProperty("funding_rate")
    private final BigDecimal fundingRate;

    /**
     *
     * The funding rate realized due to change in position size or end of funding rate period
     */
    @JsonProperty("realized_funding")
    private final BigDecimal realizedFunding;

    @ConstructorProperties({"id", "date", "asset", "info", "booking_uid", "margin_account", "old_balance", "new_balance", "old_average_entry_price",
            "new_average_entry_price", "trade_price", "mark_price", "realized_pnl", "fee", "execution", "collateral", "funding_rate", "realized_funding"})
    public KrakenFuturesAccountLog(Long id, Date date, String asset, String info, String bookingUid, String marginAccount, BigDecimal oldBalance, BigDecimal newBalance,
                                   BigDecimal oldAverageEntryPrice, BigDecimal newAverageEntryPrice, BigDecimal tradePrice, BigDecimal markPrice, BigDecimal realizedPnl,
                                   BigDecimal fee, String execution, String collateral, BigDecimal fundingRate, BigDecimal realizedFunding) {
        this.id = id;
        this.date = date;
        this.asset = asset;
        this.info = info;
        this.bookingUid = bookingUid;
        this.marginAccount = marginAccount;
        this.oldBalance = oldBalance;
        this.newBalance = newBalance;
        this.oldAverageEntryPrice = oldAverageEntryPrice;
        this.newAverageEntryPrice = newAverageEntryPrice;
        this.tradePrice = tradePrice;
        this.markPrice = markPrice;
        this.realizedPnl = realizedPnl;
        this.fee = fee;
        this.execution = execution;
        this.collateral = collateral;
        this.fundingRate = fundingRate;
        this.realizedFunding = realizedFunding;
    }

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getAsset() {
        return asset;
    }

    public String getInfo() {
        return info;
    }

    public String getBookingUid() {
        return bookingUid;
    }

    public String getMarginAccount() {
        return marginAccount;
    }

    public BigDecimal getOldBalance() {
        return oldBalance;
    }

    public BigDecimal getNewBalance() {
        return newBalance;
    }

    public BigDecimal getOldAverageEntryPrice() {
        return oldAverageEntryPrice;
    }

    public BigDecimal getNewAverageEntryPrice() {
        return newAverageEntryPrice;
    }

    public BigDecimal getTradePrice() {
        return tradePrice;
    }

    public BigDecimal getMarkPrice() {
        return markPrice;
    }

    public BigDecimal getRealizedPnl() {
        return realizedPnl;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public String getExecution() {
        return execution;
    }

    public String getCollateral() {
        return collateral;
    }

    public BigDecimal getFundingRate() {
        return fundingRate;
    }

    public BigDecimal getRealizedFunding() {
        return realizedFunding;
    }


}
