package info.bitrich.xchangestream.bequant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Pavel Chertalev on 15.03.2018.
 */
public class BequantWebSocketOrderBookTransaction extends BequantWebSocketBaseTransaction {

    private static final String ORDERBOOK_METHOD_UPDATE = "updateOrderbook";
    private BequantWebSocketOrderBookParams params;

    public BequantWebSocketOrderBookTransaction(@JsonProperty("method") String method, @JsonProperty("params") BequantWebSocketOrderBookParams params) {
        super(method);
        this.params = params;
    }

    public BequantWebSocketOrderBookParams getParams() {
        return params;
    }

    public BequantWebSocketOrderBook toBequantOrderBook(BequantWebSocketOrderBook orderbook) {
        if (method.equals(ORDERBOOK_METHOD_UPDATE)) {
            orderbook.updateOrderBook(this);
            return orderbook;
        }
        return new BequantWebSocketOrderBook(this);
    }
}
