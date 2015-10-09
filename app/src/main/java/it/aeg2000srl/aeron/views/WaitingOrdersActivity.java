package it.aeg2000srl.aeron.views;

import android.content.DialogInterface;
import android.content.Intent;
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
import it.aeg2000srl.aeron.core.IOrder;
import it.aeg2000srl.aeron.core.Order;
import it.aeg2000srl.aeron.repositories.CustomerRepository;
import it.aeg2000srl.aeron.repositories.OrderRepository;
import it.aeg2000srl.aeron.services.UseCasesService;
import it.aeg2000srl.aeron.views.adapters.OrdersArrayAdapter;

public class WaitingOrdersActivity extends AppCompatActivity {
    ListView lstWaitingOrders;
    UseCasesService service;
    Customer customer;
    CustomerRepository customerRepository;
    TextView txtCustomerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_orders);

        lstWaitingOrders = (ListView)findViewById(R.id.lstWaitingOrders);
        service = new UseCasesService();
        customerRepository = new CustomerRepository();
        txtCustomerName = (TextView)findViewById(R.id.txtCustomerName);

        if (getIntent() != null) {
            Intent intent = getIntent();
            if (intent.hasExtra(getString(R.string.customerId))) {
                long customerId = intent.getLongExtra(getString(R.string.customerId), 0);
                customer = customerRepository.findById(customerId);
                lstWaitingOrders.setAdapter(new OrdersArrayAdapter(this, R.layout.orders_waiting, new ArrayList<IOrder>()));
            }
        } else {
            finish();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        lstWaitingOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                IOrder order = getWaitingOrdersAdapter().getItem(i);

                // modifica di un ordine salvato
                Intent intent = new Intent(WaitingOrdersActivity.this, OrderActivity.class);
                intent.setAction(getString(R.string.actionEditOrder));
                intent.putExtra(getString(R.string.orderId), order.getId());
                startActivity(intent);
            }
        });

        lstWaitingOrders.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final OrderRepository orderRepository = new OrderRepository();
                final Order order = (Order)getWaitingOrdersAdapter().getItem(i);

                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(WaitingOrdersActivity.this, view);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu_order_item, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
//                        Toast.makeText(OrderActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        if (item.getItemId() == R.id.deleteOrderItem) {
                            new AlertDialog.Builder(WaitingOrdersActivity.this)
                                    .setTitle("Conferma")
                                    .setMessage("Eliminare l'ordine?")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
//                                            Toast.makeText(OrderActivity.this, "Yaay", Toast.LENGTH_SHORT).show();
                                            orderRepository.remove(order);
                                            update();
                                        }})
                                    .setNegativeButton(android.R.string.no, null).show();
                        }
                        return true;
                    }
                });
                popup.show();
                return true;
            }
        });

        update();
    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }

    protected void update() {
        txtCustomerName.setText(customer.getName());
        lstWaitingOrders.setAdapter(new OrdersArrayAdapter(this, R.layout.orders_waiting, service.getWaitingOrders(customer)));
    }

    protected OrdersArrayAdapter getWaitingOrdersAdapter() {
        return (OrdersArrayAdapter)lstWaitingOrders.getAdapter();
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
