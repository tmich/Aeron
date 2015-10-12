package it.aeg2000srl.aeron.repositories;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.aeg2000srl.aeron.core.IOrder;
import it.aeg2000srl.aeron.core.IOrderItem;
import it.aeg2000srl.aeron.core.Order;
import it.aeg2000srl.aeron.core.Product;
import it.aeg2000srl.aeron.entities.EProduct;
import it.aeg2000srl.aeron.factories.ProductFactory;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class ProductRepository implements IRepository<Product> {
    ProductFactory fact;

    public ProductRepository() {
        fact = new ProductFactory();
    }


    @Override
    public Product findById(long id) {
        try {
            return fact.from(EProduct.findById(EProduct.class, id)).make();
        } catch (Exception exc) {
            return null;
        }
    }

    public Product findByCode(String code) {
        if (code != null) {
            if (EProduct.find(EProduct.class, "code = ?", code).isEmpty()) {
                return null;
            } else {
                EProduct eProduct = EProduct.find(EProduct.class, "code = ?", code).get(0);
                return fact.from(eProduct).make();
            }
        }
        return null;
    }

    public List<Product> findByName(String name) {
        ArrayList<Product> products = new ArrayList<>();
        for (EProduct entity : Select.from(EProduct.class).where(Condition.prop("name").like("%" + name + "%")).list()) {
            products.add(fact.from(entity).make());
        }
        return products;
    }

    @Override
    public long add(Product product) {
        EProduct entity = ProductFactory.toEntity(product);
        entity.save();
        return entity.getId();
    }

    @Override
    public void edit(Product product) {
        EProduct entity = ProductFactory.toEntity(product);
        entity.save();
    }

    @Override
    public void remove(Product product) {
        EProduct entity = ProductFactory.toEntity(product);
        entity.delete();
    }

    @Override
    public List<Product> getAll() {
        List<Product> all = new ArrayList<>();
        for (EProduct entity : EProduct.listAll(EProduct.class)) {
            all.add(fact.from(entity).make());
        }
        return all;
    }

    @Override
    public long size() {
        return EProduct.count(EProduct.class, null, null);
    }

    @Override
    public void addAll(List<Product> items) {
        List<EProduct> entities = new ArrayList<>(items.size());

        for(Product product : items) {
            EProduct entity = ProductFactory.toEntity(product);

            // check existance of product
            Product px = findByCode(product.getCode());
            if (px != null) {
                entity.setId(px.getId());
            }
            entities.add(entity);
        }

        EProduct.saveInTx(entities);
    }

    public List<Product> getMostOrderedByCustomerId(long customerId) {
        ArrayList<Product> products = new ArrayList<>();
        OrderRepository orderRepository = new OrderRepository();
        List<IOrder> orders = orderRepository.findAllByCustomerId(customerId);

        for (IOrder order : orders) {
            if(order.getType() == IOrder.OrderType.NORMAL) {
                for (IOrderItem item : order.getItems()) {
                    EProduct entity = EProduct.findById(EProduct.class, item.getProductId());
                    Product fav = fact.from(entity).make();
                    if (!products.contains(fav)) {
                        products.add(fav);
                    }
                }
            }
        }

        return products;
    }
}
