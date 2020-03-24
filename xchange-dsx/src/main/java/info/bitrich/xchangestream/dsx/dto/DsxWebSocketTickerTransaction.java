package info.bitrich.xchangestream.dsx.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.knowm.xchange.dsx.v2.dto.DsxTicker;

/**
 * Created by Pavel Chertalev on 15.03.2018.
 */
public class DsxWebSocketTickerTransaction extends DsxWebSocketBaseTransaction {

    private final Integer id;
    private final DsxTicker params;

    public DsxWebSocketTickerTransaction(@JsonProperty("method") String method, @JsonProperty("id") Integer id,
                                         @JsonProperty("params") DsxTicker params) {
        super(method);
        this.id = id;
        this.params = params;
    }

    public Integer getId() {
        return id;
    }

    public DsxTicker getParams() {
        return params;
    }
}
