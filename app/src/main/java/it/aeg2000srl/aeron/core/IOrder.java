package it.aeg2000srl.aeron.core;

import java.util.List;

/**
 * Created by tiziano.michelessi on 08/10/2015.
 */
public interface IOrder {
    void add(Product product, int quantity, String notes, String discount);

    void remove(OrderItem item);

    List<OrderItem> getItems();

    long getCustomerId();
}
