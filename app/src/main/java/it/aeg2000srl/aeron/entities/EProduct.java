package it.aeg2000srl.aeron.entities;

import com.orm.SugarRecord;
/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class EProduct extends SugarRecord<EProduct> {
    public String code;
    public String name;
    public double price;
}
