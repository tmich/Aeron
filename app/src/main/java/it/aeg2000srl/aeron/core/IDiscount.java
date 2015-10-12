package it.aeg2000srl.aeron.core;

/**
 * Created by tiziano.michelessi on 09/10/2015.
 */
public interface IDiscount {
    double calculatePrice(double price);
    String getDescription();
    double getValue();
}
