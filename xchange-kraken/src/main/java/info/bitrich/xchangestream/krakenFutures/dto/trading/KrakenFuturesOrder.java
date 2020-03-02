package info.bitrich.xchangestream.krakenFutures.dto.trading;

import com.fasterxml.jackson.annotation.JsonProperty;
import info.bitrich.xchangestream.krakenFutures.dto.KrakenFuturesMessage;
import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesOpenOrderType;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;

public class KrakenFuturesOrder extends KrakenFuturesMessage {

    /**
     * The instrument (referred also as symbol or product_id) of the order
     */
    private final String instrument;

    /**
     * The UTC time in milliseconds
     */
    private final Long time;

    /**
     * The UTC time in milliseconds that the order was last updated
     */
    @JsonProperty("last_update_time")
    private final Long lastUpdateTime;

    /**
     * The remaining quantity of the order
     */
    private final BigDecimal qty;

    /**
     * The amount of the order that has been filled
     */
    private final BigDecimal filled;

    /**
     * The limit price of the order
     */
    @JsonProperty("limit_price")
    private final BigDecimal limitPrice;

    /**
     * The stop price of the order
     */
    @JsonProperty("stop_price")
    private final BigDecimal stopPrice;


    /**
     * The order type, limit or stop
     */
    private final KrakenFuturesOpenOrderType type;

    /**
     * The order id
     */
    @JsonProperty("order_id")
    private final String orderId;

    /**
     * The unique client order identifier. This field is returned only if the order has a client order id
     */
    @JsonProperty("cli_ord_id")
    private final String cliOrdId;

    /**
     * The direction of the order, either 0 for a buy order or 1 for a sell order
     */
    private final Integer direction;

    @JsonProperty("reduce_only")
    private final Boolean reduceOnly;

    @ConstructorProperties({"instrument", "time", "last_update_time", "qty", "filled", "limit_price", "stop_price", "type", "order_id", "cli_ord_id", "direction", "reduce_only"})
    public KrakenFuturesOrder(String instrument, Long time, Long lastUpdateTime, BigDecimal qty, BigDecimal filled, BigDecimal limitPrice, BigDecimal stopPrice,
                              KrakenFuturesOpenOrderType type, String orderId, String cliOrdId, Integer direction, Boolean reduceOnly) {
        this.instrument = instrument;
        this.time = time;
        this.lastUpdateTime = lastUpdateTime;
        this.qty = qty;
        this.filled = filled;
        this.limitPrice = limitPrice;
        this.stopPrice = stopPrice;
        this.type = type;
        this.orderId = orderId;
        this.cliOrdId = cliOrdId;
        this.direction = direction;
        this.reduceOnly = reduceOnly;
    }

    public String getInstrument() {
        return instrument;
    }

    public Long getTime() {
        return time;
    }

    public Long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public BigDecimal getFilled() {
        return filled;
    }

    public BigDecimal getLimitPrice() {
        return limitPrice;
    }

    public BigDecimal getStopPrice() {
        return stopPrice;
    }

    public KrakenFuturesOpenOrderType getType() {
        return type;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCliOrdId() {
        return cliOrdId;
    }

    public Integer getDirection() {
        return direction;
    }
}
