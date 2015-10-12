package it.aeg2000srl.aeron;

import android.test.AndroidTestCase;

import java.math.BigDecimal;
import java.math.RoundingMode;

import it.aeg2000srl.aeron.core.Customer;
import it.aeg2000srl.aeron.core.DiscountProduct;
import it.aeg2000srl.aeron.core.PercentDiscount;
import it.aeg2000srl.aeron.core.PriceList;
import it.aeg2000srl.aeron.core.ValueDiscount;

/**
 * Created by tiziano.michelessi on 12/10/2015.
 */
public class DiscountProductsTest extends AndroidTestCase {
    public void testCreateDiscountProduct() {
        DiscountProduct dp1 = new DiscountProduct("CANDEC", "Canaglie decongelate 4kg", 18.70, new PercentDiscount(25));
        double price1 = dp1.getPrice();
        double price2 = new BigDecimal(18.70 - (18.70/100.0) * 25.0).setScale(2, RoundingMode.HALF_UP).doubleValue();
        assertEquals(price1, price2);
    }

    public void testCreatePriceListForCustomer() {
        Customer c1 = new Customer("Il pennuto arrostito", "viale della cazzimarra, 34", "Civita Castellana");
        DiscountProduct dp1 = new DiscountProduct("CONCBRUC", "Conchiglie bruciate 2kg", 8.52, new ValueDiscount(4.5));
        PriceList priceList = new PriceList(c1);
        priceList.add(dp1);
        assertTrue(priceList.getProducts().size() == 1);
    }
}
