package com.example.morgan.catalogmanager2;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by pmora on 15/02/2018.
 */

public class myDialogs {
    public static void showInformacion(Context ctx, String Texto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

        builder.setMessage(Texto);
        builder.setPositiveButton("Aceptar", null);

        builder.show();
    }

    public static void showToast(Context cts, String Texto) {
        Toast.makeText(cts, Texto, Toast.LENGTH_SHORT).show();
    }

    public static void showToastLargo(Context cts, String Texto) {
        Toast.makeText(cts, Texto, Toast.LENGTH_LONG).show();
    }
}
