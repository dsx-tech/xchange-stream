package info.bitrich.xchangestream.krakenFutures.dto.trading;

import com.fasterxml.jackson.annotation.JsonProperty;
import info.bitrich.xchangestream.krakenFutures.dto.KrakenFuturesMessage;
import org.knowm.xchange.krakenFutures.dto.enums.KrakenFuturesFillType;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;

public class KrakenFuturesFill extends KrakenFuturesMessage {

    /**
     * 	The fill instrument (referred also as symbol or product_id)
     */
    private final String instrument;

    /**
     * The server UTC date and time in milliseconds
     */
    private final Long time;

    /**
     * The subscription message sequence number
     */
    private final Long seq;

    /**
     * 	A flag that shows if filled order was a buy
     */
    private final Boolean buy;

    /**
     * The quantity that was filled
     */
    private final BigDecimal qty;

    /**
     * 	The order id that was filled
     */
    @JsonProperty("order_id")
    private final String orderId;

    /**
     * The unique client order identifier. This field is returned only if the order has a client order id
     */
    @JsonProperty("cli_ord_id")
    private final String cliOrdId;

    /**
     * The fill id
     */
    @JsonProperty("fill_id")
    private final String fillId;

    /**
     * The classification of the fill:
     * "maker" if user has a limit order that gets filled,
     * "taker" if the user makes an execution that crosses the spread,
     * "liquidation" if execution is result of a liquidation,
     * "assignee" if execution is a result of a counterparty receiving an Assignment in PAS,
     * "assignor" if execution is a result of user assigning their position due to failed liquidation,
     * "takerAfterEdit" if the user edits the order and it is instantly executed.
     */
    @JsonProperty("fill_type")
    private final KrakenFuturesFillType fillType;

    @ConstructorProperties({"instrument", "time", "seq", "buy", "qty", "order_id", "cli_ord_id", "fill_id", "fill_type"})
    public KrakenFuturesFill(String instrument, Long time, Long seq, Boolean buy, BigDecimal qty, String orderId, String cliOrdId, String fillId, KrakenFuturesFillType fillType) {
        this.instrument = instrument;
        this.time = time;
        this.seq = seq;
        this.buy = buy;
        this.qty = qty;
        this.orderId = orderId;
        this.cliOrdId = cliOrdId;
        this.fillId = fillId;
        this.fillType = fillType;
    }

    public String getInstrument() {
        return instrument;
    }

    public Long getTime() {
        return time;
    }

    public Long getSeq() {
        return seq;
    }

    public Boolean getBuy() {
        return buy;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCliOrdId() {
        return cliOrdId;
    }

    public String getFillId() {
        return fillId;
    }

    public KrakenFuturesFillType getFillType() {
        return fillType;
    }
}
