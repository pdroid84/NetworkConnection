package com.paul.debashis.networkconnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;


public class MyNetworkConnection extends ActionBarActivity {
    Button btn;
    TextView tView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_network_connection);
        btn = (Button) findViewById(R.id.button);
        tView = (TextView) findViewById(R.id.textView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_network_connection, menu);
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

    public void downloadData(View v){
        String url = "http://headers.jsontest.com/";
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        Log.d("DEB","Connection info - "+networkInfo);
        if(networkInfo!=null&&networkInfo.isConnected()){
            Log.d("DEB","Connection is available");
            Toast.makeText(this,"Network is --available",Toast.LENGTH_LONG).show();
            new MyDownload().execute(url);
        }
        else {
            Log.d("DEB","Connection is not available");
            Toast.makeText(this,"Network is not available",Toast.LENGTH_LONG).show();
        }
    }
    class MyDownload extends AsyncTask<String, Void, String> {
        InputStream in = null;
        @Override
        protected String doInBackground(String... params) {
            String data = null;
            try {
                data = downloadData(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            tView.setText(s);
        }
        protected String downloadData(String myurl) throws IOException {
            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                int response = conn.getResponseCode();
                Log.d("DEB", "Connection response code=" + response);
                in = conn.getInputStream();
                String dataContent = readIt(in, 500);
                return dataContent;
            } finally {
                if(in!=null){
                    in.close();
                }
            }
        }
        protected String readIt(InputStream stream,int len) throws IOException {
            Reader reader = null;
            reader = new InputStreamReader(stream,"UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }
    }
}
