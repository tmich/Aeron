package it.aeg2000srl.aeron.entities;

import com.orm.SugarRecord;

/**
 * Created by tiziano.michelessi on 12/10/2015.
 */
public class EDiscountProduct extends SugarRecord<EDiscountProduct> implements Sinchronizable {
    public ECustomer customer;
    public String productCode;
    public String productName;
    public double originalPrice;
    public double discountValue;
    public DiscountType type;
    protected boolean synced;

    @Override
    public boolean isSynchronized() {
        return synced;
    }

    @Override
    public void setSynchronized(boolean sync) {
        synced = sync;
    }

    public EDiscountProduct() {
        synced = false;
    }

    public enum DiscountType {
        ValueDiscount,
        PercentDiscount
    }
}
