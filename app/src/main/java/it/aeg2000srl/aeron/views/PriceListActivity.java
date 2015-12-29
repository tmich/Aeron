package it.aeg2000srl.aeron.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import it.aeg2000srl.aeron.R;
import it.aeg2000srl.aeron.core.Customer;
import it.aeg2000srl.aeron.core.DiscountProduct;
import it.aeg2000srl.aeron.core.IProduct;
import it.aeg2000srl.aeron.core.PriceList;
import it.aeg2000srl.aeron.core.Product;
import it.aeg2000srl.aeron.repositories.CustomerRepository;
import it.aeg2000srl.aeron.repositories.PriceListRepository;
import it.aeg2000srl.aeron.repositories.ProductRepository;
import it.aeg2000srl.aeron.views.adapters.DiscountProductsArrayAdapter;

public class PriceListActivity extends AppCompatActivity {
    TextView txtCustomerName;
    Customer customer;
    ListView lstPriceList;
    FloatingActionButton btnAddDiscount;
    PriceListRepository priceListRepository;

    static int PICK_PRODUCT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_list);
        txtCustomerName = (TextView)findViewById(R.id.txtCustomerName);
        lstPriceList = (ListView)findViewById(R.id.lstPriceList);
        lstPriceList.setAdapter(new DiscountProductsArrayAdapter(this, new ArrayList<DiscountProduct>()));
        lstPriceList.setEmptyView(findViewById(R.id.empty_list));
        btnAddDiscount = (FloatingActionButton)findViewById(R.id.btnAddDiscount);
        priceListRepository = new PriceListRepository();

        if (getIntent() != null) {
            Intent intent = getIntent();
            if (intent.hasExtra(getString(R.string.customerId))) {
                CustomerRepository customerRepository = new CustomerRepository();
                long customerId = intent.getLongExtra(getString(R.string.customerId), 0);
                customer = customerRepository.findById(customerId);
                update();
            }
        } else {
            finish();
        }

        btnAddDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PriceListActivity.this, ProductsActivity.class);
                intent.setAction(getString(R.string.PICK_PRODUCT_FOR_DISCOUNT));
                startActivityForResult(intent, PICK_PRODUCT);
            }
        });

        lstPriceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                IProduct discountedProduct = getAdapter().getItem(i);
                ProductRepository productRepository = new ProductRepository();
                Product product = productRepository.findByCode(discountedProduct.getCode());
                showDiscountDialog(product);
            }
        });

        lstPriceList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final DiscountProduct discountProduct = getAdapter().getItem(i);
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(PriceListActivity.this, view);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu_pricelist, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.deleteDiscount) {
                            new AlertDialog.Builder(PriceListActivity.this)
                                    .setTitle("Conferma")
                                    .setMessage("Eliminare?")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            priceListRepository.remove(discountProduct);
                                            update();
                                        }})
                                    .setNegativeButton(R.string.no, null).show();
                        }
                        return true;
                    }
                });
                popup.show();
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == PICK_PRODUCT && resultCode == RESULT_OK) {
            String productCode = data.getStringExtra(getString(R.string.productCode));
            ProductRepository productRepository = new ProductRepository();
            Product product = productRepository.findByCode(productCode);

            showDiscountDialog(product);
        }
    }

    protected void showDiscountDialog(final Product product) {
        final DiscountedProductDialog dialog = new DiscountedProductDialog(this, product, customer);
        dialog.setOnOkListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double originalPrice = product.getPrice();
                DiscountProduct discountProduct = new DiscountProduct(product.getCode(), product.getName(), originalPrice, dialog.getDiscount());
                discountProduct.setCustomerId(customer.getId());

                DiscountProduct existingDiscountProduct = priceListRepository.getDiscountedProductForCustomerId(customer.getId(), product.getCode());
                if (existingDiscountProduct != null) {
                    discountProduct.setId(existingDiscountProduct.getId());
                }

                priceListRepository.add(discountProduct);
                dialog.dismiss();
                update();
            }
        });

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_price_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void update() {
//        txtCustomerName.setText(customer.getName());
        setTitle(getString(R.string.title_activity_price_list)+ " " + customer.getName());
        PriceListRepository priceListRepository = new PriceListRepository();
        PriceList pl = priceListRepository.getPriceListForCustomerId(customer.getId());
        getAdapter().clear();
        getAdapter().addAll(pl.getProducts());
        getAdapter().notifyDataSetChanged();
    }

    protected DiscountProductsArrayAdapter getAdapter() {
        return (DiscountProductsArrayAdapter)lstPriceList.getAdapter();
    }
}
