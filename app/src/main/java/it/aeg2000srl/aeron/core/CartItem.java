package it.aeg2000srl.aeron.core;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by tiziano.michelessi on 29/12/2015.
 */
public class CartItem extends OrderItem implements Serializable {

    public CartItem(IProduct product, int quantity) {
        super(product, quantity, null);
    }

    public CartItem(IProduct product, int quantity, String notes) {
        super(product, quantity, notes);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof CartItem )) return false;
        return (((CartItem) other).product_code.equals(this.product_code));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.quantity, this.product_code, this.product_id);
    }
}
