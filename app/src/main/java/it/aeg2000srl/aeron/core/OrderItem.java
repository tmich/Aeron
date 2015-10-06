package it.aeg2000srl.aeron.core;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class OrderItem {
    protected long id;
    private long product_id;
    protected int quantity;
    protected String discount;
    protected String notes;

    public OrderItem(long productId, int quantity) {
        this(productId, quantity, null, null);
    }

    public OrderItem(long productId, int quantity, String notes) {
        this(productId, quantity, notes, null);
    }

    public OrderItem(long productId, int quantity, String notes, String discount) {
        product_id = productId;
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

    public long getProductId() {
        return product_id;
    }
}
