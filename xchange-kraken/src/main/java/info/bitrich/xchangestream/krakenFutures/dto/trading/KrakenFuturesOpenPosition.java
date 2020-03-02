package info.bitrich.xchangestream.krakenFutures.dto.trading;

import com.fasterxml.jackson.annotation.JsonProperty;
import info.bitrich.xchangestream.krakenFutures.dto.KrakenFuturesMessage;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;

public class KrakenFuturesOpenPosition extends KrakenFuturesMessage {

    /**
     * The instrument (referred also as symbol or product_id) of the position
     */
    private final String instrument;

    /**
     * The size of the position
     */
    private final BigDecimal balance;

    /**
     * The average entry price of the instrument
     */
    @JsonProperty("entry_price")
    private final BigDecimal entryPrice;

    /**
     * The market price of the position instrument
     */
    @JsonProperty("mark_price")
    private final BigDecimal markPrice;

    /**
     * The index price of the position instrument
     */
    @JsonProperty("index_price")
    private final BigDecimal indexPrice;

    /**
     * The profit and loss of the position
     */
    @JsonProperty("pnl")
    private final BigDecimal pnl;

    /**
     * The mark price of the contract at which the position will be liquidated.
     */
    @JsonProperty("liquidation_threshold")
    private final BigDecimal liquidationThreshold;

    /**
     * The percentage gain or loss relative to the initial margin used in the position. Formula: PnL/IM
     */
    @JsonProperty("return_on_equity")
    private final BigDecimal returOnEquity;

    /**
     * How leveraged the net position is in a given margin account. Formula: Position Value at Market / Portfolio Value
     */
    @JsonProperty("effective_leverage")
    private final BigDecimal effectiveLeverage;


    @ConstructorProperties({"instrument", "balance", "entry_price", "mark_price", "index_price", "pnl", "liquidation_threshold", "return_on_equity", "effective_leverage"})
    public KrakenFuturesOpenPosition(String instrument, BigDecimal balance, BigDecimal entryPrice, BigDecimal markPrice, BigDecimal indexPrice, BigDecimal pnl,
                                     BigDecimal liquidationThreshold, BigDecimal returOnEquity, BigDecimal effectiveLeverage) {
        this.instrument = instrument;
        this.balance = balance;
        this.entryPrice = entryPrice;
        this.markPrice = markPrice;
        this.indexPrice = indexPrice;
        this.pnl = pnl;
        this.liquidationThreshold = liquidationThreshold;
        this.returOnEquity = returOnEquity;
        this.effectiveLeverage = effectiveLeverage;
    }

    public String getInstrument() {
        return instrument;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getEntryPrice() {
        return entryPrice;
    }

    public BigDecimal getMarkPrice() {
        return markPrice;
    }

    public BigDecimal getIndexPrice() {
        return indexPrice;
    }

    public BigDecimal getPnl() {
        return pnl;
    }

    public BigDecimal getLiquidationThreshold() {
        return liquidationThreshold;
    }

    public BigDecimal getReturOnEquity() {
        return returOnEquity;
    }

    public BigDecimal getEffectiveLeverage() {
        return effectiveLeverage;
    }
}
