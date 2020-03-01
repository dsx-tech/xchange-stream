package info.bitrich.xchangestream.krakenFutures;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.core.StreamingAccountService;
import info.bitrich.xchangestream.krakenFutures.dto.account.KrakenFuturesAccountBalances;
import info.bitrich.xchangestream.krakenFutures.dto.account.KrakenFuturesAccountLog;
import info.bitrich.xchangestream.krakenFutures.dto.account.KrakenFuturesAccountLogSnapshot;
import info.bitrich.xchangestream.krakenFutures.dto.account.KrakenFuturesAccountLogUpdate;
import info.bitrich.xchangestream.krakenFutures.dto.account.KrakenFuturesMarginAccount;
import info.bitrich.xchangestream.krakenFutures.enums.KrakenFuturesFeed;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import io.reactivex.Observable;

import java.util.Comparator;

public class KrakenFuturesStreamingAccountService implements StreamingAccountService {

    private KrakenFuturesStreamingService streamingService;
    private ObjectMapper mapper = StreamingObjectMapperHelper.getObjectMapper();
    private volatile long lastAccountLogId = 0;
    private volatile long lastAccountBalanceSeq = 0;

    public KrakenFuturesStreamingAccountService(KrakenFuturesStreamingService streamingService) {
        this.streamingService = streamingService;
    }

    public Observable<KrakenFuturesAccountLog> getAccountLog() {
        return streamingService.subscribeChannel(KrakenFuturesFeed.account_log.name())
                .filter(jsonMessage -> KrakenFuturesFeed.account_log.equalsJsonNode(jsonMessage) || KrakenFuturesFeed.account_log_snapshot.equalsJsonNode(jsonMessage))
                .flatMap(jsonMessage -> {
                    if (KrakenFuturesFeed.account_log_snapshot.equalsJsonNode(jsonMessage)) {
                        KrakenFuturesAccountLogSnapshot value = mapper.treeToValue(jsonMessage, KrakenFuturesAccountLogSnapshot.class);
                        return Observable.fromIterable(value.getLogs())
                                .sorted(Comparator.comparingLong(KrakenFuturesAccountLog::getId));
                    }
                    KrakenFuturesAccountLogUpdate value = mapper.treeToValue(jsonMessage, KrakenFuturesAccountLogUpdate.class);
                    return Observable.just(value.getNewEntry());
                })
                .filter(log -> log.getId() != null && log.getId() > lastAccountLogId)
                .doOnNext(log -> lastAccountLogId = log.getId());
    }


    public Observable<KrakenFuturesMarginAccount> getAccountBalances() {
        return streamingService.subscribeChannel(KrakenFuturesFeed.account_balances_and_margins.name())
                .filter(KrakenFuturesFeed.account_balances_and_margins::equalsJsonNode)
                .map(jsonMessage -> mapper.treeToValue(jsonMessage, KrakenFuturesAccountBalances.class))
                .filter(balance -> balance.getSeq() != null && balance.getSeq() > lastAccountBalanceSeq)
                .doOnNext(balance -> lastAccountBalanceSeq = balance.getSeq())
                .flatMap(balance -> Observable.fromIterable(balance.getMarginAccounts()));
    }
}
