package it.aeg2000srl.aeron.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import it.aeg2000srl.aeron.R;
import it.aeg2000srl.aeron.core.PercentDiscount;
import it.aeg2000srl.aeron.core.Product;
import it.aeg2000srl.aeron.repositories.ProductRepository;
import it.aeg2000srl.aeron.services.UseCasesService;
import it.aeg2000srl.aeron.views.adapters.ProductsArrayAdapter;

public class ProductsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    ListView productsList;
    ProgressDialog barProgressDialog;
    Handler updateBarHandler;
    protected ProductRepository repo;
    UseCasesService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        productsList = (ListView)findViewById(R.id.productsList);
        productsList.setEmptyView(findViewById(R.id.empty_list));
        service = new UseCasesService();
        repo = new ProductRepository();

        if(getIntent() != null) {
            Intent intent = getIntent();
            if(intent.getAction() != null) {
                if (intent.getAction().equals(getString(R.string.PICK_PRODUCT))) {
                    setTitle("Selezione prodotto");
                    // Restituisco al chiamante il codice prodotto selezionato
                    productsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            final Product product = getProductsAdapter().getItem(i);
                            final OrderItemDialog dialog = new OrderItemDialog(ProductsActivity.this);
                            dialog.setProductName(product.getName());
                            dialog.setOnOkListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra(getString(R.string.productCode), product.getCode());
                                    returnIntent.putExtra(getString(R.string.quantity), dialog.getQuantity());
                                    returnIntent.putExtra(getString(R.string.notes), dialog.getNotes());
                                    returnIntent.putExtra(getString(R.string.discount), dialog.getDiscount());
                                    dialog.dismiss();
                                    setResult(RESULT_OK, returnIntent);
                                    finish();
                                }
                            });
                            dialog.show();
                        }
                    });
                } else if (intent.getAction().equals(getString(R.string.PICK_PRODUCT_FOR_DISCOUNT))) {
                    setTitle("Selezione prodotto da scontare");
                    productsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            final Product product = getProductsAdapter().getItem(i);
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra(getString(R.string.productCode), product.getCode());
                            setResult(RESULT_OK, returnIntent);
                            finish();
                        }
                    });
                }
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        productsList.setAdapter(new ProductsArrayAdapter(ProductsActivity.this, service.getAllProducts()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_products, menu);

        MenuItem searchViewItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        //searchView.setIconifiedByDefault(false);
        if (searchView != null) {
            searchView.setOnQueryTextListener(this);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_update) {
            updateFromWs();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void updateFromWs() {
        barProgressDialog = new ProgressDialog(ProductsActivity.this);
        barProgressDialog.setTitle(getString(R.string.title_activity_update_products));
        barProgressDialog.setMessage(getString(R.string.please_wait));
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        barProgressDialog.setProgress(0);
        barProgressDialog.show();
        updateBarHandler = new Handler();
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String url = SP.getString("pref_default_api_url", getString(R.string.test_url)) + "/products";
        new DownloadProductsService(updateBarHandler).execute(url);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return searchProduct(query);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return searchProduct(newText);
    }

    protected boolean searchProduct(String query) {
        getProductsAdapter().clear();
        if (query != "") {
            getProductsAdapter().addAll(service.findProductsByName(query));
        } else {
            getProductsAdapter().addAll(service.getAllProducts());
        }
        getProductsAdapter().notifyDataSetChanged();
        return getProductsAdapter().getCount() > 0;
    }

    protected ProductsArrayAdapter getProductsAdapter() {
        return (ProductsArrayAdapter)productsList.getAdapter();
    }

    /***********************************************************************************************/
    /****************                           ASNYC TASK                          ****************/
    /***********************************************************************************************/
    class DownloadProductsService extends AsyncTask<String, Integer, Integer> {
        private String url = null;
        private final int CONN_TIMEOUT = 10000;
        private Exception exception;
        private List<Product> data;
        private Handler handler;

        public DownloadProductsService(Handler handler) {
            this.handler = handler;
        }

        @Override
        protected Integer doInBackground(String... urls) {
            data = new ArrayList<>();
            this.url = urls[0];

            HttpURLConnection urlConnection = null;
            int sz = 0;

            try {
                // Send GET data request
                URL url = new URL(this.url);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(CONN_TIMEOUT);
                urlConnection.setReadTimeout(20000);
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String Content = readStream(in);
                urlConnection.disconnect();

                JSONObject jsonMainNode = new JSONObject(Content);
                JSONArray items = jsonMainNode.getJSONArray("json_list");
                sz = items.length();

                barProgressDialog.setMax(sz);

                for (int i=0; i < sz; i++) {
                    publishProgress(i + 1);
                    JSONObject obj = items.getJSONObject(i);
                    Product p = new Product(obj.getString("name"), obj.getString("code"), obj.getDouble("price"));
                    data.add(p);
                }

                //serv.saveAll(data);
                repo.addAll(data);
            }
            catch (Exception e) {
                exception = e;
                e.printStackTrace();
            }

            return sz;
        }

        /** This method runs on the UI thread */
        protected void onProgressUpdate(final Integer... progressValue) {
            handler.post(new Runnable() {
                public void run() {
                    barProgressDialog.setProgress(progressValue[0]);
                }
            });
        }

        protected void onPostExecute(Integer result) {
            if(exception == null) {
//                showMessage("ok: " + result);

                getProductsAdapter().addAll(service.getAllProducts());
                getProductsAdapter().notifyDataSetChanged();
                long cnt = repo.size();
                showMessage("Aggiornamento completato: " + cnt);
            } else {
                showError(exception);
            }
            barProgressDialog.dismiss();
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
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
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
    }

    private void showMessage(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}
