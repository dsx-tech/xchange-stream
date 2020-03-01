package info.bitrich.xchangestream.krakenFutures.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesEventType;

import java.beans.ConstructorProperties;

public class KrakenFuturesChallengeRequest extends KrakenFuturesEventMessage {

    @JsonProperty("api_key")
    private final String apiKey;

    @ConstructorProperties({"event", "api_key"})
    public KrakenFuturesChallengeRequest(KrakenFuturesEventType event, String apiKey) {
        super(event);

        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }
}
