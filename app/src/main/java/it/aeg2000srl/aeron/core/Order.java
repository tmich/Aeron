package it.aeg2000srl.aeron.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class Order {
    protected long id;
    protected Customer customer;
    protected Date creationDate;
//    protected long userId;
    protected Date sentDate;
    protected List<OrderItem> items;
    private String notes;

    public Order(Customer owner) {
        customer = owner;
        items = new ArrayList<>();
    }

    public void add(Product product, int quantity, String notes, String discount) {
        if (!has(product)) {
            OrderItem item = new OrderItem(product.getId(), quantity, notes, discount);
            items.add(item);
        }
    }

    public void remove(Product product) {
        items.remove(getByProduct(product));
    }

    protected OrderItem getByProduct(Product product) {
        for (OrderItem item : items) {
            if(item.getProductId() == product.getId()) {
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

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
}
