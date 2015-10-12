package it.aeg2000srl.aeron.core;

/**
 * Created by tiziano.michelessi on 09/10/2015.
 */
public interface IOrderItem {
    long getId();

    void setId(long id);

    int getQuantity();

    void setQuantity(int quantity);

    String getDiscount();

    void setDiscount(String discount);

    String getNotes();

    void setNotes(String notes);

    long getProductId();

    void setProductId(long productId);

    String getProductCode();

    void setProductCode(String productCode);

    IOrder getOrder();

    void setOrder(IOrder order);

    String getProductName();

    void setProductName(String productName);

    void setPrice(double price);

    double getPrice();
}
