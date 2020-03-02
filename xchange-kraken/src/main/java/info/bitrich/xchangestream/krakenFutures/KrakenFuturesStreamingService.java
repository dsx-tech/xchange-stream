package info.bitrich.xchangestream.krakenFutures;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.kraken.KrakenException;
import info.bitrich.xchangestream.krakenFutures.dto.KrakenFuturesChallengeRequest;
import info.bitrich.xchangestream.krakenFutures.dto.KrakenFuturesErrorMessage;
import info.bitrich.xchangestream.krakenFutures.dto.KrakenFuturesProductMessage;
import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesEventType;
import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesFeed;
import info.bitrich.xchangestream.service.netty.JsonNettyStreamingService;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static info.bitrich.xchangestream.kraken.KrakenConstants.KRAKEN_CHANNEL_DELIMITER;
import static info.bitrich.xchangestream.kraken.KrakenConstants.KRAKEN_FUTURES_PRODUCT_ID;
import static info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesEventType.challenge;
import static info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesEventType.getEvent;
import static info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesEventType.subscribe;
import static info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesEventType.unsubscribe;

/**
 * @author pchertalev
 */
public class KrakenFuturesStreamingService extends JsonNettyStreamingService {
    private static final Logger LOG = LoggerFactory.getLogger(KrakenFuturesStreamingService.class);

    private static final String EVENT = "event";

    private ObjectMapper mapper = StreamingObjectMapperHelper.getObjectMapper();

    private final Set<String> subscriptionRequests = ConcurrentHashMap.newKeySet();
    private final Set<KrakenFuturesProductMessage> delayedMessages = ConcurrentHashMap.newKeySet();

    private final String apiKey;
    private final String apiSecret;

