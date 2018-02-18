package com.example.morgan.catalogmanager2;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

public class MovementsActivity extends AppCompatActivity {

    public CMDataSource bd;
    private long idActual;

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
            /*case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;*/
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
