package it.aeg2000srl.aeron.factories;

import it.aeg2000srl.aeron.core.Customer;
import it.aeg2000srl.aeron.core.Order;
import it.aeg2000srl.aeron.entities.EOrder;
import it.aeg2000srl.aeron.entities.EOrderItem;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class OrderFactory implements IFactory<EOrder, Order> {
    EOrder entity;

    public static EOrder toEntity(Order order) {
        EOrder entity = new EOrder();
        entity.setId(order.getId() != 0 ? order.getId() : null);
        entity.creationDate = order.getCreationDate();
//        entity.user = order.getUser();
        entity.customer = CustomerFactory.toEntity(order.getCustomer());
        entity.notes = order.getNotes();
        entity.sentDate = order.getSentDate();

        return entity;
    }

    @Override
    public OrderFactory from(EOrder entity) {
        this.entity = entity;
        return this;
    }

    @Override
    public Order make() {
        CustomerFactory customerFactory = new CustomerFactory();
        Customer customer = customerFactory.from(entity.customer).make();

        Order o = new Order(customer);
        o.setId(entity.getId());
        o.setCreationDate(entity.creationDate);
        o.setSentDate(entity.sentDate);
//        o.setUser(entity.user);

        return o;
    }
}
