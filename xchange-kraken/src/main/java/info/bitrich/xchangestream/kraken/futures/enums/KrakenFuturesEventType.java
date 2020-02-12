package info.bitrich.xchangestream.kraken.futures.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public enum KrakenFuturesEventType {
    subscribe,
    subscribed(subscribe),
    subscribed_failed(subscribe),

    unsubscribe,
    unsubscribed(unsubscribe),
    unsubscribed_failed(unsubscribe),

    error;

    private final KrakenFuturesEventType sourceEvent;

    KrakenFuturesEventType() {
        this(null);
    }

    KrakenFuturesEventType(KrakenFuturesEventType sourceEvent) {
        this.sourceEvent = sourceEvent;
    }

    public static KrakenFuturesEventType getEvent(String event) {
        return Arrays.stream(KrakenFuturesEventType.values())
                .filter(e -> StringUtils.equalsIgnoreCase(event, e.name()))
                .findFirst()
                .orElse(null);
    }

    public KrakenFuturesEventType getSourceEvent() {
        return sourceEvent;
    }
}
