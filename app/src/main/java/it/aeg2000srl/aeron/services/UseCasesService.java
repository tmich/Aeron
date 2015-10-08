package it.aeg2000srl.aeron.services;

import java.util.ArrayList;
import java.util.List;

import it.aeg2000srl.aeron.core.Customer;
import it.aeg2000srl.aeron.core.FavoriteProduct;
import it.aeg2000srl.aeron.core.Order;
import it.aeg2000srl.aeron.core.Product;
import it.aeg2000srl.aeron.repositories.OrderRepository;
import it.aeg2000srl.aeron.repositories.ProductRepository;

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

    public List<Order> getWaitingOrders(Customer customer) {
        OrderRepository orderRepository = new OrderRepository();
        return orderRepository.findNotSentByCustomerId(customer.getId());
    }

    public List<Order> getSentOrdersByCustomer(Customer customer) {
        OrderRepository orderRepository = new OrderRepository();
        return orderRepository.findSentByCustomerId(customer.getId());
    }

    public List<FavoriteProduct> getFavorites(Customer customer) {
        List<FavoriteProduct> favoriteProducts;
        ProductRepository productRepository = new ProductRepository();
        List<Product> products = productRepository.getMostOrderedByCustomerId(customer.getId());
        favoriteProducts = new ArrayList<>(products.size());

        for (Product p :
                products) {
            FavoriteProduct fav = new FavoriteProduct(p.getName(), p.getCode(), p.getPrice(), p.getId());
            favoriteProducts.add(fav);
        }

        return favoriteProducts;
    }
}
