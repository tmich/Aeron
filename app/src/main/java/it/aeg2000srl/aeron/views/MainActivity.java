package it.aeg2000srl.aeron.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import it.aeg2000srl.aeron.R;

public class MainActivity extends AppCompatActivity {

    Button btnGoToProducts;
    Button btnGoToCustomers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGoToProducts = (Button)findViewById(R.id.goToProducts);
        btnGoToProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToProductsIntent = new Intent(MainActivity.this, ProductsActivity.class);
                startActivity(goToProductsIntent);
            }
        });

        btnGoToCustomers = (Button)findViewById(R.id.goToCustomers);
        btnGoToCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToCustomersIntent = new Intent(MainActivity.this, CustomersActivity.class);
                startActivity(goToCustomersIntent);
            }
        });
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
