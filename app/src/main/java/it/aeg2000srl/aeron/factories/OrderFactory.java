package it.aeg2000srl.aeron.factories;

import java.util.Date;

import it.aeg2000srl.aeron.core.Customer;
import it.aeg2000srl.aeron.core.Order;
import it.aeg2000srl.aeron.entities.ECustomer;
import it.aeg2000srl.aeron.entities.EOrder;
import it.aeg2000srl.aeron.entities.EOrderItem;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class OrderFactory implements IFactory<EOrder, Order> {
    EOrder entity;

    public static EOrder toEntity(Order order) {
        EOrder entity_ = new EOrder();
        entity_.setId(order.getId() != 0 ? order.getId() : null);
        entity_.creationDate = order.getCreationDate();
        entity_.user = null; //order.getUser();
        entity_.customer = new ECustomer();
        entity_.customer.setId(order.getCustomerId()); //= CustomerFactory.toEntity(order.getCustomer());
        entity_.notes = order.getNotes();
        entity_.sentDate = order.getSentDate();

        return entity_;
    }

    @Override
    public OrderFactory from(EOrder entity) {
        this.entity = entity;
        return this;
    }

    @Override
    public Order make() {
        if (entity.customer != null) {
            CustomerFactory customerFactory = new CustomerFactory();
            Customer customer = customerFactory.from(entity.customer).make();

            Order o = new Order(customer);
            o.setId(entity.getId());
            o.setCreationDate(entity.creationDate);
            o.setSentDate(entity.sentDate);
//        o.setUser(entity.user);

            return o;
        }
        return null;
    }
}
