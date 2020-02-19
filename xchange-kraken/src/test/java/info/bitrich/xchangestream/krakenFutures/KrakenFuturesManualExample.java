package info.bitrich.xchangestream.krakenFutures;

import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import io.reactivex.disposables.Disposable;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.krakenFutures.dto.enums.KrakenFuturesProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

public class KrakenFuturesManualExample {

    private static final Logger LOG = LoggerFactory.getLogger(KrakenFuturesManualExample.class);

    public static void main(String[] args) throws InterruptedException, IOException {

        ExchangeSpecification exchangeSpecification = new ExchangeSpecification(KrakenFuturesStreamingExchange.class);
        exchangeSpecification.setExchangeSpecificParametersItem(
                KrakenFuturesStreamingExchange.PARAM_OVERRIDE_API_URL,
                KrakenFuturesStreamingExchange.API_DEMO_URI);

        StreamingExchange krakenExchange = StreamingExchangeFactory.INSTANCE.createExchange(exchangeSpecification);
        krakenExchange.connect().blockingAwait();

        Disposable bookPIDis = krakenExchange.getStreamingMarketDataService().getOrderBook(CurrencyPair.XBT_USD, KrakenFuturesProduct.PI).subscribe(s -> {
            LOG.info("Received PI order book {}({},{}) ask[0] = {} bid[0] = {}", CurrencyPair.XBT_USD, s.getAsks().size(), s.getBids().size(), s.getAsks().get(0), s.getBids().get(0));
        }, throwable -> {
            LOG.error("Order book FAILED {}", throwable.getMessage(), throwable);
        });

        Disposable bookFIDis = krakenExchange.getStreamingMarketDataService().getOrderBook(CurrencyPair.XBT_USD, KrakenFuturesProduct.FI, LocalDate.of(2021, 10, 28)).subscribe(s -> {
            LOG.info("Received FI order book {}({},{}) ask[0] = {} bid[0] = {}", CurrencyPair.XBT_USD, s.getAsks().size(), s.getBids().size(), s.getAsks().get(0), s.getBids().get(0));
        }, throwable -> {
            LOG.error("Order book FAILED {}", throwable.getMessage(), throwable);
        });
        Disposable tradeDis = krakenExchange.getStreamingMarketDataService().getTrades(CurrencyPair.XBT_USD, KrakenFuturesProduct.PI).subscribe(s -> {
            LOG.info("Received trade {} = {}", CurrencyPair.XBT_USD, s);
        }, throwable -> {
            LOG.error("Order book FAILED {}", throwable.getMessage(), throwable);
        });
        Disposable tickerDis = krakenExchange.getStreamingMarketDataService().getTicker(CurrencyPair.XBT_USD, KrakenFuturesProduct.PI).subscribe(s -> {
            LOG.info("Received ticker {} = {}", CurrencyPair.XBT_USD, s);
        }, throwable -> {
            LOG.error("Order book FAILED {}", throwable.getMessage(), throwable);
        });

        TimeUnit.SECONDS.sleep(10);

        bookPIDis.dispose();
        bookFIDis.dispose();
        tradeDis.dispose();
        tickerDis.dispose();

        krakenExchange.disconnect().subscribe(() -> LOG.info("Disconnected"));
    }
}
