package info.bitrich.xchangestream.krakenFutures;

import info.bitrich.xchangestream.core.ProductSubscription;
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingMarketDataService;
import io.reactivex.Completable;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.krakenFutures.KrakenFuturesExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.mazi.rescu.SynchronizedValueFactory;

/**
 * @author pchertalev
 */
public class KrakenFuturesStreamingExchange extends KrakenFuturesExchange implements StreamingExchange {

    private static final Logger LOG = LoggerFactory.getLogger(KrakenFuturesStreamingExchange.class);

    public static final String API_URI = "wss://futures.kraken.com/ws/v1";
    public static final String API_DEMO_URI = "wss://demo-futures.kraken.com/ws/v1";

    public static final String PARAM_OVERRIDE_API_URL = "OVERRIDE_API_URL";

    private KrakenFuturesStreamingService streamingService;
    private KrakenFuturesStreamingMarketDataService streamingMarketDataService;

    public KrakenFuturesStreamingExchange() {
    }

    @Override
    protected void initServices() {
        super.initServices();
        String overrideUrl = (String) getExchangeSpecification().getExchangeSpecificParametersItem(PARAM_OVERRIDE_API_URL);
        if (overrideUrl != null) {
            this.streamingService = new KrakenFuturesStreamingService(overrideUrl);
        } else {
            this.streamingService = new KrakenFuturesStreamingService(API_URI);
        }
        streamingMarketDataService = new KrakenFuturesStreamingMarketDataService(streamingService);
    }

    @Override
    public Completable connect(ProductSubscription... args) {
        return streamingService.connect();
    }

    @Override
    public Completable disconnect() {
        return streamingService.disconnect();
    }

    @Override
    public boolean isAlive() {
        return streamingService.isSocketOpen();
    }

    @Override
    public SynchronizedValueFactory<Long> getNonceFactory() {
        return null;
    }

    @Override
    public ExchangeSpecification getDefaultExchangeSpecification() {
        ExchangeSpecification spec = super.getDefaultExchangeSpecification();
        spec.setShouldLoadRemoteMetaData(false);
        return spec;
    }

    @Override
    public StreamingMarketDataService getStreamingMarketDataService() {
        return streamingMarketDataService;
    }

    @Override
    public void useCompressedMessages(boolean compressedMessages) {
        streamingService.useCompressedMessages(compressedMessages);
    }
}
