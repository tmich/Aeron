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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import it.aeg2000srl.aeron.core.Customer;
import it.aeg2000srl.aeron.repositories.CustomerRepository;
import it.aeg2000srl.aeron.services.UseCasesService;
import it.aeg2000srl.aeron.views.adapters.CustomersArrayAdapter;

public class CustomersActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    ListView customersList;
    ProgressDialog barProgressDialog;
    Handler updateBarHandler;
    protected CustomerRepository repo;
    UseCasesService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        customersList = (ListView)findViewById(R.id.customersList);
        customersList.setEmptyView(findViewById(R.id.empty_list));
        service = new UseCasesService();
        repo = new CustomerRepository();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        customersList.setAdapter(new CustomersArrayAdapter(this, service.getAllCustomers()));
        Log.d("aeron", this.getClass().getCanonicalName() + " onPostCreate ");


        // scheda cliente
        customersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CustomersActivity.this, CustomerDetailsActivity.class);
                intent.putExtra(getString(R.string.customerId), getCustomersAdapter().getItem(i).getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_customer, menu);

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
        barProgressDialog = new ProgressDialog(CustomersActivity.this);
        barProgressDialog.setTitle(getString(R.string.title_activity_update_customers));
        barProgressDialog.setMessage(getString(R.string.please_wait));
        barProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        barProgressDialog.setProgress(0);
        barProgressDialog.show();
        updateBarHandler = new Handler();
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String url = SP.getString("pref_default_api_url", getString(R.string.test_url)) + "/customers";
        showMessage(url);
        new DownloadCustomersService(updateBarHandler).execute(url);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return searchCustomer(query);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return searchCustomer(newText);
    }

    protected boolean searchCustomer(String query) {
        getCustomersAdapter().clear();
        if (query != "") {
            getCustomersAdapter().addAll(service.findCustomerByName(query));
        } else {
            getCustomersAdapter().addAll(service.getAllCustomers());
        }
        getCustomersAdapter().notifyDataSetChanged();

        return getCustomersAdapter().getCount() > 0;
    }

    protected CustomersArrayAdapter getCustomersAdapter() {
        return (CustomersArrayAdapter)customersList.getAdapter();
    }

    /***********************************************************************************************/
    /****************                           ASNYC TASK                          ****************/
    /***********************************************************************************************/
    class DownloadCustomersService extends AsyncTask<String, Integer, Integer> {
        private String url = null;
        private final int CONN_TIMEOUT = 10000;
        private Exception exception;
        private List<Customer> data;
        private Handler handler;

        public DownloadCustomersService(Handler handler) {
            this.handler = handler;
//            showMessage("Prima - Sono presenti " + repo.size() + " clienti");
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
                    Customer customer = new Customer(obj.getString("name"),
                            obj.has("address") ? obj.getString("address") : "",
                            obj.has("city") ?  obj.getString("city") : "");
                    customer.setCode(obj.getString("code"));
                    data.add(customer);
                }
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
                getCustomersAdapter().addAll(service.getAllCustomers());
                getCustomersAdapter().notifyDataSetChanged();
                showMessage("Aggiornamento completato");
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
