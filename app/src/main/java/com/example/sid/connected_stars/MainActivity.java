package com.example.sid.connected_stars;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.zip.GZIPInputStream;


public class MainActivity extends ActionBarActivity {
    private Button loginButton;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        loginButton = (Button) findViewById(R.id.button3);
        signUpButton = (Button) findViewById(R.id.button4);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
//                try {
//                    EditText edEmail = (EditText)findViewById(R.id.editText3);
//                    EditText edPassWord = (EditText)findViewById(R.id.editText2);
//                    String email = edEmail.getText().toString();
//                    String passWord = edPassWord.getText().toString();
//                    //String passWord = edPassWord
//                    String urlParameter = "grant_type=password&username="+email+"&password="+passWord;
//                    new HttpPostAsyncTask().execute(urlParameter);
//
//                } catch (Exception ioe) {
//                    System.out.println("Http Post Async Task Failed" + ioe);
//                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

    //Block to POST JSON object
    public String POST(String urlParameters) {
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpURLConnection connection = null;
            String targetURL = "https://dit.connectedstars.com/service/token";
            URL url = null;
            //String urlParameters = "grant_type=password&username=siddimore@live.com&password=Saanika123";
            try {
                url = new URL(targetURL);
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            }

            try {
                if (null != url) {
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Length", "" +
                            Integer.toString(urlParameters.getBytes().length));
                    connection.setRequestProperty("Content-Type",
                            "application/x-www-form-urlencoded");
                    connection.setRequestProperty("Content-Language", "en-US");
                    //send the POST out
                    PrintWriter out = new PrintWriter(connection.getOutputStream());
                    out.print(urlParameters);
                    out.close();
                }

                int statusCode = connection.getResponseCode();
                if (statusCode != HttpURLConnection.HTTP_OK) {
                    System.out.println("Login Failed");
                }
                else
                {
                    //HttpContent entity = connection.gegetContent();
                    System.out.println("Login Pass");
                    InputStream instream = connection.getInputStream();

                    int ch;
                    StringBuffer sb = new StringBuffer();
                    while ((ch = instream.read()) != -1) {
                        sb.append((char) ch);
                    }
                    System.out.println(sb.toString());
                    Intent intent = new Intent(this, AutoPost.class);

                    // calling an activity using <intent-filter> action name
                    //  Intent inent = new Intent("com.hmkcode.android.ANOTHER_ACTIVITY");

                    startActivity(intent);


//                    instream = new GZIPInputStream(instream);
//                    if (instream != null) {
//                        String resultString = convertStreamToString(instream);
//                        instream.close();
//                        try {
//                            JSONObject jsonObjRecv = new JSONObject(resultString);
//                            // Raw DEBUG output of our received JSON object:
//                            System.out.println(jsonObjRecv.toString());
//                        }
//                        catch (JSONException ex) {
//                            System.out.println("Exception in converting to JSONObject" + ex.toString());
//                        }
//                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "null";
    }

    //Async Task Post Workspace
    private class HttpPostAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        }
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
