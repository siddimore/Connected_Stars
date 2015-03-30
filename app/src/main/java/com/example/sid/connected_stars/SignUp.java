package com.example.sid.connected_stars;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Sid on 3/27/2015.
 */
public class SignUp extends Activity {

    private Button loginButton;
    private JSONObject signUpnowq = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        loginButton = (Button) findViewById(R.id.button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EditText edEmail = (EditText) findViewById(R.id.editText3);
                    EditText edPassWord = (EditText) findViewById(R.id.editText2);
                    EditText edFirstName = (EditText) findViewById(R.id.editText5);
                    EditText edLastName = (EditText) findViewById(R.id.editText4);
                    EditText edConfirmPassWord = (EditText) findViewById(R.id.editText);
                    String email = edEmail.getText().toString();
                    String passWord = edPassWord.getText().toString();
                    String firstName = edFirstName.getText().toString();
                    String lastName = edLastName.getText().toString();
                    String confirmPassWord = edConfirmPassWord.getText().toString();
                    if (passWord.equals(confirmPassWord)) {
                        signUpnowq = new JSONObject();
                        signUpnowq.put("password", passWord); // Set the first name/pair
                        signUpnowq.put("confirmPassword", confirmPassWord);
                        signUpnowq.put("email", email);
                        signUpnowq.put("isrecruiter", "false");
                        signUpnowq.put("isrecruiteradmin", "false");
                        signUpnowq.put("firstname", firstName);
                        signUpnowq.put("lastname", lastName);
                        signUpnowq.put("companyname", "");
                        if (null != signUpnowq) {
                            new HttpPostAsyncTask().execute();
                        }

                    } else {
                        Toast.makeText(SignUp.this, "PassWord dont match", Toast.LENGTH_LONG).show();
                    }


                } catch (Exception ioe) {
                    System.out.println("Http Post Async Task Failed" + ioe);
                }
            }
        });
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    //Block to POST Signup
    public String POST() {
        {
            HttpURLConnection Uconnection = null;
            String targetURL = "https://dit.connectedstars.com/service/api/account/register";
            URL url = null;

            try {
                url = new URL(targetURL);
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }

            try {
                if (null != url) {
                    Uconnection = (HttpURLConnection) url.openConnection();
                    Uconnection.setRequestMethod("POST");
                    Uconnection.setRequestProperty("Content-Type",
                            "application/json");
                    Uconnection.setRequestProperty("Accept", "application/json");
                    Uconnection.setRequestProperty("Accep-Language", "en-US,en;q=0.8");
                    Uconnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
                    Uconnection.setRequestProperty("Content-Language", "en-US");
                    Uconnection.setDoInput(true);
                    Uconnection.setDoOutput(true);
                    Uconnection.setUseCaches(false);

                    DataOutputStream wr = new DataOutputStream(
                            Uconnection.getOutputStream());
                    wr.writeBytes(URLEncoder.encode(signUpnowq.toString(), "UTF-8"));
                    wr.flush();
                    wr.close();
                }

                //Verify Status Code
                int statusCode = Uconnection.getResponseCode();
                if (statusCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                    showToast("Already registered, Please Login using your credentials");

                } else {
                    System.out.println("Signup Succesful");
                    InputStream instream = Uconnection.getInputStream();

                    int ch;
                    StringBuffer sb = new StringBuffer();
                    while ((ch = instream.read()) != -1) {
                        sb.append((char) ch);
                    }
                    System.out.println(sb.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "null";
    }

    //Async Task Post Sign
    private class HttpPostAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... urls) {
            return POST();
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }

    public void showToast(final String toast) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(SignUp.this, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Helper to Convert InputStream to JSON String
    private static String convertStreamToString(InputStream is) {
        String line = "";
        StringBuilder total = new StringBuilder();
        try {

            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                while ((line = rd.readLine()) != null) {
                    total.append(line);
                }
            } catch (Exception e) {
                System.out.println("Stream Exception");
            }

        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }
        return total.toString();
    }
}

