package com.example.morgan.catalogmanager2;

import java.util.LinkedList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by MORGAN on 23/01/2018.
 */

public class CMSQLiteHelper extends SQLiteOpenHelper {

    // database version
    private static int database_VERSION = 100;
    // database name
    private static final String database_NAME = "CatalogManager";


    public CMSQLiteHelper(Context context) {
        super(context, database_NAME, null, database_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_PRODUCT_TABLE = "CREATE TABLE Productes ( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "codi_article text NOT NULL UNIQUE, " +
                "descripcio text NOT NULL, " +
                "PVP real, " +
                "stock integer)";

        db.execSQL(CREATE_PRODUCT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop books table if already exists
        db.execSQL("DROP TABLE IF EXISTS Productes");
        this.onCreate(db);
    }
}
