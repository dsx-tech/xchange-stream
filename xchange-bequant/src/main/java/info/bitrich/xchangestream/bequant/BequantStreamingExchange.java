package info.bitrich.xchangestream.bequant;

import info.bitrich.xchangestream.core.ProductSubscription;
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingMarketDataService;
import io.reactivex.Completable;
import io.reactivex.Observable;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.hitbtc.v2.HitbtcExchange;

/**
 * Created by Pavel Chertalev on 15.03.2018.
 */
public class BequantStreamingExchange extends HitbtcExchange implements StreamingExchange {
    private static final String API_URI = "wss://api.bequant.io/api/2/ws";

    private final BequantStreamingService streamingService;
    private BequantStreamingMarketDataService streamingMarketDataService;

    public BequantStreamingExchange() {
        this.streamingService = new BequantStreamingService(API_URI);
    }

    @Override
    protected void initServices() {
        super.initServices();
        streamingMarketDataService = new BequantStreamingMarketDataService(streamingService);
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
    public Observable<Throwable> reconnectFailure() {
        return streamingService.subscribeReconnectFailure();
    }

    @Override
    public Observable<Object> connectionSuccess() {
        return streamingService.subscribeConnectionSuccess();
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
    public void useCompressedMessages(boolean compressedMessages) { streamingService.useCompressedMessages(compressedMessages); }
}
