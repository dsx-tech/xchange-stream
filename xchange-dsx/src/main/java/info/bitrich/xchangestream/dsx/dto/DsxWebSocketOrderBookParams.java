package info.bitrich.xchangestream.dsx.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.knowm.xchange.dsx.v2.dto.DsxOrderLimit;

/**
 * Created by Pavel Chertalev on 15.03.2018.
 */
public class DsxWebSocketOrderBookParams extends DsxWebSocketBaseParams {

    private final DsxOrderLimit[] ask;
    private final DsxOrderLimit[] bid;
    private final long sequence;

    public DsxWebSocketOrderBookParams(@JsonProperty("property") String symbol, @JsonProperty("sequence") long sequence,
                                       @JsonProperty("ask") DsxOrderLimit[] ask, @JsonProperty("bid") DsxOrderLimit[] bid) {
        super(symbol);
        this.ask = ask;
        this.bid = bid;
        this.sequence = sequence;
    }

    public DsxOrderLimit[] getAsk() {
        return ask;
    }

    public DsxOrderLimit[] getBid() {
        return bid;
    }

    public long getSequence() {
        return sequence;
    }

}
