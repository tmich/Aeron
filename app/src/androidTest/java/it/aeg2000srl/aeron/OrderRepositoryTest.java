package it.aeg2000srl.aeron;

import android.test.AndroidTestCase;

import it.aeg2000srl.aeron.core.Customer;
import it.aeg2000srl.aeron.core.Order;
import it.aeg2000srl.aeron.core.OrderItem;
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
        Product p1 = productRepository.findById(1);
        Product p3 = productRepository.findById(3);
        Product p5 = productRepository.findById(8);

        Order order = new Order(customer);
        order.add(p1, 3, "prova 1", null);
        order.add(p3, 2, "prova 3", null);
        order.add(p5, 1, "prova 5", "1 gratis");

        OrderRepository orderRepository = new OrderRepository();
        long orderId = orderRepository.add(order);
        assertEquals(orderRepository.findById(orderId).getItems().size(), 3);

        for(OrderItem item : orderRepository.findById(orderId).getItems()) {
            assertTrue(item.getProductId() == 1 || item.getProductId() == 3 || item.getProductId() == 8);
        }
    }
}
