package it.aeg2000srl.aeron.views;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.aeg2000srl.aeron.R;
import it.aeg2000srl.aeron.core.Customer;
import it.aeg2000srl.aeron.core.FavoriteProduct;
import it.aeg2000srl.aeron.core.IOrder;
import it.aeg2000srl.aeron.core.IOrderItem;
import it.aeg2000srl.aeron.core.IOrderSender;
import it.aeg2000srl.aeron.core.Order;
import it.aeg2000srl.aeron.core.OrderItem;
import it.aeg2000srl.aeron.core.OrderSender;
import it.aeg2000srl.aeron.core.Product;
import it.aeg2000srl.aeron.repositories.CustomerRepository;
import it.aeg2000srl.aeron.repositories.FavoriteProductRepository;
import it.aeg2000srl.aeron.repositories.OrderRepository;
import it.aeg2000srl.aeron.repositories.ProductRepository;
import it.aeg2000srl.aeron.views.adapters.OrderItemsArrayAdapter;

public class OrderActivity extends AppCompatActivity {
    CustomerRepository customerRepository;
    OrderRepository orderRepository;
    IOrder order;
    TextView txtCustomerName;
    FloatingActionButton btnAddItem;
    ListView lstItems;
    ImageButton btnDeleteItem;
    ProgressDialog barProgressDialog;
    Handler updateBarHandler;

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
        lstItems.setAdapter(new OrderItemsArrayAdapter(this, new ArrayList<IOrderItem>()));

        if (getIntent() != null) {
            Intent intent = getIntent();
            if (intent.getAction() != null) {
                if (intent.getAction().equals(getString(R.string.actionNewOrder))) {
                    // creazione nuovo ordine
                    long customerId = intent.getLongExtra(getString(R.string.customerId), 0);
                    Customer customer = customerRepository.findById(customerId);
                    order = new Order(customer);

                    // verifico se c'è un carrello valorizzato
                    ArrayList<String> cart = intent.getStringArrayListExtra(getString(R.string.cart));
                    if (cart != null) {
                        // aggiungo i prodotti all'ordine
                        for (String productCode : cart) {
                            ProductRepository productRepository = new ProductRepository();
                            Product product = productRepository.findByCode(productCode);

                            order.add(product, 1, "", "");
                        }
                        updateItems();
                    }
                } else {
                    // modifica di un ordine salvato
                    long orderId = intent.getLongExtra(getString(R.string.orderId), 0);
                    order = orderRepository.findById(orderId);
                    updateItems();
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
                final IOrderItem orderItem = getItemsAdapter().getItem(i);
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
                                            order.remove((OrderItem)orderItem);
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
                final OrderItem item = (OrderItem)getItemsAdapter().getItem(i);
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
            String productCode = data.getStringExtra(getString(R.string.productCode));
            int qty = data.getIntExtra(getString(R.string.quantity), 1);
            String notes = data.getStringExtra(getString(R.string.notes));
            String discount = data.getStringExtra(getString(R.string.discount));
            ProductRepository productRepository = new ProductRepository();
            Product product = productRepository.findByCode(productCode);

            order.add(product, qty, notes, discount);
            updateItems();
//            Toast.makeText(this, String.valueOf(order.getItems().size()), Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(this, "annullato", Toast.LENGTH_SHORT).show();
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
                long newId = save();
                showMessage("Ordine n. " + newId + " salvato");
                finish();
            }
            else {
                showMessage("Inserire almeno un prodotto");
            }
        }
        else if (id == R.id.action_send) {
            if (order.getItems().size() > 0) {
                long newId = save();
                send();
            }
            else {
                showMessage("Inserire almeno un prodotto");
            }
        }

        return super.onOptionsItemSelected(item);
    }

    protected OrderItemsArrayAdapter getItemsAdapter() {
        return (OrderItemsArrayAdapter)lstItems.getAdapter();
    }

    protected long save() {
        long newId = 0;
        try {
            newId = orderRepository.add(order);
            order.setId(newId);
        } catch (Exception exc) {
            showMessage(exc.toString());
        }
        return newId;
    }

