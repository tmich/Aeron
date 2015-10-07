package it.aeg2000srl.aeron;

import android.test.AndroidTestCase;

import java.util.List;
import java.util.UUID;

import it.aeg2000srl.aeron.core.Customer;
import it.aeg2000srl.aeron.core.Order;
import it.aeg2000srl.aeron.core.Product;
import it.aeg2000srl.aeron.repositories.CustomerRepository;
import it.aeg2000srl.aeron.repositories.OrderRepository;
import it.aeg2000srl.aeron.repositories.ProductRepository;
import it.aeg2000srl.aeron.services.UseCasesService;

/**
 * Created by tiziano.michelessi on 07/10/2015.
 */
public class UseCasesServiceTest extends AndroidTestCase {
    UseCasesService service;

    public void setUp() {
        service = new UseCasesService();
    }

    public void testShouldReturnManyOrders() {
        CustomerRepository customerRepository = new CustomerRepository();
        ProductRepository productRepository = new ProductRepository();
        OrderRepository orderRepository = new OrderRepository();
        String str = UUID.randomUUID().toString();
        Customer customer = new Customer(str.substring(2,8), str.substring(0,20), str.substring(0,5));
        long newCustomerId = customerRepository.add(customer);
        customer.setId(newCustomerId);

        Product p1 = productRepository.findById(1);
        Order order = new Order(customer);
        order.add(p1, 3, "prova 1", null);
        long id1 = orderRepository.add(order);

        Product p2 = productRepository.findById(10);
        Order order2 = new Order(customer);
        order2.add(p1, 3, "prova 1", null);
        long id2 = orderRepository.add(order2);

        List<Order> orders = service.getOrdersByCustomer(customer);

        for (Order o : orders) {
            assertTrue(o.getId() == id1 || o.getId() == id2);
        }
    }
}
