package it.aeg2000srl.aeron.repositories;

import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import it.aeg2000srl.aeron.core.Customer;
import it.aeg2000srl.aeron.core.IOrder;
import it.aeg2000srl.aeron.entities.ECustomer;
import it.aeg2000srl.aeron.factories.CustomerFactory;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class CustomerRepository implements IRepository<Customer> {
    CustomerFactory fact;

    public CustomerRepository() {
        fact = new CustomerFactory();
    }

    @Override
    public Customer findById(long id) {
        try {
            return fact.from(ECustomer.findById(ECustomer.class, id)).make();
        } catch (Exception exc) {
            return null;
        }
    }

    public Customer findByCode(String code) {
        if (code != null) {
            if (ECustomer.find(ECustomer.class, "code = ?", code).isEmpty()) {
                return null;
            } else {
                ECustomer customer = ECustomer.find(ECustomer.class, "code = ?", code).get(0);
                return fact.from(customer).make();
            }
        }
        return null;
    }

    public List<Customer> findByName(String name) {
        ArrayList<Customer> products = new ArrayList<>();
        for (ECustomer entity : Select.from(ECustomer.class).where(Condition.prop("name").like("%" + name + "%")).list()) {
            products.add(fact.from(entity).make());
        }
        return products;
    }

    @Override
    public long add(Customer customer) {
        ECustomer entity = CustomerFactory.toEntity(customer);
        entity.save();
        return entity.getId();
    }

    @Override
    public void edit(Customer customer) {
        ECustomer entity = CustomerFactory.toEntity(customer);
        entity.save();
    }

    @Override
    public void remove(Customer customer) {
        ECustomer entity = CustomerFactory.toEntity(customer);
        entity.delete();
    }

    @Override
    public List<Customer> getAll() {
        List<Customer> all = new ArrayList<>();
        for (ECustomer entity : ECustomer.listAll(ECustomer.class)) {
            all.add(fact.from(entity).make());
        }
        return all;
    }

    @Override
    public long size() {
        return ECustomer.count(ECustomer.class, null, null);
    }

    @Override
    public void addAll(List<Customer> items) {
        List<ECustomer> entities = new ArrayList<>();

        for(Customer customer : items) {
            ECustomer entity = CustomerFactory.toEntity(customer);

            // check existance of customer
            Customer customer1 = findByCode(customer.getCode());
            if (customer1 != null) {
                entity.setId(customer1.getId());
            }

            entities.add(entity);
        }

        ECustomer.saveInTx(entities);
    }
}
