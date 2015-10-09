package it.aeg2000srl.aeron.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class Order implements IOrder {
    protected long id;
    protected long customerId;
    protected Date creationDate = null;
    protected Date sentDate = null;
    protected List<IOrderItem> items;
    private String notes;

    public Order(Customer owner) {
        setCustomerId(owner.getId());
        items = new ArrayList<>();
        creationDate = new Date();
        sentDate = new Date(0);
    }

    public boolean hasBeenSent() {
        return !sentDate.equals(new Date(0));
    }

    @Override
    public void add(IProduct product, int quantity, String notes, String discount) {
        if (!has(product)) {
            OrderItem item = new OrderItem(product, quantity, notes, discount);
            item.setProductName(product.getName());
            item.setOrder(this);
            items.add(item);
        }
    }

    @Override
    public void add(IOrderItem orderItem) {
        if (getByProductId(orderItem.getProductId()) == null) {
            items.add(orderItem);
        }
    }

    @Override
    public void remove(IOrderItem item) {
        items.remove(getByProductId(item.getProductId()));
    }

    protected IOrderItem getByProduct(IProduct product) {
        for (IOrderItem item : items) {
            if(item.getProductId() == product.getId()) {
                return item;
            }
        }

        return null;
    }

    protected IOrderItem getByProductId(long productId) {
        for (IOrderItem item : items) {
            if(item.getProductId() == productId) {
                return item;
            }
        }

        return null;
    }

    @Override
    public List<IOrderItem> getItems() {
        return items;
    }

    protected boolean has(IProduct product) {
        return getByProduct(product) != null;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

//    public Customer getCustomer() {
//        return customer;
//    }
//
//    public void setCustomer(Customer customer) {
//        this.customer = customer;
//    }

    @Override
    public Date getCreationDate() {
        return creationDate;
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
    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    @Override
    public String getNotes() {
        return notes;
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
    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    @Override
    public OrderType getType() {
        return OrderType.NORMAL;
    }
}
