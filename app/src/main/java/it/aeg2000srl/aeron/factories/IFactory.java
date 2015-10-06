package it.aeg2000srl.aeron.factories;

import com.orm.SugarRecord;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public interface IFactory<fromT extends SugarRecord, toT> {
    IFactory from(fromT from);
    toT make();
}
