package com.example.morgan.catalogmanager2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.FontsContract;

/**
 * Created by MORGAN on 23/01/2018.
 */

public class CMDataSource {

    public static final String table_PRODUCTES = "Productes";
    public static final String product_ID = "_id";
    public static final String product_CodiArticle = "codi_article";
    public static final String product_Descripcio = "descripcio";
    public static final String product_PVP = "PVP";
    public static final String product_Stock = "stock";
    public static final String[] table_COLUMNS = { product_ID, product_CodiArticle, product_Descripcio, product_PVP,product_Stock};

    private CMSQLiteHelper dbHelper;
    private SQLiteDatabase dbW, dbR;

    //CONSTRUCTOR
    public CMDataSource(Context context){
        // En el constructor directament obro la comunicació amb la base de dades
        dbHelper = new CMSQLiteHelper(context);

        // amés també construeixo dos databases un per llegir i l'altre per alterar
        open();
    }

    // DESTRUCTOR
    protected void finalize () {
        // Cerramos los databases
        dbW.close();
        dbR.close();
    }

    private void open() {
        dbW = dbHelper.getWritableDatabase();
        dbR = dbHelper.getReadableDatabase();
    }



    /**************CURSORS**********/

    public Cursor ProductList() {
        // Retorem totes les tasques
        return dbR.query(table_PRODUCTES, table_COLUMNS,
                null, null,
                null, null, product_ID);
    }

    public Cursor producte(long id) {
        // Retorna un cursor només amb el id indicat
        // Retornem les tasques que el camp DONE = 1
        return dbR.query(table_PRODUCTES, table_COLUMNS,
                product_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);

    }

    /**************DML****************/

    public long addProducte(String codi_article, String descripcio, Double PVP, Double stock) {
        // Creem un nou producte i retornem l'id creat per si el necessiten
        ContentValues values = new ContentValues();
        values.put(product_CodiArticle, codi_article);
        values.put(product_Descripcio, descripcio);
        values.put(product_PVP, PVP);
        values.put(product_Stock, stock);  // Forcem 0 pq si s'està creant la tasca no pot estar finalitzada

        return dbW.insert(table_PRODUCTES,null,values);
    }

    public void updateProducte(long id, String descripcio, Double PVP, Double stock) {
        // Modifiquem els valors del producte amb clau primària "id"
        ContentValues values = new ContentValues();
        values.put(product_Descripcio, descripcio);
        values.put(product_PVP, PVP);
        values.put(product_Stock,stock);

        dbW.update(table_PRODUCTES,values, product_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public void deleteProducte(long id) {
        // Eliminem el producte amb clau primària "id"
        dbW.delete(table_PRODUCTES,product_ID + " = ?", new String[] { String.valueOf(id) });
    }

}
