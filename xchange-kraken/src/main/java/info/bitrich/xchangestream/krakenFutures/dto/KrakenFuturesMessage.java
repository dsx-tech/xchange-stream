package info.bitrich.xchangestream.krakenFutures.dto;

import org.apache.commons.lang3.builder.RecursiveToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

public class KrakenFuturesMessage {

    private static final ToStringStyle TO_STRING_STYLE = new RecursiveToStringStyle() {
        {
            this.setUseClassName(false);
            this.setUseIdentityHashCode(false);
        }

        @Override
        protected boolean accept(Class<?> clazz) {
            if (clazz == BigDecimal.class || clazz == Date.class) return false;
            return super.accept(clazz);
        }
    };

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, TO_STRING_STYLE);
    }
}
