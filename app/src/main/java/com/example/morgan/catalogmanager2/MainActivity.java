package com.example.morgan.catalogmanager2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private static int ACTIVITY_TASK_ADD = 1;
    private static int ACTIVITY_TASK_UPDATE = 2;

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

        setTitle("Catalog Manager");

        bd = new CMDataSource(this);
        loadProductes();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_TASK_ADD) {
            if (resultCode == RESULT_OK) {
                // Carreguem totes les tasques a lo bestia
                refreshTasks();
            }
        }

        if (requestCode == ACTIVITY_TASK_UPDATE) {
            if (resultCode == RESULT_OK) {
                refreshTasks();
            }
        }

    }

    private void refreshTasks() {

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
        startActivityForResult(i,ACTIVITY_TASK_ADD);
    }

    private void updateProducte(long id) {
        // Cridem a l'activity del detall de la tasca enviant l'id de la línia clickada
        Bundle bundle = new Bundle();
        bundle.putLong("id",id);

        idActual = id;


        Intent i = new Intent(this, ProductActivity.class );
        i.putExtras(bundle);
        startActivityForResult(i,ACTIVITY_TASK_UPDATE);
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
        /*ImageView btnMensage = (ImageView) view.findViewById(R.id.btnDelete);
        btnMensage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Busco la ROW
                View row = (View) v.getParent();
                // Busco el ListView
                ListView lv = (ListView) row.getParent();
                // Busco quina posicio ocupa la Row dins de la ListView
                int position = lv.getPositionForView(row);

                // Carrego la linia del cursor de la posició.
                Cursor linia = (Cursor) getItem(position);

                oTodoListIcon.deleteTask(linia.getInt(linia.getColumnIndexOrThrow(toDoListDatasource.TODOLIST_ID)));
            }
        });*/

        return view;
    }
}
