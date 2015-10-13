package it.aeg2000srl.aeron.repositories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.aeg2000srl.aeron.core.Customer;
import it.aeg2000srl.aeron.core.FavoriteProduct;
import it.aeg2000srl.aeron.core.Product;
import it.aeg2000srl.aeron.entities.EFavoriteProduct;

/**
 * Created by tiziano.michelessi on 13/10/2015.
 */
public class FavoriteProductRepository implements IRepository<FavoriteProduct> {

    protected FavoriteProduct toBusinessObject(EFavoriteProduct entity) {
        ProductRepository productRepository = new ProductRepository();
        Product product = productRepository.findByCode(entity.productCode);

        CustomerRepository customerRepository = new CustomerRepository();
        Customer customer = customerRepository.findByCode(entity.customerCode);

        FavoriteProduct businessObject = new FavoriteProduct(product.getName(), product.getCode(), product.getPrice(), product.getId());
        businessObject.setCustomer(customer);
        businessObject.setHits(businessObject.getHits() + entity.hits);
        return businessObject;
    }

    protected EFavoriteProduct toEntity(FavoriteProduct favoriteProduct) {
        EFavoriteProduct entity = new EFavoriteProduct();
        entity.customerCode = favoriteProduct.getCustomer().getCode();
        entity.productCode = favoriteProduct.getCode();
        entity.hits = favoriteProduct.getHits();
        return entity;
    }

    @Override
    public FavoriteProduct findById(long id) {
        FavoriteProduct favoriteProduct = null;
        EFavoriteProduct entity = EFavoriteProduct.findById(EFavoriteProduct.class, id);
        if (entity != null) {
            favoriteProduct = toBusinessObject(entity);
        }
        return favoriteProduct;
    }

    @Override
    public long add(FavoriteProduct favoriteProduct) {
        EFavoriteProduct entity = null;
        try {
            entity = EFavoriteProduct.find(EFavoriteProduct.class, "product_code = ? and customer_code = ?",
                    new String[]{ favoriteProduct.getCode(), favoriteProduct.getCustomer().getCode() }).get(0);
        } catch (IndexOutOfBoundsException notExisting) {
            entity = toEntity(favoriteProduct);
        }

        // update hits
        entity.hits += favoriteProduct.getHits();
        entity.save();
        return entity.getId();
    }

    @Override
    public void edit(FavoriteProduct favoriteProduct) {

    }

    @Override
    public void remove(FavoriteProduct favoriteProduct) {
        EFavoriteProduct entity = null;
        try {
            entity = EFavoriteProduct.find(EFavoriteProduct.class, "product_code = ? and customer_code = ?",
                    new String[]{ favoriteProduct.getCode(), favoriteProduct.getCustomer().getCode() }).get(0);
            entity.delete();
        } catch (IndexOutOfBoundsException notExisting) {
            return;
        }
    }

    @Override
    public List<FavoriteProduct> getAll() {
        return null;
    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public void addAll(List<FavoriteProduct> items) {
        for (FavoriteProduct favoriteProduct : items) {
            add(favoriteProduct);
        }
    }

    public List<FavoriteProduct> findByCustomerCode(String customerCode) {
        List<EFavoriteProduct> entities = EFavoriteProduct.find(EFavoriteProduct.class, "customer_code = ?", new String[]{ customerCode });
        List<FavoriteProduct> favoriteProducts = new ArrayList<>(entities.size());

        for (EFavoriteProduct entity : entities) {
            favoriteProducts.add(toBusinessObject(entity));
        }

        Collections.sort(favoriteProducts, new HitsComparator());
        Collections.reverse(favoriteProducts);
        return favoriteProducts;
    }

    class HitsComparator implements Comparator<FavoriteProduct> {

        @Override
        public int compare(FavoriteProduct f1, FavoriteProduct f2) {
            if (f1.getHits() > f2.getHits()) {
                return 1;
            } else if (f1.getHits() < f2.getHits()) {
                return -1;
            }

            return 0;
        }
    }
}
