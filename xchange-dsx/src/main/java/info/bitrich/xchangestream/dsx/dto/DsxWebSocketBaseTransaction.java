package info.bitrich.xchangestream.dsx.dto;

/**
 * Created by Pavel Chertalev on 15.03.2018.
 */
public class DsxWebSocketBaseTransaction {

    protected final String method;

    public DsxWebSocketBaseTransaction(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

}
