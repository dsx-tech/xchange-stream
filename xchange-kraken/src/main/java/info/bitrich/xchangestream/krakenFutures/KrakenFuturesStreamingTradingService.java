package info.bitrich.xchangestream.krakenFutures;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.core.StreamingTradeService;
import info.bitrich.xchangestream.krakenFutures.dto.trading.KrakenFuturesFill;
import info.bitrich.xchangestream.krakenFutures.dto.trading.KrakenFuturesFillSnapshot;
import info.bitrich.xchangestream.krakenFutures.dto.trading.KrakenFuturesFillUpdate;
import info.bitrich.xchangestream.krakenFutures.dto.trading.KrakenFuturesOrder;
import info.bitrich.xchangestream.krakenFutures.dto.trading.KrakenFuturesOrdersSnapshot;
import info.bitrich.xchangestream.krakenFutures.dto.trading.KrakenFuturesOrdersUpdate;
import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesFeed;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import io.reactivex.Observable;

import java.util.Comparator;

public class KrakenFuturesStreamingTradingService implements StreamingTradeService {

    private KrakenFuturesStreamingService streamingService;
    private ObjectMapper mapper = StreamingObjectMapperHelper.getObjectMapper();

    public KrakenFuturesStreamingTradingService(KrakenFuturesStreamingService streamingService) {
        this.streamingService = streamingService;
    }

    public Observable<KrakenFuturesFill> getFills() {
        return streamingService.subscribeChannel(KrakenFuturesFeed.fills.name())
                .filter(jsonMessage -> KrakenFuturesFeed.fills.equalsJsonNode(jsonMessage) || KrakenFuturesFeed.fills_snapshot.equalsJsonNode(jsonMessage))
                .flatMap(jsonMessage -> {
                    if (KrakenFuturesFeed.fills_snapshot.equalsJsonNode(jsonMessage)) {
                        KrakenFuturesFillSnapshot value = mapper.treeToValue(jsonMessage, KrakenFuturesFillSnapshot.class);
                        return Observable.fromIterable(value.getFills());
                    }
                    KrakenFuturesFillUpdate value = mapper.treeToValue(jsonMessage, KrakenFuturesFillUpdate.class);
                    return Observable.fromIterable(value.getFills());
                });
    }

    public Observable<KrakenFuturesOrder> getOpenOrders() {
        return streamingService.subscribeChannel(KrakenFuturesFeed.open_orders.name())
                .filter(jsonMessage -> KrakenFuturesFeed.open_orders.equalsJsonNode(jsonMessage) || KrakenFuturesFeed.open_orders_snapshot.equalsJsonNode(jsonMessage))
                .flatMap(jsonMessage -> {
                    if (KrakenFuturesFeed.open_orders_snapshot.equalsJsonNode(jsonMessage)) {
                        KrakenFuturesOrdersSnapshot value = mapper.treeToValue(jsonMessage, KrakenFuturesOrdersSnapshot.class);
                        return Observable.fromIterable(value.getOrders())
                                .sorted(Comparator.comparingLong(KrakenFuturesOrder::getTime));
                    }
                    KrakenFuturesOrdersUpdate value = mapper.treeToValue(jsonMessage, KrakenFuturesOrdersUpdate.class);
                    return Observable.just(value.getOrder());
                });
    }
}
