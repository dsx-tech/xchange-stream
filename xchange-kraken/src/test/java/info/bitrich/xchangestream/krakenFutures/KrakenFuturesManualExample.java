package info.bitrich.xchangestream.krakenFutures;

import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import info.bitrich.xchangestream.core.StreamingMarketDataService;
import io.reactivex.disposables.Disposable;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.krakenFutures.dto.enums.KrakenFuturesProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class KrakenFuturesManualExample {

    private static final Logger LOG = LoggerFactory.getLogger(KrakenFuturesManualExample.class);

    public static void main(String[] args) throws InterruptedException {

        ExchangeSpecification exchangeSpecification = new ExchangeSpecification(KrakenFuturesStreamingExchange.class);
        exchangeSpecification.setExchangeSpecificParametersItem(
                KrakenFuturesStreamingExchange.PARAM_OVERRIDE_API_URL,
                KrakenFuturesStreamingExchange.API_DEMO_URI);

        exchangeSpecification.setApiKey("SEpLe3MiVCPWQqVIXObDzhCKQIh3IQcfiM/OeUr+9JS5hykZQw1jl6UF");
        exchangeSpecification.setSecretKey("cosfOtLxeSHGrAFIo1zD3lCI1Qj4lfia0ezIlHzbT+nd2vNKP4JE2l6t00P/SN+nWnAwEtAbvDS+siPgHTZhXwfy");

        StreamingExchange krakenExchange = StreamingExchangeFactory.INSTANCE.createExchange(exchangeSpecification);
        krakenExchange.connect().blockingAwait();

        StreamingMarketDataService streamingMarketDataService = krakenExchange.getStreamingMarketDataService();
        KrakenFuturesStreamingAccountService accountService = (KrakenFuturesStreamingAccountService) krakenExchange.getStreamingAccountService();
        KrakenFuturesStreamingTradingService tradingService = (KrakenFuturesStreamingTradingService) krakenExchange.getStreamingTradeService();

        Disposable bookPIDis = streamingMarketDataService.getOrderBook(CurrencyPair.XBT_USD, KrakenFuturesProduct.PI).subscribe(s -> {
            LOG.info("Order book {}({},{}) ask[0] = {} bid[0] = {}", CurrencyPair.XBT_USD, s.getAsks().size(), s.getBids().size(), s.getAsks().get(0), s.getBids().get(0));
        }, throwable -> {
            LOG.error("Order book FAILED {}", throwable.getMessage(), throwable);
        });

        Disposable accountLogDis = accountService.getAccountLog().subscribe(accountLog -> {
            LOG.info("Account_log: {}", accountLog.toString());
        }, e -> {
            LOG.error(e.getMessage(), e);
        });

        Disposable accountBalanceDis = accountService.getAccountBalances().subscribe(balance -> {
            LOG.info("Account_balance: {}", balance);
        }, e -> {
            LOG.error(e.getMessage(), e);
        });

        Disposable fillsDis = tradingService.getFills().subscribe(fill -> {
            LOG.info("fill: {}", fill);
        }, e -> {
            LOG.error(e.getMessage(), e);
        });

        Disposable openOrdersDis = tradingService.getOpenOrders().subscribe(order -> {
            LOG.info("openOrder: {}", order);
        }, e -> {
            LOG.error(e.getMessage(), e);
        });

        TimeUnit.SECONDS.sleep(10);

        accountLogDis.dispose();
        accountBalanceDis.dispose();
        fillsDis.dispose();
        bookPIDis.dispose();
        openOrdersDis.dispose();

        TimeUnit.SECONDS.sleep(2);

        krakenExchange.disconnect().subscribe(() -> LOG.info("Disconnected"));
    }
}
