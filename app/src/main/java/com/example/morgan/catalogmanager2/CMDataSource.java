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
    public static final String[] PRODUCTES_COLUMNS = { product_ID, product_CodiArticle, product_Descripcio, product_PVP,product_Stock};

    public static final String table_MOVIMENTS = "Moviments";
    public static final String moviment_ID = "_id";
    public static final String moviment_producte_id = "producte_id";
    public static final String moviment_CodiArticle = "codi_article";
    public static final String moviment_dia = "dia";
    public static final String moviment_quantitat = "quantitat";
    public static final String moviment_tipus = "tipus";
    public static final String[] MOVIMENTS_COLUMNS = { moviment_ID, moviment_producte_id, moviment_CodiArticle, moviment_dia,moviment_quantitat,moviment_tipus};



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
        // Retornem tots els productes
        return dbR.query(table_PRODUCTES, PRODUCTES_COLUMNS,
                null, null,
                null, null, product_ID);
    }

    public Cursor getProductListByDescription(String description) {
        // Retornem tots els productes que continguin la descripció
        return dbR.query(table_PRODUCTES, PRODUCTES_COLUMNS,
                product_Descripcio+" LIKE ?", new String[] {"%"+String.valueOf(description)+"%"},
                null, null, product_ID);
    }

    public Cursor MovimentList() {
        // Retornem tots els moviments
        return dbR.query(table_MOVIMENTS, MOVIMENTS_COLUMNS,
                null, null,
                null, null, moviment_ID);
    }

    public Cursor getMovimentsByCodiDia(String codi, String dia) {
        // Retornem els moviments en què el codi conté el paràmetre d'entrada
        String dia_inici = dia+" 00:00";
        String dia_fi = dia+" 59:59";

        return dbR.query(table_MOVIMENTS, MOVIMENTS_COLUMNS,
                moviment_CodiArticle + " LIKE ? AND ("+moviment_dia+" >=? OR "+moviment_dia+ "<=?)", new String[] { "%"+String.valueOf(codi)+"%" ,String.valueOf(dia_inici),String.valueOf(dia_fi)},
                null, null, moviment_ID);
    }

    public Cursor getMovimentsByDates(String data_ini, String data_fin) {
        // Retornem els moviments en què la data es troba entre la data inici i la data de fi
        if(data_ini.equals("") && data_fin.equals("") ){
            // Retornem tots els moviments
            data_ini = "1900-01-01 00:01";
            data_fin = "2100-01-01 00:01";
        } else if (data_ini.equals("") && !data_fin.equals("")){
            //forcem la data més antiga com a inici
            data_ini = "1900-01-01 00:01";

        } else if(!data_ini.equals("") && data_fin.equals("")){
            //forcem la data més futurible com a final
            data_fin = "2100-01-01 00:01";

        }

        return dbR.query(table_MOVIMENTS, MOVIMENTS_COLUMNS,
                moviment_dia + " >= ? AND "+moviment_dia+" <= ?", new String[] { String.valueOf(data_ini),String.valueOf(data_fin) },
                null, null, moviment_ID);

    }


    public Cursor getProducte(long id) {
        // Retorna un cursor només amb el id indicat
        return dbR.query(table_PRODUCTES, PRODUCTES_COLUMNS,
                product_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);

    }

    public Cursor moviment(long id) {
        // Retorna un cursor només amb el id indicat
        return dbR.query(table_MOVIMENTS, MOVIMENTS_COLUMNS,
                moviment_ID + "=?", new String[]{String.valueOf(id)},
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

    public void updateProducteStock(long id, Double stock) {
        // Modifiquem els valors del producte amb clau primària "id"
        ContentValues values = new ContentValues();
        values.put(product_Stock,stock);

        dbW.update(table_PRODUCTES,values, product_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public void deleteProducte(long id) {
        // Eliminem el producte amb clau primària "id"
        dbW.delete(table_PRODUCTES,product_ID + " = ?", new String[] { String.valueOf(id) });
    }

    public long addMoviment(int producte_id, String codi_article, String dia, int quantitat, String tipus) {
        // Creem un nou moviment i retornem l'id creat per si el necessiten
        ContentValues values = new ContentValues();
        values.put(moviment_producte_id, producte_id);
        values.put(moviment_CodiArticle, codi_article);
        values.put(moviment_dia, dia);
        values.put(moviment_quantitat, quantitat);
        values.put(moviment_tipus, tipus);

        return dbW.insert(table_MOVIMENTS,null,values);
    }

}
