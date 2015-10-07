package it.aeg2000srl.aeron.repositories;

import java.util.ArrayList;
import java.util.List;

import it.aeg2000srl.aeron.core.Order;
import it.aeg2000srl.aeron.core.OrderItem;
import it.aeg2000srl.aeron.core.Product;
import it.aeg2000srl.aeron.entities.EOrder;
import it.aeg2000srl.aeron.entities.EOrderItem;
import it.aeg2000srl.aeron.factories.CustomerFactory;
import it.aeg2000srl.aeron.factories.OrderFactory;
import it.aeg2000srl.aeron.factories.OrderItemFactory;
import it.aeg2000srl.aeron.factories.ProductFactory;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class OrderRepository implements IRepository<Order> {
    OrderFactory fact;

    public OrderRepository() {
        fact = new OrderFactory();
    }

    protected OrderItem makeOrderItem(EOrderItem entity) {
        OrderItem orderItem = new OrderItem(entity.eProduct.getId(), entity.quantity);
        orderItem.setDiscount(entity.discount);
        orderItem.setNotes(entity.notes);

        return orderItem;
    }

    @Override
    public Order findById(long id) {
        Order order = fact.from(EOrder.findById(EOrder.class, id)).make();

//        try {
            List<EOrderItem> items = EOrderItem.find(EOrderItem.class, "e_order = ?", String.valueOf(id));
//        OrderItemFactory orderItemFactory = new OrderItemFactory();

            // aggiunge gli items
            for (EOrderItem entity : items) {
//            order.getItems().add(orderItemFactory.from(item).make());
                OrderItem orderItem = makeOrderItem(entity);
                orderItem.setOrder(order);
                order.getItems().add(orderItem);
            }

            return order;
//        } catch (NullPointerException exc) {
//            //remove(order);
//            EOrder.findById(EOrder.class, id).delete();
//        }

//        return null;
    }

    public List<Order> findByCustomerId(long customerId) {
        List<Order> orders = new ArrayList<>();
        List<EOrder> entities = EOrder.find(EOrder.class, "customer = ?", String.valueOf(customerId));

        for (EOrder entity : entities) {
            orders.add(findById(entity.getId()));
        }

        return orders;
    }

    public List<Order> getNotSent() {
        List<Order> orders = new ArrayList<>();
        List<EOrder> entities = EOrder.find(EOrder.class, "sent_date = ?", String.valueOf(0));

        for (EOrder entity : entities) {
            orders.add(findById(entity.getId()));
        }

        return orders;
    }

    @Override
    public long add(Order order) {
        EOrder entity = OrderFactory.toEntity(order);
        entity.save();

        for(OrderItem item : order.getItems()) {
            EOrderItem eOrderItem = OrderItemFactory.toEntity(item);
            eOrderItem.eOrder = entity;
            eOrderItem.save();
        }

        return entity.getId();
    }

    @Override
    public void edit(Order order) {
        EOrder entity = OrderFactory.toEntity(order);
        entity.save();
    }

    @Override
    public void remove(Order order) {
        EOrder entity = OrderFactory.toEntity(order);
        entity.delete();
    }

    @Override
    public List<Order> getAll() {
        return null;
    }

    @Override
    public long size() {
        return EOrder.count(EOrder.class, null, null);
    }

    @Override
    public void addAll(List<Order> items) {

    }
}
