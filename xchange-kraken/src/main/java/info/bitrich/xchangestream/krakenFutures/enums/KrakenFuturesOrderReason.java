package info.bitrich.xchangestream.krakenFutures.enums;

public enum KrakenFuturesOrderReason {

    /**
     * User placed a new order
     */
    new_placed_order_by_user,

    /**
     * User position liquidated. The order cancelled
     */
    liquidation,

    /**
     * A stop order triggered. The system  removed the stop order
     */
    stop_order_triggered,

    /**
     * The system created a limit order because an existing stop order triggered
     */
    limit_order_from_stop,

    /**
     * The order filled partially
     */
    partial_fill,

    /**
     * The order filled fully and removed
     */
    full_fill,

    /**
     * The order cancelled by the user and removed
     */
    cancelled_by_user,

    /**
     * The order contract expired. All open orders of that contract removed
     */
    contract_expired,

    /**
     * The order removed due to insufficient margin
     */
    not_enough_margin,

    /**
     * The order removed because market became inactive
     */
    market_inactive,

    /**
     * The order removed by administrator's action
     */
    cancelled_by_admin
}
