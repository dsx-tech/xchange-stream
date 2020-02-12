package info.bitrich.xchangestream.kraken;

import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import info.bitrich.xchangestream.kraken.futures.KrakenFuturesStreamingExchange;
import info.bitrich.xchangestream.kraken.futures.enums.KrakenFuturesProduct;
import io.reactivex.disposables.Disposable;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class KrakenFuturesManualExample {

    private static final Logger LOG = LoggerFactory.getLogger(KrakenFuturesManualExample.class);

    public static void main(String[] args) throws InterruptedException {

        ExchangeSpecification exchangeSpecification = new ExchangeSpecification(KrakenFuturesStreamingExchange.class);

        StreamingExchange krakenExchange = StreamingExchangeFactory.INSTANCE.createExchange(exchangeSpecification);
        krakenExchange.connect().blockingAwait();

        CurrencyPair xbtUsd = CurrencyPair.XBT_USD;
        Disposable btcEurOrderBookDis = krakenExchange.getStreamingMarketDataService().getOrderBook(xbtUsd, KrakenFuturesProduct.PI).subscribe(s -> {
            LOG.info("Received order book {}({},{}) ask[0] = {} bid[0] = {}", CurrencyPair.XBT_USD, s.getAsks().size(), s.getBids().size(), s.getAsks().get(0), s.getBids().get(0));
        }, throwable -> {
            LOG.error("Order book FAILED {}", throwable.getMessage(), throwable);
        });
        TimeUnit.SECONDS.sleep(50000);

        btcEurOrderBookDis.dispose();

        krakenExchange.disconnect().subscribe(() -> LOG.info("Disconnected"));
    }
}
