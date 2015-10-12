package it.aeg2000srl.aeron.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tiziano.michelessi on 09/10/2015.
 */
public class OrderIcewer implements IOrder {
//    private Customer customer;
    protected long id;
    protected long customerId;
    protected Date creationDate = null;
    protected Date sentDate = null;
    protected List<IOrderItem> items;
    private String notes;
    protected OrderType type;

    public OrderIcewer(Customer customer) {
        customerId = customer.getId();
        items = new ArrayList<>();
        creationDate = new Date();
        sentDate = new Date(0);
        type = OrderType.ICEWER;
    }

    @Override
    public void add(IProduct product, int quantity, String notes, String discount) {
        IOrderItem item = new OrderItemIcewer(product.getCode(), quantity, notes);
//        item.setProductName(product.getName());
        add(item);
    }

    @Override
    public void add(IOrderItem orderItem) {
        items.add(orderItem);
    }

    @Override
    public void remove(IOrderItem item) {
        items.remove(item);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public List<IOrderItem> getItems() {
        return items;
    }

    @Override
    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public long getCustomerId() {
        return customerId;
    }

    @Override
    public void setId(long newId) {
        id = newId;
    }

    @Override
    public void setSentDate(Date date) {
        sentDate = date;
    }

    @Override
    public String getNotes() {
        return notes;
    }

    @Override
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public Date getSentDate() {
        return sentDate;
    }

    @Override
    public void setCustomerId(long customerId) {

    }

    @Override
    public OrderType getType() {
        return type;
    }
}
