package info.bitrich.xchangestream.krakenFutures.dto.account;

import info.bitrich.xchangestream.krakenFutures.dto.KrakenFuturesMessage;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;

public class KrakenFuturesMarginAccount extends KrakenFuturesMessage {
    /**
     * The name of the account
     */
    private final String name;

    /**
     * The current balance of the account
     */
    private final BigDecimal balance;

    /**
     * The profit and loss of the account
     */
    private final BigDecimal pnl;

    /**
     * The portfolio value calculated as balance plus unrealized pnl value
     */
    private final BigDecimal pv;

    /**
     * The available margin for opening new positions
     */
    private final BigDecimal am;

    /**
     * The initial margin for open positions and orders
     */
    private final BigDecimal im;

    /**
     * The maintenance margin for open positions
     */
    private final BigDecimal mm;

    @ConstructorProperties({"name", "balance", "pnl", "pv", "am", "im", "mm"})
    public KrakenFuturesMarginAccount(String name, BigDecimal balance, BigDecimal pnl, BigDecimal pv, BigDecimal am, BigDecimal im, BigDecimal mm) {
        this.name = name;
        this.balance = balance;
        this.pnl = pnl;
        this.pv = pv;
        this.am = am;
        this.im = im;
        this.mm = mm;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getPnl() {
        return pnl;
    }

    public BigDecimal getPv() {
        return pv;
    }

    public BigDecimal getAm() {
        return am;
    }

    public BigDecimal getIm() {
        return im;
    }

    public BigDecimal getMm() {
        return mm;
    }
}
