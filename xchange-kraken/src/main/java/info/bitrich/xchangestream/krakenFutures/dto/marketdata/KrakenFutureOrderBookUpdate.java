package info.bitrich.xchangestream.krakenFutures.dto.marketdata;

import info.bitrich.xchangestream.krakenFutures.dto.KrakenFuturesProductUpdateMessage;
import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesFeed;
import org.knowm.xchange.krakenFutures.dto.enums.KrakenFuturesSide;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;

/**
 * @author pchertalev
 */
public class KrakenFutureOrderBookUpdate extends KrakenFuturesProductUpdateMessage {

    private Long seq;
    private KrakenFuturesSide side;
    private BigDecimal price;
    private BigDecimal qty;

    private Long timestamp;

    @ConstructorProperties({"side", "timestamp", "price", "qty", "feed", "product_id", "seq"})
    public KrakenFutureOrderBookUpdate(KrakenFuturesSide side, Long timestamp, BigDecimal price, BigDecimal qty, KrakenFuturesFeed feed, String productId, Long seq) {
        super(feed, productId);
        this.seq = seq;
        this.side = side;
        this.price = price;
        this.qty = qty;
        this.timestamp = timestamp;
    }

    public Long getSeq() {
        return seq;
    }

    public KrakenFuturesSide getSide() {
        return side;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
