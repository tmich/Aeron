package it.aeg2000srl.aeron.views;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import it.aeg2000srl.aeron.R;
import it.aeg2000srl.aeron.services.UseCasesService;

public class MainActivity extends AppCompatActivity {

    View btnGoToProducts;
    View btnGoToCustomers;
    ListView lstWaitingOrders;
    UseCasesService useCasesService;
    TextView txtVersion;
    View btnGoToWaitingOrders;

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

        btnGoToWaitingOrders = findViewById(R.id.goToWaitingOrders);
        btnGoToWaitingOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToWaitingOrdersIntent = new Intent(MainActivity.this, WaitingOrdersAllActivity.class);
                startActivity(goToWaitingOrdersIntent);
            }
        });

        txtVersion = (TextView)findViewById(R.id.txtVersion);

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            txtVersion.setText("v. " + String.valueOf(packageInfo.versionName));
        } catch (PackageManager.NameNotFoundException exc) {
            txtVersion.setText("ND");
        }
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

    @Override
    protected void onResume() {
        super.onResume();

        // ordini in attesa
        UseCasesService service = new UseCasesService();
        int countOrdiniInAttesa = service.getWaitingOrders().size();
        Button btn = (Button)btnGoToWaitingOrders;

        btn.setEnabled(countOrdiniInAttesa > 0);
        btn.setText(R.string.ordini_in_attesa);
        //btn.setTextColor(Color.BLACK);

        if(countOrdiniInAttesa > 0) {
            //btn.setTextColor(Color.RED);
            btn.setText(btn.getText() + " (" + countOrdiniInAttesa + ")");
        }
    }

}
