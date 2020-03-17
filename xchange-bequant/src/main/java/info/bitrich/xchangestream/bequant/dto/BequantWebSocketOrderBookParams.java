package info.bitrich.xchangestream.bequant.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.knowm.xchange.bequant.v2.dto.BequantOrderLimit;

/**
 * Created by Pavel Chertalev on 15.03.2018.
 */
public class BequantWebSocketOrderBookParams extends BequantWebSocketBaseParams {

    private final BequantOrderLimit[] ask;
    private final BequantOrderLimit[] bid;
    private final long sequence;

    public BequantWebSocketOrderBookParams(@JsonProperty("property") String symbol, @JsonProperty("sequence") long sequence,
                                           @JsonProperty("ask") BequantOrderLimit[] ask, @JsonProperty("bid") BequantOrderLimit[] bid) {
        super(symbol);
        this.ask = ask;
        this.bid = bid;
        this.sequence = sequence;
    }

    public BequantOrderLimit[] getAsk() {
        return ask;
    }

    public BequantOrderLimit[] getBid() {
        return bid;
    }

    public long getSequence() {
        return sequence;
    }

}
