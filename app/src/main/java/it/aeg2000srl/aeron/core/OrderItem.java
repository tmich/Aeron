package it.aeg2000srl.aeron.core;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class OrderItem {
    protected long id;
//    protected Product product;
    protected long product_id;
    protected String product_name;
    protected int quantity;
    protected String discount;
    protected String notes;
    protected Order order;

    public OrderItem(long productId, int quantity) {
        this(productId, quantity, null, null);
    }

    public OrderItem(long productId, int quantity, String notes) {
        this(productId, quantity, notes, null);
    }

    public OrderItem(long productId, int quantity, String notes, String discount) {
//        this.product = product;
        this.product_id = productId;
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

//    public Product getProduct() {
//        return product;
//    }

    public long getProductId() {
        return product_id;
    }

    public void setProductId(long productId) {
        this.product_id = productId;
    }

    public Order getOrder() { return order; }

    public void setOrder(Order order) { this.order = order; }

    public String getProductName() {
        return product_name;
    }

    public void setProductName(String productName) {
        product_name = productName;
    }
}
