package it.aeg2000srl.aeron.factories;

import java.util.ArrayList;
import java.util.LinkedList;

import it.aeg2000srl.aeron.core.Cart;
import it.aeg2000srl.aeron.core.IOrderItem;
import it.aeg2000srl.aeron.entities.ECart;
import it.aeg2000srl.aeron.entities.ECustomer;
import it.aeg2000srl.aeron.entities.EOrderItem;

/**
 * Created by tiziano.michelessi on 18/11/2015.
 */
public class CartFactory implements IFactory<ECart, Cart> {
    ECart entity;

    @Override
    public IFactory from(ECart from) {
        this.entity = from;
        return this;
    }

    public static ECart toEntity(Cart cart) {
        ECart eCart = new ECart();
        eCart.setId(cart.getId() != 0 ? cart.getId() : null);
        eCart.customer = new ECustomer();
        eCart.customer.address = cart.getCustomer().getAddress();
        eCart.customer.cap = cart.getCustomer().getCap();
        eCart.customer.city = cart.getCustomer().getCity();
        eCart.customer.code = cart.getCustomer().getCode();
        eCart.customer.iva = cart.getCustomer().getIva();
        eCart.customer.name = cart.getCustomer().getName();
        eCart.customer.province = cart.getCustomer().getProvince();
        eCart.customer.telephone = cart.getCustomer().getTelephone();
        eCart.items = new ArrayList<>();
        for (IOrderItem item : cart.getItems()) {
            EOrderItem i = new EOrderItem();
            i.setId(item.getId() != 0 ? item.getId() : null);

        }
        return eCart;
    }

    @Override
    public Cart make() {
        return null;
    }
}
