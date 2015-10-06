package it.aeg2000srl.aeron;

import android.test.AndroidTestCase;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import it.aeg2000srl.aeron.core.Product;
import it.aeg2000srl.aeron.repositories.ProductRepository;

/**
 * Created by tiziano.michelessi on 06/10/2015.
 */
public class ProductRepositoryTest extends AndroidTestCase {
    ProductRepository repo;
    String code;

    public void setUp() {
        repo = new ProductRepository();
        code = UUID.randomUUID().toString().substring(3, 8);
    }

    public void testShouldAddNewProduct() {
        Product p = new Product("Billi KOXXO ballo bello bollo", code, 34.80);
        long new_id = repo.add(p);
        assertTrue(new_id > 0);
        Product p1 = repo.findByCode(code);
        assertNotNull(p1);
        assertEquals(new_id, p1.getId());
    }

    public void testShouldRetrieveExistingProduct() {
        assertNotNull(repo.findById(1));
    }

    public void testShouldRetrieveAllProducts() {
        ArrayList<Product> products = (ArrayList<Product>) repo.getAll();
        assertEquals(repo.size(), products.size());
    }

    public void testShouldSaveBulkProducts() {
        long sz = repo.size();
        int bulk_n = 100;
        ArrayList<Product> bulk = new ArrayList<>();
        for (int i=0; i<bulk_n; i++) {
            bulk.add(new Product("Test Bulk save n." + i, UUID.randomUUID().toString().substring(4, 9), new Random().nextDouble()));
        }
        repo.addAll(bulk);

        assertTrue(repo.size() > sz);
    }

    public void testShouldReplaceValuesOnConflict() {
        ArrayList<Product> bulk = new ArrayList<>();
        Product p1 = repo.findById(new Random().nextInt((int)repo.size()));
        String code = p1.getCode();
        p1.setName(p1.getName() + " - MODIFICATO");
        bulk.add(p1);
        repo.addAll(bulk);

        assertTrue(repo.findByCode(code).getName().contains(" - MODIFICATO"));
    }
}
