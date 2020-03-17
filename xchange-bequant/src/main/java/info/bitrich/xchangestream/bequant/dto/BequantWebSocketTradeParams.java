package info.bitrich.xchangestream.bequant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.knowm.xchange.hitbtc.v2.dto.HitbtcTrade;

/**
 * Created by Pavel Chertalev on 15.03.2018.
 */
public class BequantWebSocketTradeParams extends BequantWebSocketBaseParams {

    private final HitbtcTrade[] data;

    public BequantWebSocketTradeParams(@JsonProperty("symbol") String symbol, @JsonProperty("params") HitbtcTrade[] data) {
        super(symbol);
        this.data = data;
    }

    public HitbtcTrade[] getData() {
        return data;
    }

}

