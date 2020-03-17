package info.bitrich.xchangestream.bequant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.knowm.xchange.bequant.v2.dto.BequantTicker;

/**
 * Created by Pavel Chertalev on 15.03.2018.
 */
public class BequantWebSocketTickerTransaction extends BequantWebSocketBaseTransaction {

    private final Integer id;
    private final BequantTicker params;

    public BequantWebSocketTickerTransaction(@JsonProperty("method") String method, @JsonProperty("id") Integer id,
                                             @JsonProperty("params") BequantTicker params) {
        super(method);
        this.id = id;
        this.params = params;
    }

    public Integer getId() {
        return id;
    }

    public BequantTicker getParams() {
        return params;
    }
}
