package it.aeg2000srl.aeron.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tiziano.michelessi on 12/10/2015.
 */
public class PriceList {
    Customer mCustomer;
    List<DiscountProduct> mProducts;

    public PriceList(Customer customer) {
        mCustomer = customer;
        mProducts = new ArrayList<>();
    }

    public void add(DiscountProduct discountProduct) {
        mProducts.add(discountProduct);
    }

    public List<DiscountProduct> getProducts() {
        return mProducts;
    }

    public void remove(DiscountProduct discountProduct) {
        mProducts.remove(discountProduct);
    }

    public long getCustomerId() { return mCustomer.getId(); }
}
