package it.aeg2000srl.aeron.core;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * Created by tiziano.michelessi on 18/11/2015.
 */
public class Cart implements Serializable {
    protected long id;
    protected Customer customer;
    protected List<CartItem> items;

    public Cart() {
        items = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<CartItem> getItems() {
        return items;
    }
}
