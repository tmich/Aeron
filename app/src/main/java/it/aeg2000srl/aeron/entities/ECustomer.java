package it.aeg2000srl.aeron.entities;

import com.orm.SugarRecord;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class ECustomer extends SugarRecord<ECustomer> {
    public String code;
    public String name;
    public String address;
    public String cap;
    public String city;
    public String province;
    public String telephone;
    public String iva;
}
