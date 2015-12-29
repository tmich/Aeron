package it.aeg2000srl.aeron.entities;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by tiziano.michelessi on 18/11/2015.
 */
public class ECart extends SugarRecord<ECart> {
    // relationship
    public ECustomer customer;
    public List<EOrderItem> items;
}
