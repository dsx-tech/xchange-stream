package info.bitrich.xchangestream.bequant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Pavel Chertalev on 15.03.2018.
 */
public class BequantWebSocketTradesTransaction extends BequantWebSocketBaseTransaction {
    private final BequantWebSocketTradeParams params;

    public BequantWebSocketTradesTransaction(@JsonProperty("method") String method, @JsonProperty("params") BequantWebSocketTradeParams params) {
        super(method);
        this.params = params;
    }

    public BequantWebSocketTradeParams getParams() {
        return params;
    }

}
