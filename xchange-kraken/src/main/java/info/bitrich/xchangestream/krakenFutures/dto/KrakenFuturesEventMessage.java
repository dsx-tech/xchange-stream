package info.bitrich.xchangestream.krakenFutures.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesEventType;

import java.beans.ConstructorProperties;

/**
 * @author pchertalev
 */
public class KrakenFuturesEventMessage extends KrakenFuturesMessage {

    @JsonProperty(value = "event", required = true)
    private final KrakenFuturesEventType event;

    @ConstructorProperties("event")
    public KrakenFuturesEventMessage(KrakenFuturesEventType event) {
        this.event = event;
    }

    public KrakenFuturesEventType getEvent() {
        return event;
    }

}
