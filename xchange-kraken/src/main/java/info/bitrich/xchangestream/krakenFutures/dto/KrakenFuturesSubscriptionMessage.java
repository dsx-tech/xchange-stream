package info.bitrich.xchangestream.krakenFutures.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesEventType;
import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesFeed;

import java.beans.ConstructorProperties;
import java.util.List;

/**
 * @author pchertalev
 */
public class KrakenFuturesSubscriptionMessage extends KrakenFuturesEventMessage {

    @JsonProperty
    private final KrakenFuturesFeed feed;

    @JsonProperty(value = "product_ids")
    private final List<String> productIds;

    @JsonProperty("api_key")
    private String apiKey;

    @JsonProperty("original_challenge")
    private String originalChallenge;

    @JsonProperty("signed_challenge")
    private String signedChallenge;

    public KrakenFuturesSubscriptionMessage(KrakenFuturesEventType event, KrakenFuturesFeed feed, List<String> productIds) {
        this(event, feed, productIds, null, null, null);
    }

    @ConstructorProperties({"event", "feed", "product_ids", "api_key", "original_challenge", "signed_challenge"})
    public KrakenFuturesSubscriptionMessage(KrakenFuturesEventType event, KrakenFuturesFeed feed, List<String> productIds, String apiKey, String originalChallenge, String signedChallenge) {
        super(event);
        this.feed = feed;
        this.productIds = productIds;
        this.apiKey = apiKey;
        this.originalChallenge = originalChallenge;
        this.signedChallenge = signedChallenge;
    }

    public KrakenFuturesFeed getFeed() {
        return feed;
    }

    public List<String> getProductIds() {
        return productIds;
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

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setOriginalChallenge(String originalChallenge) {
        this.originalChallenge = originalChallenge;
    }

    public void setSignedChallenge(String signedChallenge) {
        this.signedChallenge = signedChallenge;
    }
}
