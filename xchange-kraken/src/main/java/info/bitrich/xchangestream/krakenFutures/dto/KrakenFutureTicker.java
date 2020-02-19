package info.bitrich.xchangestream.krakenFutures.dto;

import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesFeed;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;

/**
 * @author pchertalev
 */
public class KrakenFutureTicker extends KrakenFuturesProductUpdateMessage {

    /**
     * The price of the current best bid: positive float
     */
    private final BigDecimal bid;

    /**
     * The price of the current best ask: positive float
     */
    private final BigDecimal ask;

    /**
     * The size of the current best bid: positive float
     */
    private final BigDecimal bidSize;

    /**
     * The size of the current best ask: positive float
     */
    private final BigDecimal askSize;

    /**
     * The sum of the sizes of all fills observed in the last 24 hours: positive float
     */
    private final BigDecimal volume;

    /**
     * The days until maturity
     */
    private final BigDecimal dtm;

    /**
     * The leverage of the product
     */
    private final String leverage;

    /**
     * The real time index of the product: positive float
     */
    private final BigDecimal index;

    /**
     * The premium associated with the product: float
     */
    private final BigDecimal premium;

    /**
     * The price of the last trade of the product: positive float
     */
    private final BigDecimal last;

    /**
     * The UTC time of the server in milliseconds
     */
    private Long time;

    /**
     * The 24h change in price
     */
    private final BigDecimal change;

    /**
     *
     * Currently can be 'week', 'month' or 'quarter'. Other tags may be added without notice.
     */
    private final String tag;

    /**
     * The currency pair of the instrument
     */
    private final String pair;

    /**
     * The current open interest of the instrument
     */
    private final BigDecimal openInterest;

    /**
     * The market price of the instrument
     */
    private final BigDecimal markPrice;

    /**
     * The UTC time, in milliseconds, at which the contract will stop trading
     */
    private final Long maturityTime;

    /**
     * The current funding rate. (Perpetuals only)
     */
    private final BigDecimal fundingRate;

    /**
     * The absolute funding rate relative to the spot price at the time of funding rate calculation. Perpetuals only)
     */
    private final BigDecimal relativeFundingRatePrediction;

    /**
     * The estimated next funding rate. (Perpetuals only)
     */
    private final BigDecimal fundingRatePrediction;

    /**
     * The time until next funding rate in milliseconds. (Perpetuals only)
     */
    private final Long nextFundingRateTime;

    @ConstructorProperties({
            "feed", "product_id", "bid", "ask", "bid_size", "ask_size", "volume", "dtm", "leverage", "index", "premium",
            "last", "time", "change", "tag", "pair", "openInterest", "markPrice", "maturityTime", "funding_rate", "relative_funding_rate_prediction",
            "funding_rate_prediction", "next_funding_rate_time"
    })
    public KrakenFutureTicker(KrakenFuturesFeed feed, String productId, BigDecimal bid, BigDecimal ask, BigDecimal bidSize, BigDecimal askSize, BigDecimal volume,
                              BigDecimal dtm, String leverage, BigDecimal index, BigDecimal premium, BigDecimal last,
                              Long time, BigDecimal change, String tag, String pair, BigDecimal openInterest,
                              BigDecimal markPrice, Long maturityTime, BigDecimal fundingRate, BigDecimal relativeFundingRatePrediction,
                              BigDecimal fundingRatePrediction, Long nextFundingRateTime) {
        super(feed, productId);
        this.bid = bid;
        this.ask = ask;
        this.bidSize = bidSize;
        this.askSize = askSize;
        this.volume = volume;
        this.dtm = dtm;
        this.leverage = leverage;
        this.index = index;
        this.premium = premium;
        this.last = last;
        this.time = time;
        this.change = change;
        this.tag = tag;
        this.pair = pair;
        this.openInterest = openInterest;
        this.markPrice = markPrice;
        this.maturityTime = maturityTime;
        this.fundingRate = fundingRate;
        this.relativeFundingRatePrediction = relativeFundingRatePrediction;
        this.fundingRatePrediction = fundingRatePrediction;
        this.nextFundingRateTime = nextFundingRateTime;
    }

    public BigDecimal getBid() {
        return bid;
    }

    public BigDecimal getAsk() {
        return ask;
    }

    public BigDecimal getBidSize() {
        return bidSize;
    }

    public BigDecimal getAskSize() {
        return askSize;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public BigDecimal getDtm() {
        return dtm;
    }

    public String getLeverage() {
        return leverage;
    }

    public BigDecimal getIndex() {
        return index;
    }

    public BigDecimal getPremium() {
        return premium;
    }

    public BigDecimal getLast() {
        return last;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public BigDecimal getChange() {
        return change;
    }

    public String getTag() {
        return tag;
    }

    public String getPair() {
        return pair;
    }

    public BigDecimal getOpenInterest() {
        return openInterest;
    }

    public BigDecimal getMarkPrice() {
        return markPrice;
    }

    public Long getMaturityTime() {
        return maturityTime;
    }

    public BigDecimal getFundingRate() {
        return fundingRate;
    }

    public BigDecimal getRelativeFundingRatePrediction() {
        return relativeFundingRatePrediction;
    }

    public BigDecimal getFundingRatePrediction() {
        return fundingRatePrediction;
    }

    public Long getNextFundingRateTime() {
        return nextFundingRateTime;
    }
}
