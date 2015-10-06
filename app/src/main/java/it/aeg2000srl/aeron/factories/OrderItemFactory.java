package it.aeg2000srl.aeron.factories;

import it.aeg2000srl.aeron.core.OrderItem;
import it.aeg2000srl.aeron.core.Product;
import it.aeg2000srl.aeron.entities.EOrderItem;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class OrderItemFactory implements IFactory<EOrderItem, OrderItem> {
    EOrderItem entity;

    public static EOrderItem toEntity(OrderItem orderItem) {
        EOrderItem entity = new EOrderItem();
        entity.setId(orderItem.getId() != 0 ? orderItem.getId() : null);
        entity.eProduct = ProductFactory.toEntity(orderItem.getProduct());
        entity.discount = orderItem.getDiscount();
        entity.quantity = orderItem.getQuantity();
        entity.notes = orderItem.getNotes();
        //entity.eOrder = OrderFactory.toEntity(orderItem.getOrder());
        return entity;
    }

    @Override
    public OrderItemFactory from(EOrderItem entity) {
        this.entity = entity;
        return this;
    }

    @Override
    public OrderItem make() {
        ProductFactory productFactory = new ProductFactory();
        Product product = productFactory.from(entity.eProduct).make();
        OrderFactory orderFactory = new OrderFactory();
        OrderItem orderItem = new OrderItem(product, entity.quantity, entity.notes, entity.discount);
        orderItem.setOrder(orderFactory.from(entity.eOrder).make());
        return orderItem;
    }
}
