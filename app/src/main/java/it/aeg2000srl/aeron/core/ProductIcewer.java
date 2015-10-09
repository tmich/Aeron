package it.aeg2000srl.aeron.core;

/**
 * Created by tiziano.michelessi on 09/10/2015.
 */
public class ProductIcewer implements IProduct {
    String code;

    public ProductIcewer(String code) {
        this.code = code;
    }

    @Override
    public String getName() {
        return code;
    }

    @Override
    public void setName(String name) {
        code = name;
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
    public double getPrice() {
        return 0;
    }

    @Override
    public void setPrice(double price) {

    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public void setId(long id) {

    }
}