    protected void send() {
        barProgressDialog = new ProgressDialog(OrderActivity.this);
        barProgressDialog.setTitle(getString(R.string.title_activity_order));
        barProgressDialog.setMessage(getString(R.string.please_wait));
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        barProgressDialog.setProgress(0);
        barProgressDialog.show();
        updateBarHandler = new Handler();
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String url = SP.getString("pref_default_api_url", getString(R.string.test_url));
        new OrderSender(url, order).execute("");
    }

    protected void updateItems() {
        getItemsAdapter().clear();
        getItemsAdapter().addAll(order.getItems());
        getItemsAdapter().notifyDataSetChanged();
    }

    protected void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

//    public void deleteItem(View view) {
//        ImageButton btn = (ImageButton)view;
//        OrderItem orderItem = (OrderItem)btn.getTag();
//        order.remove(orderItem);
//        updateItems();
//    }


    /***********************************************************************************************/
    /****************                           ASNYC TASK                          ****************/
    /***********************************************************************************************/
    class OrderSender extends AsyncTask<String, Integer, Integer> {
        String _url;
        int CONN_TIMEOUT = 10000;
        IOrder order;
        Exception exception;

        public OrderSender(String url, IOrder order) {
            _url = url;
            this.order = order;
        }

        private String readStream(InputStream in) {
            BufferedReader reader = null;
            String ret = "";

            try {
                reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line = "";

                // Read Server Response
                while((line = reader.readLine()) != null)
                {
                    // Append server response in string
                    sb.append(line);
                    sb.append("");
                }

                // Append Server Response To Content String
                ret = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
                exception = e;
            }
            finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return ret;
        }

        protected void onPostExecute(Integer result) {
            barProgressDialog.dismiss();

            if(exception != null) {
                showError(exception);
                return;
            }

            if (result == 0) {
                showError(new JSONException(""));
                return;
            }

            order.setSentDate(new Date());
            orderRepository.add(order);

            // aggiungo i prodotti ordinati ai preferiti
            Customer customer = customerRepository.findById(order.getCustomerId());

            List<FavoriteProduct> favoriteProductList = new ArrayList<>(order.getItems().size());
            for(IOrderItem orderItem : order.getItems()) {
                FavoriteProduct favoriteProduct = new FavoriteProduct(orderItem.getProductName(),
                        orderItem.getProductCode(), orderItem.getPrice(), orderItem.getProductId());
                favoriteProduct.setCustomer(customer);
                favoriteProduct.setHits(favoriteProduct.getHits() + orderItem.getQuantity());
                favoriteProductList.add(favoriteProduct);
            }

            FavoriteProductRepository favoriteProductRepository = new FavoriteProductRepository();
            favoriteProductRepository.addAll(favoriteProductList);

            showMessage("Ordine n. " + result + " inviato");
            finish();
        }

        private void showError(Exception e) {
            try {
                throw e;
            }
            catch (IOException ex) {
                showMessage("Errore di connessione");
            }
            catch (JSONException ex) {
                showMessage("Ricevuti dati non validi");
            }
            catch (Exception ex) {
                showMessage("Errore sconosciuto");
            }
        }

        @Override
        protected Integer doInBackground(String... strings) {
            HttpURLConnection conn = null;
            String Content;
            int serverAssignedId = 0;

//        if (user == null) {
//            throw new SecurityException("utente non riconosciuto");
//        }

            try {
                // Send GET data request
                URL url = new URL(_url + "/orders/new" );
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setConnectTimeout(CONN_TIMEOUT);
                conn.setReadTimeout(20000);
                conn.setRequestMethod("POST");

                JSONObject json = new JSONObject();
                json.put("user_id", 1);
                json.put("customer_code", order.getCustomerCode());
                json.put("type", order.getType() == IOrder.OrderType.NORMAL ? "O" : "I");
                JSONArray voci = new JSONArray();

                for(IOrderItem orderItem : order.getItems()) {
                    JSONObject obj = new JSONObject();
                    obj.put("product_code", orderItem.getProductCode());
                    obj.put("qty", orderItem.getQuantity());
                    obj.put("notes", orderItem.getNotes());
                    voci.put(obj);
                }

                json.put("items", voci);

                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                osw.write(json.toString());
                osw.flush();

                InputStream in = new BufferedInputStream(conn.getInputStream());
                Content = readStream(in);
                JSONObject resp = new JSONObject(Content);
                serverAssignedId = resp.getInt("id");
                conn.disconnect();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                conn = null;
            }

            return serverAssignedId;
        }
    }

}
