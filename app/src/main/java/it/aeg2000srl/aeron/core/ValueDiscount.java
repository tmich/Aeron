package it.aeg2000srl.aeron.core;

/**
 * Created by tiziano.michelessi on 09/10/2015.
 */
public class ValueDiscount implements IDiscount {
    double value;

    public ValueDiscount(double value) {
        this.value = value;
    }

    @Override
    public double calculatePrice(double price) {
        double discountPrice = price - value;
        return discountPrice;
    }

    @Override
    public String getDescription() {
        return "-" + String.valueOf(value);
    }

    @Override
    public double getValue() {
        return value;
    }
}
