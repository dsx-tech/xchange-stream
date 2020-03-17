package info.bitrich.xchangestream.bequant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Pavel Chertalev on 15.03.2018.
 */
public class BequantWebSocketBaseParams {

    protected final String symbol;

    public BequantWebSocketBaseParams(@JsonProperty("symbol") String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

}
