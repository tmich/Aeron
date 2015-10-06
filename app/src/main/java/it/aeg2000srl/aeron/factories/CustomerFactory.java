package it.aeg2000srl.aeron.factories;

import android.view.WindowId;

import it.aeg2000srl.aeron.core.Customer;
import it.aeg2000srl.aeron.entities.ECustomer;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class CustomerFactory implements IFactory<ECustomer, Customer> {
    ECustomer entity;

    public static ECustomer toEntity(Customer customer) {
        ECustomer entity = new ECustomer();
        entity.setId(customer.getId() != 0 ? customer.getId() : null);
        entity.name = customer.getName();
        entity.code = customer.getCode();
        entity.address = customer.getAddress();
        entity.telephone = customer.getTelephone();
        entity.cap = customer.getCap();
        entity.code = customer.getCode();
        entity.iva = customer.getIva();
        entity.province = customer.getProvince();

        return entity;
    }

    @Override
    public CustomerFactory from(ECustomer entity) {
        this.entity = entity;
        return this;
    }

    @Override
    public Customer make() {
        Customer customer = new Customer(entity.name, entity.address, entity.city);
        customer.setId(entity.getId());
        customer.setTelephone(entity.telephone);
        customer.setCap(entity.cap);
        customer.setCode(entity.code);
        customer.setIva(entity.iva);
        customer.setProvince(entity.province);

        return customer;
    }
}
