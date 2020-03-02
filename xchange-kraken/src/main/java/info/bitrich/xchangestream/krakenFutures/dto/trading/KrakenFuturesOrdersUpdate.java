package info.bitrich.xchangestream.krakenFutures.dto.trading;

import com.fasterxml.jackson.annotation.JsonProperty;
import info.bitrich.xchangestream.krakenFutures.dto.KrakenFuturesFeedMessage;
import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesFeed;
import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesOrderReason;

import java.beans.ConstructorProperties;

public class KrakenFuturesOrdersUpdate extends KrakenFuturesFeedMessage {

    /**
     * The user new order
     */
    private final KrakenFuturesOrder order;

    /**
     * If false the open order has been either placed or partially filled and needs to be updated.
     * If true the open order was either fully filled or cancelled and must be removed from open orders snapshot
     */
    @JsonProperty("is_cancel")
    private final Boolean isCancel;

    /**
     * Reason behind the received delta.
     */
    private final KrakenFuturesOrderReason reason;

    @ConstructorProperties({"feed", "order", "is_cancel", "reason"})
    public KrakenFuturesOrdersUpdate(KrakenFuturesFeed feed, KrakenFuturesOrder order, Boolean isCancel, KrakenFuturesOrderReason reason) {
        super(feed);
        this.order = order;
        this.isCancel = isCancel;
        this.reason = reason;
    }

    public KrakenFuturesOrder getOrder() {
        return order;
    }

    public Boolean getCancel() {
        return isCancel;
    }

    public KrakenFuturesOrderReason getReason() {
        return reason;
    }
}
