package it.aeg2000srl.aeron;

import android.test.AndroidTestCase;

import java.util.ArrayList;

import it.aeg2000srl.aeron.core.Customer;
import it.aeg2000srl.aeron.core.IOrderItem;
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
        Customer customer = new Customer("Bappo", "via del Fante di Coppe, 345", "Firenze");
        customer.setId(customerRepository.add(customer));
        ProductRepository productRepository = new ProductRepository();
        final Product p1 = new Product("Alici marinate 1KG", "ALICMAR", 8.50);
        final Product p3 = new Product("Sogliole surgelate 2KG", "SOGSUR", 17.80);
        final Product p5 = new Product("Gamberi congelati 1KG", "GAMBCON", 9.90);
        p1.setId(productRepository.add(p1));
        p3.setId(productRepository.add(p3));
        p5.setId(productRepository.add(p5));
        long id1 = p1.getId();
        long id3 = p3.getId();
        long id5 = p5.getId();

        Order order = new Order(customer);
        order.add(p1, 3, "prova 1", null);
        order.add(p3, 2, "prova 3", null);
        order.add(p5, 1, "prova 5", "1 gratis");

        OrderRepository orderRepository = new OrderRepository();
        long orderId = orderRepository.add(order);
        assertEquals(orderRepository.findById(orderId).getItems().size(), 3);

        for(IOrderItem item : orderRepository.findById(orderId).getItems()) {
            assertTrue(item.getProductId() == id1 || item.getProductId() == id3 || item.getProductId() == id5);
        }
    }
}
