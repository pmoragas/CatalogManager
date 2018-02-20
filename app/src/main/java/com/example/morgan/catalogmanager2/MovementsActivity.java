package com.example.morgan.catalogmanager2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.Console;

public class MovementsActivity extends AppCompatActivity {

    public CMDataSource bd;
    private int filterActual;
    public String inputFilterCodiActual;
    public String inputFilterDiaActual;
    public String inputFiltreDataIni;
    public String inputFiltreDataFin;
    private final int FILTER_ALL = 1;
    private final int FILTER_CODI = 2;
    private final int FILTER_DATES = 3;

    private MovAdapter movimentsAdapter;
    private static String[] from = new String[]{CMDataSource.moviment_CodiArticle,CMDataSource.moviment_dia,CMDataSource.moviment_quantitat,CMDataSource.moviment_tipus};
    private static int[] to = new int[]{R.id.txtVwCodiProd, R.id.txtVwDia, R.id.txtVwQtat,R.id.txtVwIO};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moviments);

        Toolbar toolbarMoviments = (Toolbar) findViewById(R.id.toolbarMoviments);
        setSupportActionBar(toolbarMoviments);
        setTitle("Moviments d'stock");

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);



        bd = new CMDataSource(this);
        loadMoviments();


    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        refreshMoviments();
    }



    private void refreshMoviments() {

        Cursor cursorMoviments = null;
        cursorMoviments = bd.MovimentList();

        // Demanem les tasques depenen del filtre que s'estigui aplicant
        switch (filterActual) {
            case FILTER_ALL:
                cursorMoviments = bd.MovimentList();
                break;
            case FILTER_CODI:
                cursorMoviments = bd.getMovimentsByCodiDia(inputFilterCodiActual,inputFilterDiaActual);
                break;
            case FILTER_DATES:
                cursorMoviments = bd.getMovimentsByDates(inputFiltreDataIni,inputFiltreDataFin);
                break;
        }


        // Notifiquem al adapter que les dades han canviat i que refresqui
        movimentsAdapter.changeCursor(cursorMoviments);
        movimentsAdapter.notifyDataSetChanged();
    }

    private void loadMoviments() {

        // Demanem tots els productes
        Cursor cursorMoviments = bd.MovimentList();

        // Now create a simple cursor adapter and set it to display
        movimentsAdapter = new MovAdapter(this, R.layout.row_moviment, cursorMoviments, from, to, 1);
        //movimentsAdapter.oProductListIcon = this;

        //filterActual = filterKind.FILTER_ALL;

        ListView lv = (ListView) findViewById(R.id.lvMoviments);
        lv.setAdapter(movimentsAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_moviments,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){

            case R.id.filtre_codi:
                // Pedimos código y fecha
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("FILTRE PER CODI PRODUCTE I DATA");

                LinearLayout layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.VERTICAL);

                // Add a TextView here for the "Title" label, as noted in the comments
                final EditText input_codi = new EditText(this);
                input_codi.setHint("Codi Producte: HAM101");
                layout.addView(input_codi); // Notice this is an add method

                // Add another TextView here for the "Description" label
                final EditText input_dia = new EditText(this);
                input_dia.setHint("Data Final: 2018-12-31");
                input_dia.setInputType(InputType.TYPE_DATETIME_VARIATION_NORMAL);
                layout.addView(input_dia); // Another add method


                builder.setView(layout); // Again this is a set method, not add

                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        inputFilterCodiActual = input_codi.getText().toString();
                        inputFilterDiaActual = input_dia.getText().toString();
                        filterActual = FILTER_CODI;
                        refreshMoviments();
                        //System.out.println(filterActual);

                    }
                });

                builder.setNegativeButton("Cancel·lar", null);

                builder.show();
                break;

            case R.id.filtre_data:
                // Pedimos fechas
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("FILTRE PER DATES");


                LinearLayout layout2 = new LinearLayout(this);
                layout2.setOrientation(LinearLayout.VERTICAL);

                // Add a TextView here for the "Title" label, as noted in the comments
                final EditText input_data_ini = new EditText(this);
                input_data_ini.setHint("Data Inici: 2018-12-30 23:20:59");
                input_data_ini.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
                layout2.addView(input_data_ini); // Notice this is an add method

                // Add another TextView here for the "Description" label
                final EditText input_data_end = new EditText(this);
                input_data_end.setHint("Data Final: 2018-12-31 23:20:59");
                input_data_end.setInputType(InputType.TYPE_DATETIME_VARIATION_NORMAL);
                layout2.addView(input_data_end); // Another add method


                builder2.setView(layout2); // Again this is a set method, not add

                builder2.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        inputFiltreDataIni = input_data_ini.getText().toString();
                        inputFiltreDataFin = input_data_end.getText().toString();
                        filterActual = FILTER_DATES;
                        refreshMoviments();
                        //System.out.println(filterActual);

                    }
                });

                builder2.setNegativeButton("Cancel·lar", null);
                builder2.show();
                break;

            case R.id.filtre_all:
                filterActual = FILTER_ALL;
                refreshMoviments();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}

class MovAdapter extends android.widget.SimpleCursorAdapter{

    private static final String colorMovimentSortida = "#d78290";
    private static final String colorMovimentEntrada = "#c6e2ae";

    //public  MovementsActivity oProductListIcon;

    public MovAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = super.getView(position, convertView, parent);

        // Agafem l'objecte de la view que es una LINEA DEL CURSOR
        Cursor linia = (Cursor) getItem(position);

        String tipus = linia.getString(linia.getColumnIndexOrThrow(CMDataSource.moviment_tipus));

        // Pintem el fons de la view segons està completada o no
        if (tipus.equals("S")) {
            view.setBackgroundColor(Color.parseColor(colorMovimentSortida));
        }
        else if (tipus.equals("E")) {
            view.setBackgroundColor(Color.parseColor(colorMovimentEntrada));
        }


        return view;
    }
}
