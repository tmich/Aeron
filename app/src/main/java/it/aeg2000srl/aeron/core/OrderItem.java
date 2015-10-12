package it.aeg2000srl.aeron.core;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class OrderItem implements IOrderItem {
    protected long id;
//    protected Product product;
    protected long product_id;
    protected String product_code;
    protected String product_name;
    protected int quantity;
    protected String discount;
    protected String notes;
    protected IOrder order;
    private double price;

    public OrderItem(IProduct product, int quantity) {
        this(product, quantity, null, null);
    }

    public OrderItem(IProduct product, int quantity, String notes) {
        this(product, quantity, notes, null);
    }

    public OrderItem(IProduct product, int quantity, String notes, String discount) {
//        this.product = product;
        this.product_id = product.getId();
        this.quantity = quantity;
        this.notes = notes;
        this.discount = discount;
        this.product_code = product.getCode();
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String getDiscount() {
        return discount;
    }

    @Override
    public void setDiscount(String discount) {
        this.discount = discount;
    }

    @Override
    public String getNotes() {
        return notes;
    }

    @Override
    public void setNotes(String notes) {
        this.notes = notes;
    }

//    public Product getProduct() {
//        return product;
//    }

    @Override
    public long getProductId() {
        return product_id;
    }

    @Override
    public void setProductId(long productId) {
        this.product_id = productId;
    }

    @Override
    public String getProductCode() {
        return this.product_code;
    }

    @Override
    public void setProductCode(String productCode) {
        this.product_code = productCode;
    }

    @Override
    public IOrder getOrder() { return order; }

    @Override
    public void setOrder(IOrder order) { this.order = order; }

    @Override
    public String getProductName() {
        return product_name;
    }

    @Override
    public void setProductName(String productName) {
        product_name = productName;
    }

    @Override
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public double getPrice() {
        return price;
    }
}
