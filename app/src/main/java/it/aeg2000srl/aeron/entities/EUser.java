package it.aeg2000srl.aeron.entities;

import com.orm.SugarRecord;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class EUser extends SugarRecord<EUser> {
    String username;
    String password;
    boolean locked;
}
