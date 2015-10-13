package it.aeg2000srl.aeron.entities;

import com.orm.SugarRecord;

/**
 * Created by tiziano.michelessi on 13/10/2015.
 */
public class EFavoriteProduct extends SugarRecord<EFavoriteProduct> implements Sinchronizable {
    public String productCode;
    public String customerCode;
    public int hits;
    protected boolean synced;

    @Override
    public boolean isSynchronized() {
        return synced;
    }

    @Override
    public void setSynchronized(boolean sync) {
        synced = sync;
    }
}
