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

import it.aeg2000srl.aeron.R;
import it.aeg2000srl.aeron.core.IOrder;
import it.aeg2000srl.aeron.core.Order;
import it.aeg2000srl.aeron.repositories.OrderRepository;
import it.aeg2000srl.aeron.services.UseCasesService;
import it.aeg2000srl.aeron.views.adapters.OrdersArrayAdapter;
import it.aeg2000srl.aeron.views.adapters.WaitingOrdersArrayAdapter;

public class WaitingOrdersAllActivity extends AppCompatActivity {
    ListView lstWaitingOrders;
    UseCasesService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_orders_all);

        lstWaitingOrders = (ListView)findViewById(R.id.lstWaitingOrders);
        service = new UseCasesService();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        lstWaitingOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                IOrder order = getWaitingOrdersAdapter().getItem(i);

                // modifica di un ordine salvato
                Intent intent;
                if (order.getType() == IOrder.OrderType.NORMAL) {
                    intent = new Intent(WaitingOrdersAllActivity.this, OrderActivity.class);
                } else {
                    intent = new Intent(WaitingOrdersAllActivity.this, OrderIcewerActivity.class);
                }
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
                PopupMenu popup = new PopupMenu(WaitingOrdersAllActivity.this, view);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu_order_item, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
//                        Toast.makeText(OrderActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        if (item.getItemId() == R.id.deleteOrderItem) {
                            new AlertDialog.Builder(WaitingOrdersAllActivity.this)
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
        lstWaitingOrders.setAdapter(new WaitingOrdersArrayAdapter(this, R.layout.orders_waiting, service.getWaitingOrders()));
    }

    protected WaitingOrdersArrayAdapter getWaitingOrdersAdapter() {
        return (WaitingOrdersArrayAdapter)lstWaitingOrders.getAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_waiting_orders_all, menu);
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
