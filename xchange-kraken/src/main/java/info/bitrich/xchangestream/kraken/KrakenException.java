package info.bitrich.xchangestream.kraken;

/**
 * @author pchertalev
 */
public class KrakenException extends RuntimeException {

    public KrakenException(String message, Throwable cause) {
        super(message, cause);
    }

    public KrakenException(String message) {
        super(message);
    }
}
