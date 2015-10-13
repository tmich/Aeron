package it.aeg2000srl.aeron.views;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import it.aeg2000srl.aeron.R;
import it.aeg2000srl.aeron.core.User;
import it.aeg2000srl.aeron.repositories.UserRepository;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Handler.Callback {

    Button btnAccedi;
    EditText etUsername;
    EditText etPassword;
    Handler mHandler;
    ProgressBar spinner;
    LinearLayout layout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnAccedi = (Button)findViewById(R.id.btnAccedi);
        etUsername = (EditText)findViewById(R.id.etUserName);
        etPassword = (EditText)findViewById(R.id.etPass);
        layout1 = (LinearLayout)findViewById(R.id.layout1);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);

        btnAccedi.setOnClickListener(this);
        mHandler = new Handler(this);
    }

    @Override
    public boolean handleMessage(Message message) {
        Bundle data = message.getData();
        if(data.keySet().contains("userCode")) {
            String userCode = data.getString("userCode");
            String userName = data.getString("userName");

            if (userCode != null) {
                User user = new User();
                user.setCode(userCode);
                user.setUsername(userName);
                UserRepository userRepository = new UserRepository();
                long userId = userRepository.add(user);

                if (userId == 0) {
                    showError(new Exception());
                }

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }

        } else {
            // Login fallito
            showError(new FileNotFoundException());
        }

        spinner.clearAnimation();
        toggleControls(true);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    protected void toggleControls(boolean enabled) {
        etUsername.setEnabled(enabled);
        etPassword.setEnabled(enabled);
        btnAccedi.setEnabled(enabled);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showError(Exception e) {
        try {
            e.printStackTrace();
            throw e;
        }
        catch (FileNotFoundException ex) {
            showMessage("Nome utente o password non validi.");
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

        spinner.clearAnimation();
        spinner.setVisibility(View.INVISIBLE);
        toggleControls(true);
    }

    private void showMessage(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        String username, password;
        username = etUsername.getText().toString();
        password = etPassword.getText().toString();

        if (username.isEmpty()) {
            etUsername.setError("Inserire il nome utente");
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Inserire la password");
            return;
        }

        toggleControls(false);

        ObjectAnimator animation = ObjectAnimator.ofInt (spinner, "progress", 1, 500);
        animation.setDuration (5000); //in milliseconds
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
        spinner.setVisibility(View.VISIBLE);

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String url = SP.getString("pref_default_api_url", getString(R.string.test_url)) + "/login";
        new AsyncLogin(mHandler, username, password).execute(url);
    }

    /***********************************************************************************************/
    /****************                           ASNYC TASK                          ****************/
    /***********************************************************************************************/
    class AsyncLogin extends AsyncTask<String, Integer, Integer> {
        Handler mHandler;
        URL mUrl;
        String mUsername, mPassword;
        Exception exception;
        final int CONN_TIMEOUT = 5000;
        String mUserCode;

        public AsyncLogin(Handler handler, String username, String password) {
            mHandler = handler;
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Integer doInBackground(String... urls) {
            HttpURLConnection conn = null;

            try {
                // Send POST data request
                mUrl = new URL(urls[0]);

                String Content;

                // Send POST data request
                conn = (HttpURLConnection) mUrl.openConnection();
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setConnectTimeout(CONN_TIMEOUT);
                conn.setReadTimeout(20000);
                conn.setRequestMethod("POST");

                JSONObject json = new JSONObject();
                json.put("username", mUsername);
                json.put("password", mPassword);

                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                osw.write(json.toString());
                osw.flush();

                InputStream in = new BufferedInputStream(conn.getInputStream());
                Content = readStream(in);
                JSONObject resp = new JSONObject(Content);
                mUserCode = resp.getString("code");
                // String username = resp.getString("username");
            }
            catch (Exception e) {
                exception = e;
                e.printStackTrace();
                return 0;
            }
            finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

            return 1;
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
        protected void onPostExecute(Integer result) {
            if (exception == null) {
                Message msg = new Message();
                Bundle data = new Bundle();
                if (result == 1) {
                    data.putString("userCode", mUserCode);
                    data.putString("userName", mUsername);
                } else {
                    data.putString("userCode", null);
                }
                msg.setData(data);
                mHandler.sendMessage(msg);
            } else {
                showError(exception);
            }
        }

    }
}
