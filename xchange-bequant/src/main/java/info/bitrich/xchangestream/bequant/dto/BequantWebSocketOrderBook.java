package info.bitrich.xchangestream.bequant.dto;

import org.knowm.xchange.bequant.v2.dto.BequantOrderBook;
import org.knowm.xchange.bequant.v2.dto.BequantOrderLimit;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Collections.reverseOrder;

/**
 * Created by Pavel Chertalev on 15.03.2018.
 */
public class BequantWebSocketOrderBook {
    private Map<BigDecimal, BequantOrderLimit> asks;
    private Map<BigDecimal, BequantOrderLimit> bids;
    private long sequence = 0;

    public BequantWebSocketOrderBook(BequantWebSocketOrderBookTransaction orderbookTransaction) {
        createFromLevels(orderbookTransaction);
    }

    private void createFromLevels(BequantWebSocketOrderBookTransaction orderbookTransaction) {
        this.asks = new TreeMap<>(BigDecimal::compareTo);
        this.bids = new TreeMap<>(reverseOrder(BigDecimal::compareTo));

        for (BequantOrderLimit orderBookItem : orderbookTransaction.getParams().getAsk()) {
            if (orderBookItem.getSize().signum() != 0) {
                asks.put(orderBookItem.getPrice(), orderBookItem);
            }
        }

        for (BequantOrderLimit orderBookItem : orderbookTransaction.getParams().getBid()) {
            if (orderBookItem.getSize().signum() != 0) {
                bids.put(orderBookItem.getPrice(), orderBookItem);
            }
        }

        sequence = orderbookTransaction.getParams().getSequence();
    }

    public BequantOrderBook toBequantOrderBook() {
        BequantOrderLimit[] askLimits = asks.entrySet().stream()
                .map(Map.Entry::getValue)
                .toArray(BequantOrderLimit[]::new);

        BequantOrderLimit[] bidLimits = bids.entrySet().stream()
                .map(Map.Entry::getValue)
                .toArray(BequantOrderLimit[]::new);

        return new BequantOrderBook(askLimits, bidLimits);
    }

    public void updateOrderBook(BequantWebSocketOrderBookTransaction orderBookTransaction) {
        if (orderBookTransaction.getParams().getSequence() <= sequence) {
            return;
        }
        updateOrderBookItems(orderBookTransaction.getParams().getAsk(), asks);
        updateOrderBookItems(orderBookTransaction.getParams().getBid(), bids);
        sequence = orderBookTransaction.getParams().getSequence();
    }

    private void updateOrderBookItems(BequantOrderLimit[] itemsToUpdate, Map<BigDecimal, BequantOrderLimit> localItems) {
        for (BequantOrderLimit itemToUpdate : itemsToUpdate) {
            localItems.remove(itemToUpdate.getPrice());
            if (itemToUpdate.getSize().signum() != 0) {
                localItems.put(itemToUpdate.getPrice(), itemToUpdate);
            }
        }
    }
}
