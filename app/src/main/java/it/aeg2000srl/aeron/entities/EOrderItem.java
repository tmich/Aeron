package it.aeg2000srl.aeron.entities;

import com.orm.SugarRecord;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class EOrderItem extends SugarRecord<EOrderItem> {
    public int quantity;
    public String discount;
    public String notes;

    // relationship
    public EOrder eOrder;
    public EProduct eProduct;
}
