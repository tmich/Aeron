package it.aeg2000srl.aeron.repositories;

import java.util.ArrayList;
import java.util.List;

import it.aeg2000srl.aeron.core.Customer;
import it.aeg2000srl.aeron.core.DiscountProduct;
import it.aeg2000srl.aeron.core.IDiscount;
import it.aeg2000srl.aeron.core.PercentDiscount;
import it.aeg2000srl.aeron.core.PriceList;
import it.aeg2000srl.aeron.core.ValueDiscount;
import it.aeg2000srl.aeron.entities.ECustomer;
import it.aeg2000srl.aeron.entities.EDiscountProduct;

/**
 * Created by tiziano.michelessi on 12/10/2015.
 */
public class PriceListRepository implements IRepository<DiscountProduct> {

    protected DiscountProduct toBusinessObject(EDiscountProduct entity) {
        IDiscount discount;
        if (entity.type == EDiscountProduct.DiscountType.PercentDiscount) {
            discount = new PercentDiscount(entity.discountValue);
        } else {
            discount = new ValueDiscount(entity.discountValue);
        }
        DiscountProduct dp = new DiscountProduct(entity.productCode, entity.productName, entity.originalPrice, discount);
        dp.setCustomerId(entity.customer.getId());
        dp.setId(entity.getId());

        return dp;
    }

    protected EDiscountProduct toEntity(DiscountProduct discountProduct) {
        EDiscountProduct entity = new EDiscountProduct();
        ProductRepository productRepository = new ProductRepository();
        entity.setId(discountProduct.getId() != 0 ? discountProduct.getId() : null);
        entity.customer = ECustomer.findById(ECustomer.class, discountProduct.getCustomerId());
        entity.type = (discountProduct.getDiscount() instanceof PercentDiscount ? EDiscountProduct.DiscountType.PercentDiscount : EDiscountProduct.DiscountType.ValueDiscount);
        entity.discountValue = discountProduct.getDiscount().getValue();
        entity.originalPrice = productRepository.findByCode(discountProduct.getCode()).getPrice();
        entity.productCode = discountProduct.getCode();
        entity.productName = discountProduct.getName();

        return entity;
    }

    @Override
    public DiscountProduct findById(long id) {
        EDiscountProduct entity = EDiscountProduct.findById(EDiscountProduct.class, id);
        return toBusinessObject(entity);
    }

    @Override
    public long add(DiscountProduct discountProduct) {
        EDiscountProduct entity = toEntity(discountProduct);
        entity.save();
        return entity.getId();
    }

    @Override
    public void edit(DiscountProduct discountProduct) {
        EDiscountProduct entity = toEntity(discountProduct);
        entity.save();
    }

    @Override
    public void remove(DiscountProduct discountProduct) {
        EDiscountProduct entity = EDiscountProduct.findById(EDiscountProduct.class, discountProduct.getId());
        entity.delete();
    }

    @Override
    public List<DiscountProduct> getAll() {
        List<EDiscountProduct> discountedProducts = EDiscountProduct.find(EDiscountProduct.class, null, null);
        List<DiscountProduct> pl = new ArrayList<>(discountedProducts.size());

        for (EDiscountProduct entity : discountedProducts) {
            pl.add(toBusinessObject(entity));
        }

        return pl;
    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public void addAll(List<DiscountProduct> items) {
        for(DiscountProduct discountProduct : items) {
            EDiscountProduct entity = toEntity(discountProduct);
            entity.save();
        }
    }

    public PriceList getPriceListForCustomerId(long customerId) {
        CustomerRepository customerRepository = new CustomerRepository();
        Customer customer = customerRepository.findById(customerId);
        PriceList pl = new PriceList(customerRepository.findById(customer.getId()));

        List<EDiscountProduct> discountedProducts = EDiscountProduct.find(EDiscountProduct.class, "customer = ?", String.valueOf(customerId));
        for (EDiscountProduct entity : discountedProducts) {
            DiscountProduct discountProduct = toBusinessObject(entity);
            discountProduct.setCustomerId(customer.getId());
            pl.add(discountProduct);
        }

        return pl;
    }

    public DiscountProduct getDiscountedProductForCustomerId(long customerId, String productCode) {
        DiscountProduct discountProduct = null;
        CustomerRepository customerRepository = new CustomerRepository();
        Customer customer = customerRepository.findById(customerId);
        PriceList pl = new PriceList(customerRepository.findById(customer.getId()));

        List<EDiscountProduct> discountedProducts = EDiscountProduct.find(EDiscountProduct.class, "customer = ? and product_code = ?", String.valueOf(customerId), productCode);
        if (discountedProducts.size() > 0) {
            discountProduct = toBusinessObject(discountedProducts.get(0));
        }

        return discountProduct;
    }
}