    public KrakenFuturesStreamingService(String uri, String apiKey, String apiSecret) {
        super(uri, Integer.MAX_VALUE);
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    private boolean isAuthenticated() {
        return StringUtils.isNotEmpty(apiKey);
    }

    @Override
    public boolean processArrayMassageSeparately() {
        return false;
    }

    private volatile ImmutablePair<String, String> sign;

    @Override
    protected void handleMessage(JsonNode message) {
        try {
            JsonNode event = message.get(EVENT);
            KrakenFuturesEventType krakenEvent;
            if (event != null && (krakenEvent = getEvent(event.textValue())) != null) {
                switch (krakenEvent) {
                    case info:
                        if (isAuthenticated()) {
                            sendObjectMessage(new KrakenFuturesChallengeRequest(challenge, apiKey));
                        }
                        return;
                    case challenge:
                        if (message.hasNonNull("message")) {
                            String messageText = message.get("message").asText();
                            sign = ImmutablePair.of(messageText, signMessage(messageText));
                            delayedMessages.forEach(msg -> {
                                msg.setApiKey(apiKey);
                                msg.setSignedChallenge(sign.getRight());
                                msg.setOriginalChallenge(sign.getLeft());
                                subscriptionRequests.add(msg.getFeed().name());
                                sendObjectMessage(msg);
                            });
                            delayedMessages.clear();
                        }
                    case error:
                        KrakenFuturesErrorMessage errorMessage = mapper.treeToValue(message, KrakenFuturesErrorMessage.class);
                        LOG.error("Kraken Future error: {}", errorMessage.getMessage());
                        return;
                    case subscribed:
                    case unsubscribed:
                        KrakenFuturesProductMessage statusMessage = mapper.treeToValue(message, KrakenFuturesProductMessage.class);
                        if (statusMessage.getProductIds() == null) {
                            processSubscriptionActionConfirmation(krakenEvent, statusMessage, null);
                        } else {
                            statusMessage.getProductIds().forEach(productId -> {
                                processSubscriptionActionConfirmation(krakenEvent, statusMessage, productId);
                            });
                        }
                        return;
                    case subscribed_failed:
                    case unsubscribed_failed:
                        KrakenFuturesProductMessage failedMessage = mapper.treeToValue(message, KrakenFuturesProductMessage.class);
                        failedMessage.getProductIds().forEach(productId -> {
                            String channelName = failedMessage.getFeed() + KRAKEN_CHANNEL_DELIMITER + productId;
                            if (subscriptionRequests.contains(channelName)) {
                                LOG.error("{} request has been rejected for productId {}", krakenEvent.getSourceEvent(), productId);
                                subscriptionRequests.remove(channelName);
                            } else {
                                LOG.error("Unknown {} request has been rejected for productId {}", krakenEvent.getSourceEvent(), productId);
                            }
                        });
                        return;
                    default:
                        // do nothing
                }
            }

            String channelName = getChannel(message);
            if (message.isArray() || StringUtils.isBlank(channelName)) {
                LOG.error("Unknown message: {}", message.toString());
                return;
            }

        } catch (JsonProcessingException e) {
            LOG.error("Error reading message: {}", e.getMessage(), e);
        }

        super.handleMessage(message);
    }

    private void processSubscriptionActionConfirmation(KrakenFuturesEventType krakenEvent, KrakenFuturesProductMessage statusMessage, String productId) {
        String channelName = statusMessage.getFeed() + (productId == null ? "" : KRAKEN_CHANNEL_DELIMITER + productId);
        if (subscriptionRequests.contains(channelName)) {
            LOG.info("{} request has been successfully confirmed for productId {}", krakenEvent.getSourceEvent(), productId);
            subscriptionRequests.remove(channelName);
        } else {
            LOG.warn("Unknown {} request has been confirmed for productId {}", krakenEvent.getSourceEvent(), productId);
        }
    }

    // Signs a message
    public String signMessage(String message) {
        try {
            //Step 1: hash the result of step 1 with SHA256
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(message.getBytes(StandardCharsets.UTF_8));

            // step 2: base64 decode apiPrivateKey
            byte[] secretDecoded = Base64.getDecoder().decode(apiSecret);

            // step 3: use result of step 3 to hash the resultof step 2 with
            // HMAC-SHA512
            Mac hmacsha512 = Mac.getInstance("HmacSHA512");
            hmacsha512.init(new SecretKeySpec(secretDecoded, "HmacSHA512"));
            byte[] hash2 = hmacsha512.doFinal(hash);

            // step 4: base64 encode the result of step 4 and return
            return Base64.getEncoder().encodeToString(hash2);
        } catch (Exception e) {
            throw new KrakenException("Can't create signature: " + e.getMessage(), e);
        }
    }

    @Override
    protected String getChannelNameFromMessage(JsonNode message) {
        StringBuilder channelNameBuilder = new StringBuilder();
        KrakenFuturesFeed feed = KrakenFuturesFeed.getFeed(message);
        if (feed != null) {
            channelNameBuilder.append(feed.sourceFeed);
            if (message.has(KRAKEN_FUTURES_PRODUCT_ID)) {
                JsonNode productId = message.get(KRAKEN_FUTURES_PRODUCT_ID);
                channelNameBuilder
                        .append(KRAKEN_CHANNEL_DELIMITER)
                        .append(productId.asText());
            }
        }
        String channelName = channelNameBuilder.toString();
        if (StringUtils.isBlank(channelName)) {
            LOG.debug("ChannelName not defined");
            return null;
        }
        LOG.debug("ChannelName {}", channelName);
        return channelName;
    }

    @Override
    public String getSubscribeMessage(String channelName, Object... args) throws IOException {
        String[] channelData = channelName.split(KRAKEN_CHANNEL_DELIMITER);
        KrakenFuturesFeed feed = KrakenFuturesFeed.valueOf(channelData[0]);
        if (feed.auth) {
            if (isAuthenticated()) {
                if (sign == null) {
                    delayedMessages.add(new KrakenFuturesProductMessage(subscribe, feed, null));
                    return null;
                }
                return getSubscriptionMessage(channelName, subscribe, true);
            }
            throw new KrakenException(feed + " feed is required authorization, apiKey/secretKey must be specified");
        } else {
            return getSubscriptionMessage(channelName, subscribe, false);
        }
    }

    @Override
    public String getUnsubscribeMessage(String channelName) throws IOException {
        return getSubscriptionMessage(channelName, unsubscribe, false);
    }

    private String getSubscriptionMessage(String channelName, KrakenFuturesEventType event, boolean auth) throws JsonProcessingException {
        String[] channelData = channelName.split(KRAKEN_CHANNEL_DELIMITER);
        KrakenFuturesFeed feed = KrakenFuturesFeed.valueOf(channelData[0]);
        String productId = channelData.length > 1 ? channelData[1] : null;

        subscriptionRequests.add(channelName);
        List<String> productIds = productId == null ? null : Collections.singletonList(productId);

        KrakenFuturesProductMessage subscriptionMessage;
        if (auth) {
            subscriptionMessage = new KrakenFuturesProductMessage(event, feed, productIds, apiKey, sign.getLeft(), sign.getRight());
        } else {
            subscriptionMessage = new KrakenFuturesProductMessage(event, feed, productIds);
        }
        return objectMapper.writeValueAsString(subscriptionMessage);
    }

}
