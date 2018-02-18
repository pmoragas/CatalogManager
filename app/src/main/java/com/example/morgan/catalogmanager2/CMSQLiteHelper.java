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
    private static int database_VERSION = 101;
    // database name
    private static final String database_NAME = "CatalogManager";


    public CMSQLiteHelper(Context context) {
        super(context, database_NAME, null, database_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create Productes table
        String CREATE_PRODUCT_TABLE = "CREATE TABLE Productes ( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "codi_article TEXT NOT NULL UNIQUE, " +
                "descripcio TEXT NOT NULL, " +
                "PVP REAL, " +
                "stock INTEGER)";

        db.execSQL(CREATE_PRODUCT_TABLE);

        // SQL statement to create Productes table
        String CREATE_MOVIMENTS_TABLE = "CREATE TABLE Moviments ( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "producte_id INTEGER NOT NULL, " +
                "codi_article TEXT NOT NULL, " +
                "dia TEXT NOT NULL, " +
                "quantitat REAL NOT NULL, " +
                "tipus TEXT NOT NULL, " +
                "FOREIGN KEY(producte_id) REFERENCES Productes(_id))";

        db.execSQL(CREATE_MOVIMENTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(database_VERSION == 100){
            // SQL statement to create Productes table
            String CREATE_MOVIMENTS_TABLE = "CREATE TABLE Moviments ( " +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "producte_id INTEGER NOT NULL, " +
                    "codi_article TEXT NOT NULL, " +
                    "dia TEXT NOT NULL, " +
                    "quantitat REAL NOT NULL, " +
                    "tipus TEXT NOT NULL, " +
                    "FOREIGN KEY(producte_id) REFERENCES Productes(_id))";

            db.execSQL(CREATE_MOVIMENTS_TABLE);

            database_VERSION = 101;
        }

    }
}
