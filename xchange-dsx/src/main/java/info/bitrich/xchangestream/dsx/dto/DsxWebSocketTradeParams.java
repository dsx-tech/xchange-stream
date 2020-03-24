package info.bitrich.xchangestream.dsx.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.knowm.xchange.dsx.v2.dto.DsxTrade;

/**
 * Created by Pavel Chertalev on 15.03.2018.
 */
public class DsxWebSocketTradeParams extends DsxWebSocketBaseParams {

    private final DsxTrade[] data;

    public DsxWebSocketTradeParams(@JsonProperty("symbol") String symbol, @JsonProperty("params") DsxTrade[] data) {
        super(symbol);
        this.data = data;
    }

    public DsxTrade[] getData() {
        return data;
    }

}

