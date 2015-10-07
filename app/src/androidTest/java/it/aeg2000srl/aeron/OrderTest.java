package it.aeg2000srl.aeron;

import android.test.AndroidTestCase;

import java.util.Date;

import it.aeg2000srl.aeron.core.Customer;
import it.aeg2000srl.aeron.core.Order;
import it.aeg2000srl.aeron.core.Product;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class OrderTest extends AndroidTestCase {
    public void setUp() {}

    public void testShouldCreateNewOrder() {
        Customer c = new Customer("PROVA", "via della prova", "VERONA");
        Order o = new Order(c);
        Product p = new Product("Prodotto di test", "ZAZAZ", 10.90);

        o.add(p, 4, "ciao", null);
        assertEquals(o.getItems().size(), 1);
    }

    public void testShouldNotAddAProductTwice() {
        Customer c = new Customer("PROVONA", "via della provona", "VENEZIA");
        Order o = new Order(c);
        Product p = new Product("Un altro prodotto di test", "JKLKLJK", 20.60);
        p.setId(100);

        Product p1 = new Product("Un ennesimo prodotto di test", "POPOJ", 1.70);
        p1.setId(101);

        o.add(p, 5, "TEST", null);
        o.add(p, 1, "NOOOO", null);
        o.add(p1, 3, null, null);
        o.add(p1, 1, "NOOOO", null);

        assertEquals(o.getItems().size(), 2);
    }

    public void testNewOrderShouldHaveNullSentDate() {
        Customer c = new Customer("PROVONA", "via della provona", "VENEZIA");
        Order o = new Order(c);
        assertFalse(o.hasBeenSent());
    }
}
