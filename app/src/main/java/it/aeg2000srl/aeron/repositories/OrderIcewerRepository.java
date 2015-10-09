package it.aeg2000srl.aeron.repositories;

import java.util.ArrayList;
import java.util.List;

import it.aeg2000srl.aeron.core.IOrder;
import it.aeg2000srl.aeron.core.IOrderItem;
import it.aeg2000srl.aeron.core.OrderItem;
import it.aeg2000srl.aeron.core.OrderItemIcewer;
import it.aeg2000srl.aeron.entities.EOrder;
import it.aeg2000srl.aeron.entities.EOrderItem;
import it.aeg2000srl.aeron.factories.OrderFactory;
import it.aeg2000srl.aeron.factories.OrderItemFactory;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class OrderIcewerRepository implements IRepository<IOrder> {
    OrderFactory fact;

    public OrderIcewerRepository() {
        fact = new OrderFactory();
    }

    protected OrderItemIcewer makeOrderItem(EOrderItem entity) {
        OrderItemIcewer orderItem = new OrderItemIcewer(entity.eProduct.code, entity.quantity, entity.notes);
        orderItem.setDiscount(entity.discount);
        orderItem.setNotes(entity.notes);
        orderItem.setProductName(entity.eProduct.name);
        orderItem.setId(entity.getId());

        return orderItem;
    }

    @Override
    public IOrder findById(long id) {
        IOrder order = fact.from(EOrder.findById(EOrder.class, id)).make();

//        try {
        List<EOrderItem> items = EOrderItem.find(EOrderItem.class, "e_order = ?", String.valueOf(id));
//        OrderItemFactory orderItemFactory = new OrderItemFactory();

        // aggiunge gli items
        for (EOrderItem entity : items) {
//            order.getItems().add(orderItemFactory.from(item).make());
            IOrderItem orderItem = makeOrderItem(entity);
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

    public List<IOrder> findByCustomerId(long customerId) {
        List<IOrder> orders = new ArrayList<>();
        List<EOrder> entities = EOrder.find(EOrder.class, "customer = ?", String.valueOf(customerId));

        for (EOrder entity : entities) {
            orders.add(findById(entity.getId()));
        }

        return orders;
    }

    public List<IOrder> getNotSent() {
        List<IOrder> orders = new ArrayList<>();
        List<EOrder> entities = EOrder.find(EOrder.class, "sent_date = ?", String.valueOf(0));

        for (EOrder entity : entities) {
            orders.add(findById(entity.getId()));
        }

        return orders;
    }

    @Override
    public long add(IOrder order) {
        EOrder entity = OrderFactory.toEntity(order);
        entity.save();

        for(IOrderItem item : order.getItems()) {
            EOrderItem eOrderItem = OrderItemFactory.toEntity(item);
            eOrderItem.eOrder = entity;
            eOrderItem.save();
        }

        return entity.getId();
    }

    @Override
    public void edit(IOrder order) {
        EOrder entity = OrderFactory.toEntity(order);
        entity.save();
    }

    @Override
    public void remove(IOrder order) {
        EOrder entity = OrderFactory.toEntity(order);
        entity.delete();
    }

    @Override
    public List<IOrder> getAll() {
        return null;
    }

    @Override
    public long size() {
        return EOrder.count(EOrder.class, null, null);
    }

    @Override
    public void addAll(List<IOrder> items) {

    }

    public List<IOrder> findAllByCustomerId(long customerId) {
        List<IOrder> orders = new ArrayList<>();
        List<EOrder> entities = EOrder.find(EOrder.class, "customer = ?", String.valueOf(customerId));

        for (EOrder entity : entities) {
            orders.add(findById(entity.getId()));
        }

        return orders;
    }

    public List<IOrder> findSentByCustomerId(long customerId) {
        List<IOrder> orders = new ArrayList<>();
        List<EOrder> entities = EOrder.find(EOrder.class, "customer = ? and sent_date <> ?", String.valueOf(customerId), String.valueOf(0));

        for (EOrder entity : entities) {
            orders.add(findById(entity.getId()));
        }

        return orders;
    }

    public List<IOrder> findNotSentByCustomerId(long customerId) {
        List<IOrder> orders = new ArrayList<>();
        List<EOrder> entities = EOrder.find(EOrder.class, "customer = ? and sent_date = 0", String.valueOf(customerId));

        for (EOrder entity : entities) {
            orders.add(findById(entity.getId()));
        }

        return orders;
    }
}
