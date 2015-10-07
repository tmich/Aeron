package it.aeg2000srl.aeron;

import android.test.AndroidTestCase;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import it.aeg2000srl.aeron.core.Customer;
import it.aeg2000srl.aeron.repositories.CustomerRepository;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class CustomerRepositoryTest extends AndroidTestCase {
    CustomerRepository repo;
    String code;
    String rnd;

    public void setUp() {
        repo = new CustomerRepository();
        rnd = UUID.randomUUID().toString();
        code = rnd.substring(0, 5);
    }

    public void testShouldAddNewProduct() {
        Customer customer = new Customer("Gino Pino SrL", "via del Pollo d'oro", "Viterbo");
        customer.setCode(code);
        customer.setProvince(rnd.substring(5, 7));
        customer.setTelephone(rnd.substring(4, 9));
        customer.setIva(rnd.substring(2, 13));
        customer.setCap(rnd.substring(4, 9));
        long new_id = repo.add(customer);
        assertTrue(new_id > 0);
        Customer customer1 = repo.findByCode(code);
        assertNotNull(customer1);
        assertEquals(new_id, customer1.getId());
    }

    public void testShouldRetrieveExistingProduct() {
        assertNotNull(repo.findById(1));
    }

    public void testShouldRetrieveAllProducts() {
        ArrayList<Customer> products = (ArrayList<Customer>) repo.getAll();
        assertEquals(repo.size(), products.size());
    }

    public void testShouldSaveBulkProducts() {
        long sz = repo.size();
        int bulk_n = 500;
        ArrayList<Customer> bulk = new ArrayList<>();
        for (int i=0; i<bulk_n; i++) {
            Customer customer = new Customer("Test Customer Bulk save n." + i, UUID.randomUUID().toString().substring(0, 5), "Roma");
            customer.setProvince(rnd.substring(5, 7));
            customer.setTelephone(rnd.substring(4, 9));
            customer.setIva(rnd.substring(2, 13));
            customer.setCap(rnd.substring(4, 9));
            bulk.add(customer);
        }
        repo.addAll(bulk);

        assertTrue(repo.size() > sz);
    }

    public void testShouldReplaceValuesOnConflict() {
        ArrayList<Customer> bulk = new ArrayList<>();
        String code = rnd.substring(1,6);
        Customer customer = new Customer("PAPPO", "via poppo", "Milano");
        customer.setCode(code);
        customer.setName(customer.getName() + " - MODIFICATO");
        bulk.add(customer);
        repo.addAll(bulk);

        assertTrue(repo.findByCode(code).getName().contains(" - MODIFICATO"));
    }
}
