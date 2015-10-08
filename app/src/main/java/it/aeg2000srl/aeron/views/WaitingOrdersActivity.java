package it.aeg2000srl.aeron.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import it.aeg2000srl.aeron.R;
import it.aeg2000srl.aeron.core.Customer;
import it.aeg2000srl.aeron.core.Order;
import it.aeg2000srl.aeron.repositories.CustomerRepository;
import it.aeg2000srl.aeron.services.UseCasesService;
import it.aeg2000srl.aeron.views.adapters.OrdersArrayAdapter;

public class WaitingOrdersActivity extends AppCompatActivity {
    ListView lstWaitingOrders;
    UseCasesService service;
    Customer customer;
    CustomerRepository customerRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_orders);

        lstWaitingOrders = (ListView)findViewById(R.id.lstWaitingOrders);
        service = new UseCasesService();
        customerRepository = new CustomerRepository();

        if (getIntent() != null) {
            Intent intent = getIntent();
            if (intent.hasExtra(getString(R.string.customerId))) {
                long customerId = intent.getLongExtra(getString(R.string.customerId), 0);
                customer = customerRepository.findById(customerId);
                lstWaitingOrders.setAdapter(new OrdersArrayAdapter(this, R.layout.orders_waiting, new ArrayList<Order>()));
            }
        } else {
            finish();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        update();
    }

    protected void update() {
        lstWaitingOrders.setAdapter(new OrdersArrayAdapter(this, R.layout.orders_waiting, service.getSentOrdersByCustomer(customer)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_waiting_orders, menu);
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
}
