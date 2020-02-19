package info.bitrich.xchangestream.krakenFutures.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesEventType;

import java.beans.ConstructorProperties;

/**
 * @author pchertalev
 */
public class KrakenFuturesEventMessage {

    @JsonProperty(value = "event", required = true)
    private final KrakenFuturesEventType event;

    private String message;

    @ConstructorProperties("event")
    public KrakenFuturesEventMessage(KrakenFuturesEventType event) {
        this.event = event;
    }

    public KrakenFuturesEventType getEvent() {
        return event;
    }

    public String getMessage() {
        return message;
    }
}
