package it.aeg2000srl.aeron.core;

/**
 * Created by tiziano.michelessi on 08/10/2015.
 */
public class FavoriteProduct extends Product {
    boolean isSelected;
    long productId;

    public FavoriteProduct(String name, String code, double price, long productId) {
        super(name, code, price);
        isSelected = false;
        this.productId = productId;
    }

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
