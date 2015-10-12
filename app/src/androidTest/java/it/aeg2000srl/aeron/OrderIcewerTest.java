package it.aeg2000srl.aeron;

import android.test.AndroidTestCase;

import it.aeg2000srl.aeron.core.Customer;
import it.aeg2000srl.aeron.core.IOrder;
import it.aeg2000srl.aeron.core.IOrderItem;
import it.aeg2000srl.aeron.core.OrderIcewer;
import it.aeg2000srl.aeron.core.Product;
import it.aeg2000srl.aeron.repositories.CustomerRepository;
import it.aeg2000srl.aeron.repositories.OrderRepository;
import it.aeg2000srl.aeron.repositories.ProductRepository;

/**
 * Created by tiziano.michelessi on 12/10/2015.
 */
public class OrderIcewerTest extends AndroidTestCase {
    @Override
    public void setUp() {

    }

    public void testCreateNewOrderIcewer() {
        CustomerRepository customerRepository = new CustomerRepository();
        Customer customer = new Customer("Sbrappo", "via del Fante di Denari, 22", "Spoleto");
        customer.setId(customerRepository.add(customer));

        final Product p1 = new Product("Cagnacci rampanti 1KG", "CAGNRAMP", 8.50);
        final Product p3 = new Product("Bestemmie preconfezionate 2KG", "BESTPREC", 17.80);
        final Product p5 = new Product("Dolori intercostali 1KG", "DOLINT", 9.90);

        IOrder order = new OrderIcewer(customer);
        order.add(p1, 3, "prova 1", null);
        order.add(p3, 2, "prova 3", null);
        order.add(p5, 1, "prova 5", "1 gratis");

        OrderRepository orderRepository = new OrderRepository();
        long orderId = orderRepository.add(order);
        assertEquals(orderRepository.findById(orderId).getItems().size(), 3);
    }
}
