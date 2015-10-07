package it.aeg2000srl.aeron.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class Order {
    protected long id;
    protected long customerId;
    protected Date creationDate = null;
    protected Date sentDate = null;
    protected List<OrderItem> items;
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

    public void add(OrderItem orderItem) {
        if (getByProductId(orderItem.getProductId()) == null) {
            items.add(orderItem);
        }
    }

    public void add(Product product, int quantity, String notes, String discount) {
        if (!has(product)) {
            OrderItem item = new OrderItem(product.getId(), quantity, notes, discount);
            item.setProductName(product.getName());
            items.add(item);
        }
    }

    public void remove(OrderItem item) {
        items.remove(getByProductId(item.getProductId()));
    }

    protected OrderItem getByProduct(Product product) {
        for (OrderItem item : items) {
            if(item.getProductId() == product.getId()) {
                return item;
            }
        }

        return null;
    }

    protected OrderItem getByProductId(long productId) {
        for (OrderItem item : items) {
            if(item.getProductId() == productId) {
                return item;
            }
        }

        return null;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    protected boolean has(Product product) {
        return getByProduct(product) != null;
    }

    public long getId() {
        return id;
    }

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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }
}
