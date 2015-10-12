package it.aeg2000srl.aeron.core;

/**
 * Created by tiziano.michelessi on 09/10/2015.
 */
public class DiscountProduct implements IProduct {
    protected long id;
    protected String name;
    protected String code;
    protected double price;
    IDiscount discount;
    protected long customerId;

    public DiscountProduct(String code, String name, double price, IDiscount discount) {
        this.code = code;
        this.price = price;
        this.name = name;
        this.discount = discount;
    }

    public IDiscount getDiscount() {
        return discount;
    }

    @Override
    public double getPrice() {
        return discount.calculatePrice(price);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public void setPrice(double price) {
        this.price = price;
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
    public String toString() {
        return this.getName();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof DiscountProduct)) return false;
        DiscountProduct otherProduct = (DiscountProduct)other;
        return code.equals(otherProduct.getCode());
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }
}
