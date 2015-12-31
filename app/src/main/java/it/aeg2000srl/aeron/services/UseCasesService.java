package it.aeg2000srl.aeron.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.aeg2000srl.aeron.core.Customer;
import it.aeg2000srl.aeron.core.DiscountProduct;
import it.aeg2000srl.aeron.core.FavoriteProduct;
import it.aeg2000srl.aeron.core.IOrder;
import it.aeg2000srl.aeron.core.Order;
import it.aeg2000srl.aeron.core.Product;
import it.aeg2000srl.aeron.repositories.CustomerRepository;
import it.aeg2000srl.aeron.repositories.FavoriteProductRepository;
import it.aeg2000srl.aeron.repositories.OrderRepository;
import it.aeg2000srl.aeron.repositories.PriceListRepository;
import it.aeg2000srl.aeron.repositories.ProductRepository;

/**
 * Created by tiziano.michelessi on 07/10/2015.
 */
public class UseCasesService {
    int MAX_SIZE = 100;

    public List<IOrder> getOrdersByCustomer(Customer customer) {
        OrderRepository orderRepository = new OrderRepository();
        return orderRepository.findByCustomerId(customer.getId());
    }

    public List<IOrder> getWaitingOrders() {
        OrderRepository orderRepository = new OrderRepository();
        return orderRepository.getNotSent();
    }

    public List<IOrder> getWaitingOrders(Customer customer) {
        OrderRepository orderRepository = new OrderRepository();
        return orderRepository.findNotSentByCustomerId(customer.getId());
    }

    public List<IOrder> getSentOrdersByCustomer(Customer customer) {
        OrderRepository orderRepository = new OrderRepository();
        return orderRepository.findSentByCustomerId(customer.getId());
    }

    public List<FavoriteProduct> getFavorites(Customer customer) {
        List<FavoriteProduct> favoriteProducts;
        FavoriteProductRepository favoriteProductRepository = new FavoriteProductRepository();
        favoriteProducts = favoriteProductRepository.findByCustomerCode(customer.getCode());

        return favoriteProducts;
    }

    public List<Product> getAllProducts() {
        ProductRepository productRepository = new ProductRepository();
        List<Product> all = productRepository.getAll();
        Collections.sort(all, new ProductComparator());
        if (all.size() > MAX_SIZE) {
            return all.subList(0, MAX_SIZE);
        }
        return all;
    }

    public List<Product> findProductsByName(String productName) {
        ProductRepository productRepository = new ProductRepository();
        List<Product> found = productRepository.findByName(productName);
        Collections.sort(found, new ProductComparator());
        if (found.size() > MAX_SIZE) {
            return found.subList(0, MAX_SIZE);
        }
        return found;
    }

    public List<Customer> getAllCustomers() {
        CustomerRepository customerRepository = new CustomerRepository();
        List<Customer> all = customerRepository.getAll();
        Collections.sort(all, new CustomerComparator());
        if (all.size() > MAX_SIZE) {
            return all.subList(0, MAX_SIZE);
        }
        return all;
    }

    public List<Customer> findCustomerByName(String customerName) {
        CustomerRepository customerRepository = new CustomerRepository();
        List<Customer> found = customerRepository.findByName(customerName);
        Collections.sort(found, new CustomerComparator());
        if (found.size() > MAX_SIZE) {
            return found.subList(0, MAX_SIZE);
        }
        return found;
    }

    public DiscountProduct getDiscountedProductForCustomerId(long customerId, String productCode) {
        PriceListRepository priceListRepository = new PriceListRepository();
        return priceListRepository.getDiscountedProductForCustomerId(customerId, productCode);
    }

    public boolean isDiscountedProductForCustomer(long customerId, String productCode) {
        PriceListRepository priceListRepository = new PriceListRepository();
        return priceListRepository.getDiscountedProductForCustomerId(customerId, productCode) != null;
    }
}

class ProductComparator implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {
        String name1 = p1.getName();
        String name2 = p2.getName();
        if (name1 != null && name2 != null) {
            return name1.compareTo(name2);
        }
        return -1;
    }
}

class CustomerComparator implements Comparator<Customer> {
    @Override
    public int compare(Customer c1, Customer c2) {
        String name1 = c1.getName();
        String name2 = c2.getName();
        if (name1 != null && name2 != null) {
            return name1.compareTo(name2);
        }
        return -1;
    }
}
