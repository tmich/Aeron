package it.aeg2000srl.aeron.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import it.aeg2000srl.aeron.R;
import it.aeg2000srl.aeron.core.Customer;
import it.aeg2000srl.aeron.core.Order;
import it.aeg2000srl.aeron.core.OrderItem;
import it.aeg2000srl.aeron.core.Product;
import it.aeg2000srl.aeron.repositories.CustomerRepository;
import it.aeg2000srl.aeron.repositories.OrderRepository;
import it.aeg2000srl.aeron.repositories.ProductRepository;
import it.aeg2000srl.aeron.views.adapters.OrderItemsArrayAdapter;

public class OrderActivity extends AppCompatActivity {
    CustomerRepository customerRepository;
    OrderRepository orderRepository;
    Order order;
    TextView txtCustomerName;
    FloatingActionButton btnAddItem;
    ListView lstItems;
    ImageButton btnDeleteItem;

    static int PICK_PRODUCT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        customerRepository = new CustomerRepository();
        orderRepository = new OrderRepository();
        txtCustomerName = (TextView) findViewById(R.id.txtCustomerName);
        btnAddItem = (FloatingActionButton) findViewById(R.id.btnAddItem);
        lstItems = (ListView) findViewById(R.id.lstItems);
        lstItems.setAdapter(new OrderItemsArrayAdapter(this, new ArrayList<OrderItem>()));

        if (getIntent() != null) {
            Intent intent = getIntent();
            if (intent.getAction() != null) {
                if (intent.getAction().equals(getString(R.string.actionNewOrder))) {
                    // creazione nuovo ordine
                    long customerId = intent.getLongExtra(getString(R.string.customerId), 0);
                    Customer customer = customerRepository.findById(customerId);
                    order = new Order(customer);
                } else {
                    // modifica di un ordine salvato
                    long orderId = intent.getLongExtra(getString(R.string.orderId), 0);
                    order = orderRepository.findById(orderId);
                }
            }
        }

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderActivity.this, ProductsActivity.class);
                intent.setAction(getString(R.string.PICK_PRODUCT));
                startActivityForResult(intent, PICK_PRODUCT);
            }
        });

        lstItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final OrderItem orderItem = getItemsAdapter().getItem(i);
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(OrderActivity.this, view);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu_order_item, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
//                        Toast.makeText(OrderActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        if (item.getItemId() == R.id.deleteOrderItem) {
                            new AlertDialog.Builder(OrderActivity.this)
                                    .setTitle("Conferma")
                                    .setMessage("Eliminare il prodotto?")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
//                                            Toast.makeText(OrderActivity.this, "Yaay", Toast.LENGTH_SHORT).show();
                                            order.remove(orderItem);
                                            updateItems();
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

        lstItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final OrderItem item = getItemsAdapter().getItem(i);
                final OrderItemDialog dialog = new OrderItemDialog(OrderActivity.this);
                dialog.setTitle("Modifica");
                dialog.setProductName(item.getProductName());
                dialog.setQuantity(item.getQuantity());
                dialog.setNotes(item.getNotes());
                dialog.setDiscount(item.getDiscount());
                dialog.setOnOkListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        order.remove(item);
                        item.setDiscount(dialog.getDiscount());
                        item.setNotes(dialog.getNotes());
                        item.setQuantity(dialog.getQuantity());
                        item.setProductName(dialog.getProductName());
                        order.add(item);
                        updateItems();
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == PICK_PRODUCT && resultCode == RESULT_OK) {
            long productId = data.getLongExtra(getString(R.string.productId), 0);
            int qty = data.getIntExtra(getString(R.string.quantity), 1);
            String notes = data.getStringExtra(getString(R.string.notes));
            String discount = data.getStringExtra(getString(R.string.discount));
            ProductRepository productRepository = new ProductRepository();
            Product product = productRepository.findById(productId);

            order.add(product, qty, notes, discount);
            updateItems();
            Toast.makeText(this, String.valueOf(order.getItems().size()), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "annullato", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        txtCustomerName.setText(customerRepository.findById(order.getCustomerId()).getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_save) {
            if (order.getItems().size() > 0) {
                save();
            } else {
                showMessage("Inserire almeno un prodotto");
            }
        }

        return super.onOptionsItemSelected(item);
    }

    protected OrderItemsArrayAdapter getItemsAdapter() {
        return (OrderItemsArrayAdapter)lstItems.getAdapter();
    }

    protected void save() {
        try {
            long newId = orderRepository.add(order);
            showMessage("Ordine n. " + newId + " salvato");
            finish();
        } catch (Exception exc) {
            showMessage(exc.toString());
        }
    }

    protected void updateItems() {
        getItemsAdapter().clear();
        getItemsAdapter().addAll(order.getItems());
        getItemsAdapter().notifyDataSetChanged();
    }

    protected void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void deleteItem(View view) {
        ImageButton btn = (ImageButton)view;
        OrderItem orderItem = (OrderItem)btn.getTag();
        order.remove(orderItem);
        updateItems();
    }
}
