package it.aeg2000srl.aeron.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import it.aeg2000srl.aeron.R;
import it.aeg2000srl.aeron.core.Customer;
import it.aeg2000srl.aeron.repositories.CustomerRepository;

public class CustomerDetailsActivity extends AppCompatActivity {
    Customer customer;
    CustomerRepository customerRepository;
    TextView lblCustomerName;
    TextView lblCustomerAddress;
    TextView lblCustomerCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);
        customerRepository = new CustomerRepository();
        lblCustomerName = (TextView)findViewById(R.id.lblCustomerName);
        lblCustomerAddress = (TextView)findViewById(R.id.lblCustomerAddress);
        lblCustomerCity = (TextView)findViewById(R.id.lblCustomerCity);

        if (getIntent() != null) {
            Intent intent = getIntent();
            long customerId = intent.getLongExtra(getString(R.string.customerId), 0);
            customer = customerRepository.findById(customerId);
            update();
        }
    }

    protected void update() {
        lblCustomerName.setText(customer.getName());
        lblCustomerAddress.setText(customer.getAddress());
        lblCustomerCity.setText(customer.getCity());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_customer_details, menu);
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
