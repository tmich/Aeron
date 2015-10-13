package it.aeg2000srl.aeron.entities;

import com.orm.SugarRecord;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class EUser extends SugarRecord<EUser> {
    public String username;
    public String password;
    public String code;
    public boolean locked;
}
