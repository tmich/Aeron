package it.aeg2000srl.aeron;

import android.test.AndroidTestCase;

import it.aeg2000srl.aeron.core.Customer;
import it.aeg2000srl.aeron.core.Order;
import it.aeg2000srl.aeron.core.Product;
import it.aeg2000srl.aeron.repositories.CustomerRepository;
import it.aeg2000srl.aeron.repositories.OrderRepository;
import it.aeg2000srl.aeron.repositories.ProductRepository;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class OrderRepositoryTest extends AndroidTestCase {
    OrderRepository repo;

    public void setUp() {
        repo = new OrderRepository();
    }

    public void testShouldCreateNewOrder() {
        CustomerRepository customerRepository = new CustomerRepository();
        Customer customer = customerRepository.getAll().get(0);
        ProductRepository productRepository = new ProductRepository();
        Product p1 = productRepository.getAll().get(1);
        Product p3 = productRepository.getAll().get(3);
        Product p5 = productRepository.getAll().get(5);

        Order order = new Order(customer);
        order.add(p1, 3, "prova 1", null);
        order.add(p3, 2, "prova 3", null);
        order.add(p5, 1, "prova 5", "1 gratis");

        OrderRepository orderRepository = new OrderRepository();
        long orderId = orderRepository.add(order);
        assertEquals(orderRepository.findById(orderId).getItems().size(), 3);
    }
}
