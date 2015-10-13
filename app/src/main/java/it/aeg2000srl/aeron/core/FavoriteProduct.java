package it.aeg2000srl.aeron.core;

/**
 * Created by tiziano.michelessi on 08/10/2015.
 */
public class FavoriteProduct extends Product {
    boolean isSelected;
    long productId;
    Customer mCustomer;
    int mHits;

    public FavoriteProduct(String name, String code, double price, long productId) {
        super(name, code, price);
        isSelected = false;
        this.productId = productId;
    }

    public void setHits(int hits) { mHits = hits; }

    public int getHits() { return mHits; }

    public void setCustomer(Customer customer) { mCustomer = customer; }

    public Customer getCustomer() { return mCustomer; }

    public void setSelected(boolean flag) {
        isSelected = flag;
    }

    public long getProductId() {
        return productId;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
