package it.aeg2000srl.aeron.repositories;

import java.util.List;

import it.aeg2000srl.aeron.core.Order;
import it.aeg2000srl.aeron.core.OrderItem;
import it.aeg2000srl.aeron.entities.EOrder;
import it.aeg2000srl.aeron.entities.EOrderItem;
import it.aeg2000srl.aeron.factories.CustomerFactory;
import it.aeg2000srl.aeron.factories.OrderFactory;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class OrderRepository implements IRepository<Order> {
    OrderFactory fact;

    public OrderRepository() {
        fact = new OrderFactory();
    }

    @Override
    public Order findById(long id) {
        Order order = fact.from(EOrder.findById(EOrder.class, id)).make();

        List<EOrderItem> items = EOrderItem.find(EOrderItem.class, "e_order = ?", String.valueOf(id));
        for (EOrderItem item : items) {
            order.getItems().add(createItem(item));
        }

        return order;
    }

    protected OrderItem createItem(EOrderItem entity) {
        OrderItem orderItem = new OrderItem(entity.eProduct.getId(), entity.quantity);
        orderItem.setId(entity.getId());
        orderItem.setDiscount(entity.discount);
        orderItem.setNotes(entity.notes);

        return orderItem;
    }

    @Override
    public long add(Order order) {
        return 0;
    }

    @Override
    public void edit(Order order) {

    }

    @Override
    public void remove(Order order) {

    }

    @Override
    public List<Order> getAll() {
        return null;
    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public void addAll(List<Order> items) {

    }
}
