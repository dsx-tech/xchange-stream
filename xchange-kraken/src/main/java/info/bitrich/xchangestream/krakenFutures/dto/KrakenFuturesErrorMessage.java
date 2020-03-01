package info.bitrich.xchangestream.krakenFutures.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesEventType;

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

    public String getMessage() {
        return message;
    }
}
