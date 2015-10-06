package it.aeg2000srl.aeron.factories;

import it.aeg2000srl.aeron.core.Product;
import it.aeg2000srl.aeron.entities.EProduct;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class ProductFactory implements IFactory<EProduct, Product> {
    EProduct entity;

    public static EProduct toEntity(Product product) {
        EProduct entity = new EProduct();
        entity.code = product.getCode();
        entity.name = product.getName();
        entity.price = product.getPrice();
        entity.setId(product.getId() != 0 ? product.getId() : null);

        return entity;
    }

    @Override
    public ProductFactory from(EProduct entity) {
        this.entity = entity;
        return this;
    }

    @Override
    public Product make() {
        Product p = new Product(entity.name, entity.code, entity.price);
        p.setId(entity.getId());
        return p;
    }
}
