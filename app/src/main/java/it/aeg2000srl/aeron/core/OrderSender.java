package it.aeg2000srl.aeron.core;

import android.os.AsyncTask;

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

/**
 * Created by tiziano.michelessi on 08/10/2015.
 */
public class OrderSender extends AsyncTask<String, Integer, Integer> implements IOrderSender {
    String _url;
    int CONN_TIMEOUT = 10000;
    IOrder order;

    public OrderSender(String url) {
        _url = url;
    }

    @Override
    public void send(IOrder order) throws IOException, JSONException {
        this.order = order;
        doInBackground("");
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

    @Override
    protected Integer doInBackground(String... strings) {
        HttpURLConnection conn = null;
        String Content;

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
            json.put("customer_id", order.getCustomerId());
            JSONArray voci = new JSONArray();

            for(IOrderItem orderItem : order.getItems()) {
                JSONObject obj = new JSONObject();
                obj.put("product_id", orderItem.getProductId());
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
            conn.disconnect();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            conn = null;
        }

        return 1;
    }
}
