package info.bitrich.xchangestream.krakenFutures;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.krakenFutures.dto.KrakenFuturesErrorMessage;
import info.bitrich.xchangestream.krakenFutures.dto.KrakenFuturesProductMessage;
import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesEventType;
import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesFeed;
import info.bitrich.xchangestream.service.netty.JsonNettyStreamingService;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static info.bitrich.xchangestream.kraken.KrakenConstants.KRAKEN_CHANNEL_DELIMITER;
import static info.bitrich.xchangestream.kraken.KrakenConstants.KRAKEN_FUTURES_PRODUCT_ID;
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

    private final Set<String> subscriptionRequestMap = ConcurrentHashMap.newKeySet();

    public KrakenFuturesStreamingService(String uri) {
        super(uri, Integer.MAX_VALUE);
    }


    @Override
    public boolean processArrayMassageSeparately() {
        return false;
    }

    @Override
    protected void handleMessage(JsonNode message) {
        try {
            JsonNode event = message.get(EVENT);
            KrakenFuturesEventType krakenEvent;
            if (event != null && (krakenEvent = getEvent(event.textValue())) != null) {
                switch (krakenEvent) {
                    case error:
                        KrakenFuturesErrorMessage errorMessage = mapper.treeToValue(message, KrakenFuturesErrorMessage.class);
                        LOG.error("Kraken Future error: {}", errorMessage.getMessage());
                        return;
                    case subscribed:
                    case unsubscribed:
                        KrakenFuturesProductMessage statusMessage = mapper.treeToValue(message, KrakenFuturesProductMessage.class);
                        statusMessage.getProductIds().forEach(productId -> {
                            String channelName = statusMessage.getFeed() + KRAKEN_CHANNEL_DELIMITER + productId;
                            if (subscriptionRequestMap.contains(channelName)) {
                                LOG.info("{} request has been successfully confirmed for productId {}", krakenEvent.getSourceEvent(), productId);
                                subscriptionRequestMap.remove(channelName);
                            } else {
                                LOG.warn("Unknown {} request has been confirmed for productId {}", krakenEvent.getSourceEvent(), productId);
                            }
                        });
                        return;
                    case subscribed_failed:
                    case unsubscribed_failed:
                        KrakenFuturesProductMessage failedMessage = mapper.treeToValue(message, KrakenFuturesProductMessage.class);
                        failedMessage.getProductIds().forEach(productId -> {
                            String channelName = failedMessage.getFeed() + KRAKEN_CHANNEL_DELIMITER + productId;
                            if (subscriptionRequestMap.contains(channelName)) {
                                LOG.error("{} request has been rejected for productId {}", krakenEvent.getSourceEvent(), productId);
                                subscriptionRequestMap.remove(channelName);
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
        return getSubscriptionMessage(channelName, subscribe);
    }

    @Override
    public String getUnsubscribeMessage(String channelName) throws IOException {
        return getSubscriptionMessage(channelName, unsubscribe);
    }

    private String getSubscriptionMessage(String channelName, KrakenFuturesEventType event) throws JsonProcessingException {
        String[] channelData = channelName.split(KRAKEN_CHANNEL_DELIMITER);
        KrakenFuturesFeed feed = KrakenFuturesFeed.valueOf(channelData[0]);
        String productId = channelData[1];
        subscriptionRequestMap.add(channelName);
        KrakenFuturesProductMessage subscriptionMessage = new KrakenFuturesProductMessage(event, feed, Collections.singletonList(productId));
        return objectMapper.writeValueAsString(subscriptionMessage);
    }

}
