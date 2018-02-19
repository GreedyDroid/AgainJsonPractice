package com.example.sayed.againjsonpractice;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView flowerNameTV;
    private static final String FLOWER_URL= "http://services.hanselandpetal.com/feeds/flowers.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flowerNameTV= (TextView) findViewById(R.id.flowerName);

        ConnectivityManager connectivityManger = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManger.getActiveNetworkInfo();
        if (networkInfo != null){
            if (networkInfo.isAvailable() && networkInfo.isConnected()){
                new MyJsonDownloadTask().execute(FLOWER_URL);
            }else {
                Toast.makeText(this, "Network Error", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "No network Found", Toast.LENGTH_SHORT).show();
        }

    }

    public class MyJsonDownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            try {
                url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader reader = new BufferedReader(inputStreamReader);
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";

                while((line=reader.readLine())!=null){
                    stringBuilder.append(line);
                }
                return stringBuilder.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONArray jsonArray = new JSONArray(s);
                JSONObject flowerObj = jsonArray.getJSONObject(0);
                String flowerName = flowerObj.getString("name");
                Toast.makeText(MainActivity.this, flowerName, Toast.LENGTH_SHORT).show();
                flowerNameTV.setText(flowerName);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            super.onPostExecute(s);
        }
    }
}
