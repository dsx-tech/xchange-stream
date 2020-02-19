package info.bitrich.xchangestream.krakenFutures.dto;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;

/**
 * @author pchertalev
 */
public class KrakenFutureOrderBookItem {

    private final BigDecimal price;
    private final BigDecimal qty;

    @ConstructorProperties({"price", "qty"})
    public KrakenFutureOrderBookItem(BigDecimal price, BigDecimal qty) {
        this.price = price;
        this.qty = qty;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getQty() {
        return qty;
    }
}
