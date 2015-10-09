package it.aeg2000srl.aeron.factories;

import java.util.Date;

import it.aeg2000srl.aeron.core.Customer;
import it.aeg2000srl.aeron.core.IOrder;
import it.aeg2000srl.aeron.core.Order;
import it.aeg2000srl.aeron.core.OrderIcewer;
import it.aeg2000srl.aeron.entities.ECustomer;
import it.aeg2000srl.aeron.entities.EOrder;
import it.aeg2000srl.aeron.entities.EOrderItem;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class OrderFactory implements IFactory<EOrder, IOrder> {
    EOrder entity;

    public static EOrder toEntity(IOrder order) {
        EOrder entity_ = new EOrder();
        entity_.setId(order.getId() != 0 ? order.getId() : null);
        entity_.creationDate = order.getCreationDate();
        entity_.user = null; //order.getUser();
        entity_.customer = new ECustomer();
        entity_.customer.setId(order.getCustomerId()); //= CustomerFactory.toEntity(order.getCustomer());
        entity_.notes = order.getNotes();
        entity_.sentDate = order.getSentDate();
        entity_.type = (order.getType() == IOrder.OrderType.ICEWER ? 1 : 0);

        return entity_;
    }

    @Override
    public OrderFactory from(EOrder entity) {
        this.entity = entity;
        return this;
    }

    @Override
    public IOrder make() {
        if (entity.customer != null) {
            CustomerFactory customerFactory = new CustomerFactory();
            Customer customer = customerFactory.from(entity.customer).make();
            IOrder o;
            if (entity.type == 0) {
                o = new Order(customer);
            } else {
                o = new OrderIcewer(customer);
            }
            o.setId(entity.getId());
            o.setCreationDate(entity.creationDate);
            o.setSentDate(entity.sentDate);
//        o.setUser(entity.user);

            return o;
        }
        return null;
    }
}
