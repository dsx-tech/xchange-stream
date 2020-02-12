package info.bitrich.xchangestream.kraken.futures.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import info.bitrich.xchangestream.kraken.futures.enums.KrakenFuturesEventType;

import java.beans.ConstructorProperties;

/**
 * @author pchertalev
 */
public class KrakenFuturesErrorMessage extends KrakenFuturesEventMessage {

    @JsonProperty
    private final String message;

    @ConstructorProperties({"event", "message"})
    public KrakenFuturesErrorMessage(@JsonProperty("event") KrakenFuturesEventType event, @JsonProperty("message") String message) {
        super(event);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
