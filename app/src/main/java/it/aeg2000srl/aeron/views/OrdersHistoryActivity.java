package it.aeg2000srl.aeron.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;

import it.aeg2000srl.aeron.R;
import it.aeg2000srl.aeron.core.Customer;
import it.aeg2000srl.aeron.core.IOrder;
import it.aeg2000srl.aeron.core.Order;
import it.aeg2000srl.aeron.repositories.CustomerRepository;
import it.aeg2000srl.aeron.services.UseCasesService;
import it.aeg2000srl.aeron.views.adapters.OrdersArrayAdapter;

public class OrdersHistoryActivity extends AppCompatActivity {
    ListView lstSentOrders;
    UseCasesService service;
    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_history);
        lstSentOrders = (ListView)findViewById(R.id.lstSentOrders);
        service = new UseCasesService();

        if (getIntent() != null) {
            CustomerRepository customerRepository = new CustomerRepository();
            long customerId = getIntent().getLongExtra(getString(R.string.customerId), 0);
            if (customerId > 0) {
                customer = customerRepository.findById(customerId);
            } else {
                finish();
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

    private void update() {
        List<IOrder> sentOrdersByCustomer = service.getSentOrdersByCustomer(customer);
        lstSentOrders.setAdapter(new OrdersArrayAdapter(this, R.layout.orders_waiting, sentOrdersByCustomer));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_orders_history, menu);
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
