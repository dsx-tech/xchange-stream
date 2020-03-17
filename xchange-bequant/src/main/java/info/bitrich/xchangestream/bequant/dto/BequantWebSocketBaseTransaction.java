package info.bitrich.xchangestream.bequant.dto;

/**
 * Created by Pavel Chertalev on 15.03.2018.
 */
public class BequantWebSocketBaseTransaction {

    protected final String method;

    public BequantWebSocketBaseTransaction(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

}
