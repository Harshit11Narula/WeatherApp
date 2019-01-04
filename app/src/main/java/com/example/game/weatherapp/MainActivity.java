package com.example.game.weatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText cityname;
    TextView result1;


    public class download extends AsyncTask<String , Void , String>{


        @Override
        protected String doInBackground(String... urls) {

            URL url;
            Log.i("weather12345", urls[0]);
            HttpURLConnection connection = null;
            try {
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                String result  = "";
                while(data!=-1){
                    char current = (char) data;
                    result += current;
                    data   = reader.read();
                }

                return result;

            }catch (Exception e){

                Toast.makeText(getApplicationContext() , "Could not find weather" , Toast.LENGTH_LONG).show();
            }
            String res = "";

            return res;
        }

        @Override
        protected void onPostExecute(String s) {


            try {

                String message = "";

                JSONObject jsonobj = new JSONObject(s);
                if(!jsonobj.has("weather")){
                    result1.setText(message);
                    Toast.makeText(getApplicationContext() , "Could not find weather" , Toast.LENGTH_LONG);
                    return;
                }
                String info = jsonobj.getString("weather");
                JSONArray arr = new JSONArray(info);

                for(int i =0;i<arr.length();i++){

                    JSONObject jsonpart = arr.getJSONObject(i);

                    String main = "";
                    String description = "";
                    main = jsonpart.getString("main");
                    description = jsonpart.getString("description");
                    if(main!="" && description!=""){
                        message += main  + ": " + description + "\r\n";

                    }

                    
                }

                if(message!=""){
                    result1.setText(message);
                } else {
                    result1.setText(message);
                    Toast.makeText(getApplicationContext() , "Could not find weather" , Toast.LENGTH_LONG).show();
                }


            }catch (Exception e){
                result1.setText("");
                Toast.makeText(getApplicationContext() , "Could not find weather" , Toast.LENGTH_LONG).show();

            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityname = (EditText) findViewById(R.id.cityname);
        result1 = (TextView) findViewById(R.id.textView2);
    }
    public void findweather(View view) {

        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityname.getWindowToken(), 0);
    try {
        String encodecityname = URLEncoder.encode(cityname.getText().toString(), "UTF-8");
        download task = new download();
        task.execute("https://api.openweathermap.org/data/2.5/weather?q=" + encodecityname +"&appid=f10fd960c599a62976da9317b08c3f48");

    } catch (Exception e) {
        result1.setText("");
        Toast.makeText(getApplicationContext() , "Could not find weather" , Toast.LENGTH_LONG).show();
    }

    }
}
