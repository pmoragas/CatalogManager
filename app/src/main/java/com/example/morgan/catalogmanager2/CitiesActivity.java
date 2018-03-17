package com.example.morgan.catalogmanager2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CitiesActivity extends AppCompatActivity {

    private static int ACTIVITY_PRODUCT_ADD = 1;
    private static int ACTIVITY_PRODUCT_UPDATE = 2;

    public CMDataSource bd;


    public long idActual;

    private CitiesAdapter citiesAdapter;
    private static String[] from = new String[]{CMDataSource.ciutat_nom};
    private static int[] to = new int[]{R.id.txtVwNomCity};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCiutat();
            }
        });

        setTitle("Ciutats");

        bd = new CMDataSource(this);
        loadCities();

    }


    //llista les ciutats a la ListView
    private void loadCities() {

        // Demanem tots els productes
        Cursor cursorTasks = bd.CiutatsList();

        citiesAdapter = new CitiesAdapter(this, R.layout.row_ciutat, cursorTasks, from, to, 1);

        citiesAdapter.oProductListIcon = this;


        ListView lv = (ListView) findViewById(R.id.lvCities);
        lv.setAdapter(citiesAdapter);

        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                                            int position, long id) {

                        // anem al detall de la ciutat
                        // Cridem a l'activity del detall de la tasca enviant l'id de la línia clickada
                        Bundle bundle = new Bundle();
                        bundle.putLong("id",id);
                        TextView txtNom = (TextView) findViewById(R.id.txtVwNomCity);
                        bundle.putString("nom",txtNom.getText().toString());
                        idActual = id;


                        Intent i = new Intent(CitiesActivity.this, CityDetailsActivity.class );
                        i.putExtras(bundle);
                        startActivity(i);

                    }
                }
        );
    }

    //AFEGIR Ciutat
    private void addCiutat() {
        // Cridem a l'activity del detall de la tasca enviant com a id -1
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Crear Ciutat");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText input_nom = new EditText(this);
        input_nom.setHint("Málaga");
        input_nom.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        layout.addView(input_nom);

        builder.setView(layout);

        //SI CONFIRMA
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                bd.addCiutat(input_nom.getText().toString());
                Toast toast = Toast.makeText(CitiesActivity.this,"Ciutat afegida amb èxit", Toast.LENGTH_SHORT);
                toast.show();
                loadCities();


            }
        });

        builder.setNegativeButton("Cancel·lar", null);

        builder.show();
    }


    public void deleteCiutat(final int _id) {
        // Pedimos confirmación
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Desitja eliminar la ciutat?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                bd.deleteCiutat(_id);
                //refreshProducts();
            }
        });

        builder.setNegativeButton("No", null);

        builder.show();
    }

}

class CitiesAdapter extends android.widget.SimpleCursorAdapter{


    public  CitiesActivity oProductListIcon;

    public CitiesAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = super.getView(position, convertView, parent);



        return view;
    }


}
