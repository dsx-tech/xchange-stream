package info.bitrich.xchangestream.dsx.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Pavel Chertalev on 15.03.2018.
 */
public class DsxWebSocketSubscriptionMessage {

    private final String method;
    private final int id;
    private final DsxWebSocketBaseParams params;

    public DsxWebSocketSubscriptionMessage(@JsonProperty("id") int id, @JsonProperty("method") String method, @JsonProperty("params") DsxWebSocketBaseParams params) {
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

    public DsxWebSocketBaseParams getParams() {
        return params;
    }
}
