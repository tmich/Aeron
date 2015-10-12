package it.aeg2000srl.aeron.core;

/**
 * Created by tiziano.michelessi on 09/10/2015.
 */
public class OrderItemIcewer implements IOrderItem {
    int quantity;
    String notes;
    String productCode;
    IOrder order;
    double price;

    public OrderItemIcewer(String productCode, int quantity, String notes) {
        this.productCode = productCode;
        this.quantity = quantity;
        this.notes = notes;
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public void setId(long id) {

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
        return null;
    }

    @Override
    public void setDiscount(String discount) {

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
    public long getProductId() {
        return 0;
    }

    @Override
    public void setProductId(long productId) {

    }

    @Override
    public String getProductCode() {
        return productCode;
    }

    @Override
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    @Override
    public IOrder getOrder() {
        return order;
    }

    @Override
    public void setOrder(IOrder order) {
        this.order = order;
    }

    @Override
    public String getProductName() {
        return productCode;
    }

    @Override
    public void setProductName(String productName) {
        productCode = productName;
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
