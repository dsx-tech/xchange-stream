package info.bitrich.xchangestream.dsx.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Pavel Chertalev on 15.03.2018.
 */
public class DsxWebSocketTradesTransaction extends DsxWebSocketBaseTransaction {
    private final DsxWebSocketTradeParams params;

    public DsxWebSocketTradesTransaction(@JsonProperty("method") String method, @JsonProperty("params") DsxWebSocketTradeParams params) {
        super(method);
        this.params = params;
    }

    public DsxWebSocketTradeParams getParams() {
        return params;
    }

}
