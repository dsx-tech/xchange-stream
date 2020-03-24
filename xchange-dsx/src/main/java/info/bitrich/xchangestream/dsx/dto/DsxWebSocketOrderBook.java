package info.bitrich.xchangestream.dsx.dto;

import org.knowm.xchange.dsx.v2.dto.DsxOrderBook;
import org.knowm.xchange.dsx.v2.dto.DsxOrderLimit;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Collections.reverseOrder;

/**
 * Created by Pavel Chertalev on 15.03.2018.
 */
public class DsxWebSocketOrderBook {
    private Map<BigDecimal, DsxOrderLimit> asks;
    private Map<BigDecimal, DsxOrderLimit> bids;
    private long sequence = 0;

    public DsxWebSocketOrderBook(DsxWebSocketOrderBookTransaction orderbookTransaction) {
        createFromLevels(orderbookTransaction);
    }

    private void createFromLevels(DsxWebSocketOrderBookTransaction orderbookTransaction) {
        this.asks = new TreeMap<>(BigDecimal::compareTo);
        this.bids = new TreeMap<>(reverseOrder(BigDecimal::compareTo));

        for (DsxOrderLimit orderBookItem : orderbookTransaction.getParams().getAsk()) {
            if (orderBookItem.getSize().signum() != 0) {
                asks.put(orderBookItem.getPrice(), orderBookItem);
            }
        }

        for (DsxOrderLimit orderBookItem : orderbookTransaction.getParams().getBid()) {
            if (orderBookItem.getSize().signum() != 0) {
                bids.put(orderBookItem.getPrice(), orderBookItem);
            }
        }

        sequence = orderbookTransaction.getParams().getSequence();
    }

    public DsxOrderBook toDsxOrderBook() {
        DsxOrderLimit[] askLimits = asks.entrySet().stream()
                .map(Map.Entry::getValue)
                .toArray(DsxOrderLimit[]::new);

        DsxOrderLimit[] bidLimits = bids.entrySet().stream()
                .map(Map.Entry::getValue)
                .toArray(DsxOrderLimit[]::new);

        return new DsxOrderBook(askLimits, bidLimits);
    }

    public void updateOrderBook(DsxWebSocketOrderBookTransaction orderBookTransaction) {
        if (orderBookTransaction.getParams().getSequence() <= sequence) {
            return;
        }
        updateOrderBookItems(orderBookTransaction.getParams().getAsk(), asks);
        updateOrderBookItems(orderBookTransaction.getParams().getBid(), bids);
        sequence = orderBookTransaction.getParams().getSequence();
    }

    private void updateOrderBookItems(DsxOrderLimit[] itemsToUpdate, Map<BigDecimal, DsxOrderLimit> localItems) {
        for (DsxOrderLimit itemToUpdate : itemsToUpdate) {
            localItems.remove(itemToUpdate.getPrice());
            if (itemToUpdate.getSize().signum() != 0) {
                localItems.put(itemToUpdate.getPrice(), itemToUpdate);
            }
        }
    }
}
