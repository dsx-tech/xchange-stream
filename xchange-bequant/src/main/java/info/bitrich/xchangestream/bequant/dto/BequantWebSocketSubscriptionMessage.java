package info.bitrich.xchangestream.bequant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Pavel Chertalev on 15.03.2018.
 */
public class BequantWebSocketSubscriptionMessage {

    private final String method;
    private final int id;
    private final BequantWebSocketBaseParams params;

    public BequantWebSocketSubscriptionMessage(@JsonProperty("id") int id, @JsonProperty("method") String method, @JsonProperty("params") BequantWebSocketBaseParams params) {
        this.id = id;
        this.method = method;
        this.params = params;
    }

    public String getMethod() {
        return method;
    }

    public int getId() {
        return id;
    }

    public BequantWebSocketBaseParams getParams() {
        return params;
    }
}
