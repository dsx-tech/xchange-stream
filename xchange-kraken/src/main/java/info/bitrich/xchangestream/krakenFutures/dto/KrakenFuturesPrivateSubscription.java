package info.bitrich.xchangestream.krakenFutures.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesEventType;
import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesFeed;

public class KrakenFuturesPrivateSubscription extends KrakenFuturesEventFeedMessage {

    @JsonProperty("api_key")
    private final String apiKey;

    @JsonProperty("original_challenge")
    private final String originalChallenge;

    @JsonProperty("signed_challenge")
    private final String signedChallenge;

    public KrakenFuturesPrivateSubscription(KrakenFuturesEventType event, KrakenFuturesFeed feed, String apiKey, String originalChallenge, String signedChallenge) {
        super(event, feed);
        this.apiKey = apiKey;
        this.originalChallenge = originalChallenge;
        this.signedChallenge = signedChallenge;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getOriginalChallenge() {
        return originalChallenge;
    }

    public String getSignedChallenge() {
        return signedChallenge;
    }
}
