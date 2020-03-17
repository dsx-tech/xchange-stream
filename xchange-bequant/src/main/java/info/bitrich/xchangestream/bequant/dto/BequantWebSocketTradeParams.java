package info.bitrich.xchangestream.bequant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.knowm.xchange.bequant.v2.dto.BequantTrade;

/**
 * Created by Pavel Chertalev on 15.03.2018.
 */
public class BequantWebSocketTradeParams extends BequantWebSocketBaseParams {

    private final BequantTrade[] data;

    public BequantWebSocketTradeParams(@JsonProperty("symbol") String symbol, @JsonProperty("params") BequantTrade[] data) {
        super(symbol);
        this.data = data;
    }

    public BequantTrade[] getData() {
        return data;
    }

}

