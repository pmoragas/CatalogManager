package com.example.morgan.catalogmanager2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    private static int ACTIVITY_PRODUCT_ADD = 1;
    private static int ACTIVITY_PRODUCT_UPDATE = 2;
    private static final int ACTIVITY_MOVEMENT_ADD = 3;


    public CMDataSource bd;
    private long idActual;

    private CMAdapter productAdapter;
    private static String[] from = new String[]{CMDataSource.product_CodiArticle,CMDataSource.product_Descripcio,CMDataSource.product_Stock};
    private static int[] to = new int[]{R.id.txtVwCodi, R.id.txtVwDescripcio, R.id.txtVwStock};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProducte();
            }
        });

        setTitle("Productes");

        bd = new CMDataSource(this);
        loadProductes();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_movements:
                Intent i = new Intent(this, MovementsActivity.class );
                startActivity(i);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_PRODUCT_ADD) {
            if (resultCode == RESULT_OK) {
                // Carreguem totes les tasques a lo bestia
                refreshProducts();
            }
        }

        if (requestCode == ACTIVITY_PRODUCT_UPDATE) {
            if (resultCode == RESULT_OK) {
                refreshProducts();
            }
        }





    }

    private void refreshProducts() {

        Cursor cursorProducts = null;
        cursorProducts = bd.ProductList();

        // Demanem les tasques depenen del filtre que s'estigui aplicant
        /*switch (filterActual) {
            case FILTER_ALL:
                cursorTasks = bd.toDoList();
                break;
            case FILTER_COMPLETED:
                cursorTasks = bd.toDoListCompleted();
                break;
            case FILTER_PENDING:
                cursorTasks = bd.toDoListPending();
                break;
        }*/


        // Notifiquem al adapter que les dades han canviat i que refresqui
        productAdapter.changeCursor(cursorProducts);
        productAdapter.notifyDataSetChanged();
    }

    private void loadProductes() {

        // Demanem tots els productes
        Cursor cursorTasks = bd.ProductList();

        // Now create a simple cursor adapter and set it to display
        productAdapter = new CMAdapter(this, R.layout.row_producte, cursorTasks, from, to, 1);
        productAdapter.oProductListIcon = this;

        //filterActual = filterKind.FILTER_ALL;

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

    private void addProducte() {
        // Cridem a l'activity del detall de la tasca enviant com a id -1
        Bundle bundle = new Bundle();
        bundle.putLong("id",-1);

        idActual = -1;

        Intent i = new Intent(this, ProductActivity.class );
        i.putExtras(bundle);
        startActivityForResult(i, ACTIVITY_PRODUCT_ADD);
    }

    private void addMoviment() {
        // Cridem a l'activity del detall de la tasca enviant com a id -1
        Bundle bundle = new Bundle();
        bundle.putLong("id",-1);

        idActual = -1;

        Intent i = new Intent(this, ProductActivity.class );
        i.putExtras(bundle);
        startActivityForResult(i,ACTIVITY_MOVEMENT_ADD);
    }

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


    public void createMoviment(final int _id, final String tipus) {

        Cursor cursorProducte = bd.getProducte(_id);

        /*Calendar c = Calendar.getInstance();
        //System.out.println("Current time => "+c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd-mm-yyyy ss:mm:HH");
        String formattedDate = df.format(c.getTime());

        final String diatext = formattedDate;*/

        final Date currentTime = Calendar.getInstance(Locale.getDefault()).getTime();
        final String diatext = currentTime.toString();

        String codi_producte_curs = "ERROR";
        Double stockActual_curs = 9999999.0;


        if (cursorProducte.moveToFirst())
        {
            do
            {
                codi_producte_curs = cursorProducte.getString(cursorProducte.getColumnIndexOrThrow(CMDataSource.product_CodiArticle));
                stockActual_curs = cursorProducte.getDouble(cursorProducte.getColumnIndexOrThrow(CMDataSource.product_Stock));


            } while (false);
        }

        final String codi_producte = codi_producte_curs;
        final Double stockActual = stockActual_curs;

        // Pedimos confirmación
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(tipus.equals("E")){
            builder.setTitle("ENTRADA DE "+codi_producte);
            builder.setMessage("Introdueix la quantitat de stock que entra:");
        } else {
            builder.setTitle("SORTIDA DE "+codi_producte);
            builder.setMessage("Introdueix la quantitat de stock que surt:");
        }

        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String textInput = input.getText().toString();
                if(isNumeric(textInput)){
                    int quantitat = Integer.parseInt(textInput);
                    bd.addMoviment(_id,codi_producte,diatext,quantitat,tipus);

                    if(tipus.equals("E")){
                        bd.updateProducteStock(_id,stockActual+quantitat);
                    } else {
                        bd.updateProducteStock(_id,stockActual-quantitat);
                    }
                    myDialogs.showToast(MainActivity.this,"Moviment efectuat amb èxit");
                    refreshProducts();


                } else {
                    myDialogs.showToast(MainActivity.this,"Moviment amb errors, cancel·lat");
                }
            }
        });

        builder.setNegativeButton("Cancel·lar", null);

        builder.show();




    }

    public boolean isNumeric(String text){
        try {
            int num = Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /*
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
    */

}

class CMAdapter extends android.widget.SimpleCursorAdapter{

    private static final String colorProductOutOfStock = "#d78290";
    private static final String colorProductInStock = "#d7d7d7";

    public  MainActivity oProductListIcon;

    public CMAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
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
