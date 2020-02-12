package info.bitrich.xchangestream.kraken.futures.enums;

import org.apache.commons.lang3.StringUtils;
import org.knowm.xchange.currency.CurrencyPair;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public enum KrakenFuturesProduct {

    FI("Inverse Futures", true),
    FV("Vanilla Futures", true),
    PI("Perpetual Inverse Futures", false),
    PV("Perpetual Vanilla Futures", false),
    IN("Real Time Index", false),
    RR("Reference Rate", false);

    public final static DateTimeFormatter MATURITY_DATE_FORMAT = DateTimeFormatter.ofPattern("YYMMdd");
    private final static String PRODUCT_DELIMITER_CHAR = "_";

    public final String codeName;
    public final boolean mustHaveMaturityDate;

    KrakenFuturesProduct(String codeName, boolean mustHaveMaturityDate) {
        this.codeName = codeName;
        this.mustHaveMaturityDate = mustHaveMaturityDate;
    }

    /**
     * @param maturityDate 16:00 UTC
     * @return formatted productId string
     */
    public String formatProductId(CurrencyPair pair, LocalDate maturityDate) {
        if (maturityDate != null && mustHaveMaturityDate) {
            return formatProductId(pair) + PRODUCT_DELIMITER_CHAR + MATURITY_DATE_FORMAT.format(maturityDate);
        }
        return formatProductId(pair);
    }

    public String formatProductId(CurrencyPair pair) {
        return this.name() + PRODUCT_DELIMITER_CHAR + StringUtils.upperCase(StringUtils.upperCase(pair.base.toString() + pair.counter.toString()));
    }

}
