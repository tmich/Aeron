package it.aeg2000srl.aeron.core;

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
        double discountPrice = price -((price / 100.0) * percent);
        return discountPrice;
    }
}
