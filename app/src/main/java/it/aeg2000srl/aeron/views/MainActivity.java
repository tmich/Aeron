package it.aeg2000srl.aeron.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import it.aeg2000srl.aeron.R;
import it.aeg2000srl.aeron.core.Order;
import it.aeg2000srl.aeron.services.UseCasesService;
import it.aeg2000srl.aeron.views.adapters.OrdersArrayAdapter;

public class MainActivity extends AppCompatActivity {

    View btnGoToProducts;
    View btnGoToCustomers;
    ListView lstWaitingOrders;
    UseCasesService useCasesService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        useCasesService = new UseCasesService();

//        lstWaitingOrders = (ListView)findViewById(R.id.lstWaitingOrders);
//        lstWaitingOrders.setEmptyView(findViewById(R.id.empty_list));
//        lstWaitingOrders.setAdapter(new OrdersArrayAdapter(this, R.layout.orders_waiting, new ArrayList<Order>()));

        btnGoToProducts = findViewById(R.id.goToProducts);
        btnGoToProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToProductsIntent = new Intent(MainActivity.this, ProductsActivity.class);
                startActivity(goToProductsIntent);
            }
        });

        btnGoToCustomers = findViewById(R.id.goToCustomers);
        btnGoToCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToCustomersIntent = new Intent(MainActivity.this, CustomersActivity.class);
                startActivity(goToCustomersIntent);
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        ((OrdersArrayAdapter) lstWaitingOrders.getAdapter()).addAll(useCasesService.getWaitingOrders());
//        ((OrdersArrayAdapter) lstWaitingOrders.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items make the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
