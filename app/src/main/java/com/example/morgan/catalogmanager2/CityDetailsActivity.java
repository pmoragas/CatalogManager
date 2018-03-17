package com.example.morgan.catalogmanager2;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CityDetailsActivity extends AppCompatActivity {

    JSONObject data = null;
    JSONObject receivedData = null;
    JSONObject main = null;

    int temp;
    int pressure;
    int humidity;
    int temp_min;
    int temp_max;

    TextView txtNom;
    TextView txtTemp;
    TextView txtPress;
    TextView txtHumit;
    TextView txtTmax;
    TextView txtTmin;

    CMDataSource bd;
    Long idCiutat;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_details);
        toolbar = (Toolbar) findViewById(R.id.toolbarCityDetails);
        setSupportActionBar(toolbar);


        receivedData = new JSONObject();
        bd = new CMDataSource(this);


        idCiutat = this.getIntent().getExtras().getLong("id");
        cargarDatos();




    }

    private void cargarDatos() {

        // Demanem un cursor que retorna un sol registre amb les dades de la tasca
        // Això es podria fer amb un classe pero...
        Cursor datos = bd.getCiutat(idCiutat);
        datos.moveToFirst();

        // Carreguem les dades en la interfície
        TextView tv;


        String ciutat = datos.getString(datos.getColumnIndex(CMDataSource.ciutat_nom));
        toolbar.setTitle(ciutat);
        getSupportActionBar().setTitle(ciutat);


        getJSON(ciutat);

    }

    public JSONObject getJSON(final String city) {

        new AsyncTask<Void, Void, Void>() {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+city+"&APPID=0d9e6c92afae1358698afaa3256af6a6");

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuffer json = new StringBuffer(1024);
                    String tmp = "";

                    while((tmp = reader.readLine()) != null)
                        json.append(tmp).append("\n");
                    reader.close();

                    data = new JSONObject(json.toString());

                    if(data.getInt("cod") != 200) {
                        System.out.println("Cancelled");
                        return null;
                    }


                } catch (Exception e) {

                    System.out.println("Exception "+ e.getMessage());
                    return null;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void Void) {
                if(data!=null){
                    Log.d("my weather received",data.toString());
                    try {
                        main = data.getJSONObject("main");
                        temp = main.getInt("temp");
                        pressure = main.getInt("pressure");
                        humidity = main.getInt("humidity");
                        temp_min = main.getInt("temp_min");
                        temp_max = main.getInt("temp_max");

                        txtTemp = (TextView)findViewById(R.id.txtVwTemp);
                        txtPress = (TextView)findViewById(R.id.txtVwPress);
                        txtHumit = (TextView)findViewById(R.id.txtVwHumit);
                        txtTmax = (TextView)findViewById(R.id.txtVwTmax);
                        txtTmin = (TextView)findViewById(R.id.txtVwTempMin);

                        Log.d("my DATA",String.valueOf(temp));
                        txtTemp.setText(String.valueOf(temp-273)+"℃");
                        txtPress.setText(String.valueOf(pressure)+"hPa");
                        txtHumit.setText(String.valueOf(humidity)+"%");
                        txtTmax.setText(String.valueOf(temp_max-273)+"℃");
                        txtTmin.setText(String.valueOf(temp_min-273)+"℃");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        }.execute();

        return data;
    }

}
