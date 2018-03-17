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
import android.widget.ImageView;
import android.widget.ListView;

public class CitiesActivity extends AppCompatActivity {

    private static int ACTIVITY_PRODUCT_ADD = 1;
    private static int ACTIVITY_PRODUCT_UPDATE = 2;

    public CMDataSource bd;


    public long idActual;

    private CitiesAdapter citiesAdapter;
    private static String[] from = new String[]{CMDataSource.ciutat_nom};
    private static int[] to = new int[]{R.id.txtVwNom};

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



    }


    //llista els productes a la ListView
    private void loadProductes() {

        // Demanem tots els productes
        Cursor cursorTasks = bd.ProductList();

        productAdapter = new CMAdapter(this, R.layout.row_producte, cursorTasks, from, to, 1);
        productAdapter.oProductListIcon = this;


        ListView lv = (ListView) findViewById(R.id.lvDades);
        lv.setAdapter(productAdapter);

        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                                            int position, long id) {

                        // modifiquem el id
                        updateProducte(id);
                    }
                }
        );
    }

    //AFEGIR PRODUCTE
    private void addProducte() {
        // Cridem a l'activity del detall de la tasca enviant com a id -1
        Bundle bundle = new Bundle();
        bundle.putLong("id",-1);

        idActual = -1;

        Intent i = new Intent(this, ProductActivity.class );
        i.putExtras(bundle);
        startActivityForResult(i, ACTIVITY_PRODUCT_ADD);
    }


    //UPDATAR PRODUCTE
    private void updateProducte(long id) {
        // Cridem a l'activity del detall de la tasca enviant l'id de la línia clickada
        Bundle bundle = new Bundle();
        bundle.putLong("id",id);

        idActual = id;


        Intent i = new Intent(this, ProductActivity.class );
        i.putExtras(bundle);
        startActivityForResult(i, ACTIVITY_PRODUCT_UPDATE);
    }

    public void deleteProducte(final int _id) {
        // Pedimos confirmación
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Desitja eliminar el producte?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                bd.deleteProducte(_id);
                refreshProducts();
            }
        });

        builder.setNegativeButton("No", null);

        builder.show();
    }

}

class CitiesAdapter extends android.widget.SimpleCursorAdapter{

    private static final String colorProductOutOfStock = "#d78290";
    private static final String colorProductInStock = "#d7d7d7";

    public  MainActivity oProductListIcon;

    public CitiesAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = super.getView(position, convertView, parent);

        // Agafem l'objecte de la view que es una LINEA DEL CURSOR
        Cursor linia = (Cursor) getItem(position);

        double stock = linia.getDouble(linia.getColumnIndexOrThrow(CMDataSource.product_Stock));

        // Pintem el fons de la view segons està completada o no
        if (stock < 0) {
            view.setBackgroundColor(Color.parseColor(colorProductOutOfStock));
        }
        else {
            view.setBackgroundColor(Color.parseColor(colorProductInStock));
        }

        // Capturem botons
        ImageView btnDelete = (ImageView) view.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Busco la ROW
                View row = (View) v.getParent();
                // Busco el ListView
                ListView lv = (ListView) row.getParent();
                // Busco quina posicio ocupa la Row dins de la ListView
                int position = lv.getPositionForView(row);

                // Carrego la linia del cursor de la posició.
                Cursor linia = (Cursor) getItem(position);

                oProductListIcon.deleteProducte(linia.getInt(linia.getColumnIndexOrThrow(CMDataSource.product_ID)));
            }
        });

        ImageView btnEntrada = (ImageView) view.findViewById(R.id.btnStockIn);
        btnEntrada.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onClick(View v) {

                // Busco la ROW
                View row = (View) v.getParent();
                // Busco el ListView
                ListView lv = (ListView) row.getParent();
                // Busco quina posicio ocupa la Row dins de la ListView
                int position = lv.getPositionForView(row);

                // Carrego la linia del cursor de la posició.
                Cursor linia = (Cursor) getItem(position);

                oProductListIcon.createMoviment(linia.getInt(linia.getColumnIndexOrThrow(CMDataSource.product_ID)),"E");
            }
        });

        ImageView btnSortida = (ImageView) view.findViewById(R.id.btnStockOut);
        btnSortida.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onClick(View v) {

                // Busco la ROW
                View row = (View) v.getParent();
                // Busco el ListView
                ListView lv = (ListView) row.getParent();
                // Busco quina posicio ocupa la Row dins de la ListView
                int position = lv.getPositionForView(row);

                // Carrego la linia del cursor de la posició.
                Cursor linia = (Cursor) getItem(position);

                oProductListIcon.createMoviment(linia.getInt(linia.getColumnIndexOrThrow(CMDataSource.product_ID)),"S");
            }
        });




        return view;
    }


}
