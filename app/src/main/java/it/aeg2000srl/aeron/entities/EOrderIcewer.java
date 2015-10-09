package it.aeg2000srl.aeron.entities;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class EOrderIcewer extends SugarRecord<EOrder> {
    public Date creationDate;
    public String notes;
    public Date sentDate;

    // relationships
    public EUser user;
    public ECustomer customer;
}