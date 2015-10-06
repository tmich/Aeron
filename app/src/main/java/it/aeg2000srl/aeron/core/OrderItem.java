package it.aeg2000srl.aeron.core;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class OrderItem {
    protected long id;
    protected Product product;
    protected int quantity;
    protected String discount;
    protected String notes;
    protected Order order;

    public OrderItem(Product product, int quantity) {
        this(product, quantity, null, null);
    }

    public OrderItem(Product product, int quantity, String notes) {
        this(product, quantity, notes, null);
    }

    public OrderItem(Product product, int quantity, String notes, String discount) {
        this.product = product;
        this.quantity = quantity;
        this.notes = notes;
        this.discount = discount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Product getProduct() {
        return product;
    }

    public Order getOrder() { return order; }

    public void setOrder(Order order) { this.order = order; }
}
