package it.aeg2000srl.aeron.core;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

/**
 * Created by tiziano.michelessi on 09/10/2015.
 */
public class PercentDiscount implements IDiscount {
    double percent;

    public PercentDiscount(double percent) {
        this.percent = percent;
    }

    @Override
    public double calculatePrice(double price) {
        return new BigDecimal(price - (price/100.0) * percent).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    @Override
    public String getDescription() {
        return String.valueOf(new BigDecimal(percent).setScale(0).intValue()) + "%";
    }

    @Override
    public double getValue() {
        return percent;
    }
}
