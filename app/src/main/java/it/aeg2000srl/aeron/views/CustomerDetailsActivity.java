package it.aeg2000srl.aeron.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import it.aeg2000srl.aeron.R;
import it.aeg2000srl.aeron.core.Customer;
import it.aeg2000srl.aeron.core.FavoriteProduct;
import it.aeg2000srl.aeron.repositories.CustomerRepository;
import it.aeg2000srl.aeron.services.UseCasesService;
import it.aeg2000srl.aeron.views.adapters.FavoriteProductsArrayAdapter;

public class CustomerDetailsActivity extends AppCompatActivity {
    Customer customer;
    CustomerRepository customerRepository;
    TextView lblCustomerName;
    TextView lblCustomerAddress;
    TextView lblCustomerCity;
    Button btnNewOrder;
    Button btnNewOrderIcewer;
    Button btnPriceList;
//    Button btnWaitingOrders;
    ListView lstFavorites;
    UseCasesService service;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);
        customerRepository = new CustomerRepository();
        lblCustomerName = (TextView)findViewById(R.id.lblCustomerName);
        lblCustomerAddress = (TextView)findViewById(R.id.lblCustomerAddress);
        lblCustomerCity = (TextView)findViewById(R.id.lblCustomerCity);
        btnNewOrder = (Button)findViewById(R.id.btnNewOrder);
        btnNewOrderIcewer = (Button)findViewById(R.id.btnNewOrderIcewer);
        btnPriceList = (Button)findViewById(R.id.btnPriceList);
//        btnWaitingOrders = (Button)findViewById(R.id.btnWaitingOrders);
        lstFavorites = (ListView)findViewById(R.id.lstFavorites);
        service = new UseCasesService();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (getIntent() != null) {
            Intent intent = getIntent();
            long customerId = intent.getLongExtra(getString(R.string.customerId), 0);
            customer = customerRepository.findById(customerId);
            update();
        } else {
            finish();
        }

        // creazione nuovo ordine
        btnNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerDetailsActivity.this, OrderActivity.class);
                intent.setAction(getString(R.string.actionNewOrder));
                intent.putExtra(getString(R.string.customerId), customer.getId());

                // preferiti selezionati
                ArrayList<String> selectedCodes = new ArrayList<>();
                for (FavoriteProduct fav : getFavoritesAdapter().getFavorites()) {
                    if (fav.isSelected()) {
                        selectedCodes.add(fav.getCode());
                    }
                }
                intent.putStringArrayListExtra(getString(R.string.cart), selectedCodes);
                startActivity(intent);
            }
        });

        // creazione nuovo ordine Icewer
        btnNewOrderIcewer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerDetailsActivity.this, OrderIcewerActivity.class);
                intent.setAction(getString(R.string.actionNewOrder));
                intent.putExtra(getString(R.string.customerId), customer.getId());
                startActivity(intent);
            }
        });

        // listino cliente
        btnPriceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerDetailsActivity.this, PriceListActivity.class);
                intent.putExtra(getString(R.string.customerId), customer.getId());
                startActivity(intent);
            }
        });

        // lista prodotti preferiti
        lstFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FavoriteProduct fav = getFavoritesAdapter().getItem(i);
                fav.setSelected(!fav.isSelected());
                ((CheckBox)view.findViewById(R.id.checkBox1)).setChecked(fav.isSelected());
            }
        });

    }

    protected void update() {
        lblCustomerName.setText(customer.getName());
        lblCustomerAddress.setText(customer.getAddress());
        lblCustomerCity.setText(customer.getCity());

        // lista prodotti preferiti
        lstFavorites.setAdapter(new FavoriteProductsArrayAdapter(this, service.getFavorites(customer)));

        //ordini in attesa
//        btnWaitingOrders.setEnabled(service.getWaitingOrders(customer).size() > 0);
        updateMenuActions();
    }

    void updateMenuActions() {
        if (this.menu != null) {
            MenuItem menuItem = this.menu.findItem(R.id.action_orders_waiting);
            menuItem.setVisible(service.getWaitingOrders(customer).size() > 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }

    protected FavoriteProductsArrayAdapter getFavoritesAdapter() {
        return (FavoriteProductsArrayAdapter)lstFavorites.getAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_customer_details, menu);
        this.menu = menu;

        updateMenuActions();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_orders_history) {
            Intent intent = new Intent(CustomerDetailsActivity.this, OrdersHistoryActivity.class);
            intent.putExtra(getString(R.string.customerId), customer.getId());
            startActivity(intent);
        } else if (id == R.id.action_orders_waiting) {
            Intent intent = new Intent(CustomerDetailsActivity.this, WaitingOrdersActivity.class);
            intent.putExtra(getString(R.string.customerId), customer.getId());
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
