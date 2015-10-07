package it.aeg2000srl.aeron.services;

import java.util.List;

import it.aeg2000srl.aeron.core.Customer;
import it.aeg2000srl.aeron.core.Order;
import it.aeg2000srl.aeron.repositories.OrderRepository;

/**
 * Created by tiziano.michelessi on 07/10/2015.
 */
public class UseCasesService {
    public List<Order> getOrdersByCustomer(Customer customer) {
        OrderRepository orderRepository = new OrderRepository();
        return orderRepository.findByCustomerId(customer.getId());
    }

    public List<Order> getWaitingOrders() {
        OrderRepository orderRepository = new OrderRepository();
        return orderRepository.getNotSent();
    }
}
