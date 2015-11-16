package it.aeg2000srl.aeron.repositories;

import java.util.ArrayList;
import java.util.List;

import it.aeg2000srl.aeron.core.IOrder;
import it.aeg2000srl.aeron.core.IOrderItem;
import it.aeg2000srl.aeron.core.IProduct;
import it.aeg2000srl.aeron.core.Order;
import it.aeg2000srl.aeron.core.OrderItem;
import it.aeg2000srl.aeron.core.OrderItemIcewer;
import it.aeg2000srl.aeron.core.Product;
import it.aeg2000srl.aeron.entities.EOrder;
import it.aeg2000srl.aeron.entities.EOrderItem;
import it.aeg2000srl.aeron.factories.OrderFactory;
import it.aeg2000srl.aeron.factories.OrderItemFactory;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class OrderRepository implements IRepository<IOrder> {
    OrderFactory fact;

    public OrderRepository() {
        fact = new OrderFactory();
    }

    protected OrderItem makeOrderItem(EOrderItem entity) {
        ProductRepository productRepository = new ProductRepository();
        IProduct product = productRepository.findById(entity.eProduct.getId());
        OrderItem orderItem = new OrderItem(product, entity.quantity);
        orderItem.setDiscount(entity.discount);
        orderItem.setNotes(entity.notes);
        orderItem.setProductName(entity.eProduct.name);
        orderItem.setProductCode(entity.eProduct.code);
        orderItem.setId(entity.getId());
        orderItem.setPrice(entity.eProduct.price);
//        orderItem.setOrder(findById(entity.eOrder.getId()));

        return orderItem;
    }

    protected OrderItemIcewer makeOrderItemIcewer(EOrderItem entity) {
        OrderItemIcewer orderItem = new OrderItemIcewer(entity.productCode, entity.quantity, entity.notes);
        orderItem.setDiscount(entity.discount);
        orderItem.setNotes(entity.notes);
        orderItem.setId(entity.getId());
        orderItem.setPrice(entity.price);
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
                IOrderItem orderItem;
                if (order.getType() == IOrder.OrderType.NORMAL) {
                    orderItem = makeOrderItem(entity);
                } else {
                    orderItem = makeOrderItemIcewer(entity);
                }
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

        // differenza tra gli items presenti
        ArrayList<IOrderItem> itemsToSave = new ArrayList<>(order.getItems().size());
        ArrayList<IOrderItem> itemsToRemove = new ArrayList<>(order.getItems().size());

//        for(IOrderItem item : order.getItems()) {
//            EOrderItem eOrderItem = OrderItemFactory.toEntity(item);
//            eOrderItem.eOrder = entity;
//            eOrderItem.save();
//        }

        List<EOrderItem> itemsOnDb = EOrderItem.find(EOrderItem.class, "e_order = ?", String.valueOf(order.getId()));

        // items da tenere
        for(IOrderItem item : order.getItems()) {
            itemsToSave.add(item);
        }

        // items da eliminare
        for(EOrderItem it : itemsOnDb) {
            IOrderItem iit = new OrderItemFactory().from(it).make();
            if(!(order.getItems().contains(iit))) {
                itemsToRemove.add(iit);
            }
        }

        // salvataggio items
        for (IOrderItem itemToSave :
                itemsToSave) {
            EOrderItem eOrderItem = OrderItemFactory.toEntity(itemToSave);
            eOrderItem.eOrder = entity;
            eOrderItem.save();
        }

        // eliminazione items
        for(IOrderItem itemToRemove : itemsToRemove) {
            EOrderItem eOrderItem = OrderItemFactory.toEntity(itemToRemove);
            eOrderItem.delete();
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
