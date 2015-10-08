package it.aeg2000srl.aeron.core;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class Product {
    protected long id;
    protected String name;
    protected String code;
    protected double price;

    public Product(String name, String code, double price) {
        this.code = code;
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getId() {
        return id;
    }

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
        if (!(other instanceof Product)) return false;
        Product otherProduct = (Product)other;
        return id == otherProduct.getId();
    }
}
