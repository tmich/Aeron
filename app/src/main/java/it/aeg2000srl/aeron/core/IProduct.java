package it.aeg2000srl.aeron.core;

/**
 * Created by tiziano.michelessi on 09/10/2015.
 */
public interface IProduct {
    String getName();

    void setName(String name);

    String getCode();

    void setCode(String code);

    double getPrice();

    void setPrice(double price);

    long getId();

    void setId(long id);

    @Override
    String toString();

    @Override
    boolean equals(Object other);
}
