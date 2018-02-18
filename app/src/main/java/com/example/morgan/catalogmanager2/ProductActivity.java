package com.example.morgan.catalogmanager2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


public class ProductActivity extends AppCompatActivity {

    private long idProduct;
    private CMDataSource bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fitxa_producte);


        bd = new CMDataSource(this);

        // Botones de aceptar y cancelar
        // Boton ok
        Button btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                aceptarCambios();
            }
        });

        // Boton eliminar
        Button  btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                deleteProduct();
            }
        });

        // Boton cancelar
        Button  btnCancel = (Button) findViewById(R.id.btnCancelar);
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cancelarCambios();
            }
        });

        // Busquem el id que estem modificant
        // si el el id es -1 vol dir que s'està creant
        idProduct = this.getIntent().getExtras().getLong("id");

        if (idProduct != -1) {
            // Si estem modificant carreguem les dades en pantalla
            cargarDatos();
        }
        else {
            // Si estem creant amaguem el botó d'eliminar
            btnDelete.setVisibility(View.GONE);
        }
    }

    private void cargarDatos() {

        // Demanem un cursor que retorna un sol registre amb les dades de la tasca
        // Això es podria fer amb un classe pero...
        Cursor datos = bd.getProducte(idProduct);
        datos.moveToFirst();

        // Carreguem les dades en la interfície
        TextView tv;

        tv = (TextView) findViewById(R.id.edtCodi);
        tv.setText(datos.getString(datos.getColumnIndex(CMDataSource.product_CodiArticle)));

        tv = (TextView) findViewById(R.id.edtDescripcio);
        tv.setText(datos.getString(datos.getColumnIndex(CMDataSource.product_Descripcio)));

        tv = (TextView) findViewById(R.id.edtPVP);
        tv.setText(datos.getString(datos.getColumnIndex(CMDataSource.product_PVP)));

        tv = (TextView) findViewById(R.id.edtStock);
        tv.setText(datos.getString(datos.getColumnIndex(CMDataSource.product_Stock)));


    }

    private void aceptarCambios() {
        // Validem les dades
        TextView tv;

        // Codi ha d'estar informat i no es pot repetir
        tv = (TextView) findViewById(R.id.edtCodi);
        String codi = tv.getText().toString();
        if (codi.trim().equals("")) {
            //myDialogs.showToast(this,"Ha d'informar del codi");
            return;
        }

        // La descripció és obligatòria
        tv = (TextView) findViewById(R.id.edtDescripcio);
        String descripcio = tv.getText().toString();
        if (descripcio.trim().equals("")) {
            //myDialogs.showToast(this,"Ha d'informar de la descripció");
            return;
        }

        // El PVP si es posa, ha de ser un real
        tv = (TextView) findViewById(R.id.edtPVP);
        String PVP = tv.getText().toString();
        Double PVPreal;
        if (PVP.trim().equals("")) {
            //myDialogs.showToast(this,"Ha d'informar del PVP");
            return;
        } else {
            try{
                PVPreal = Double.parseDouble(PVP);
            } catch(Exception e){
                //myDialogs.showToast(this,"El PVP ha de ser un real");
                return;
            }
        }

        // L' Stock es obligatori i per default està a 0. Cal que sigui 0 o més
        tv = (TextView) findViewById(R.id.edtStock);
        String stock = tv.getText().toString();
        Double stockReal;
        if (stock.trim().equals("")) {
            //myDialogs.showToast(this,"Ha d'informar de l'stock");
            return;
        } else {
            if(idProduct == -1){
                try{
                    stockReal = Double.parseDouble(stock);

                } catch(Exception e){
                    //myDialogs.showToast(this,"L'stock ha de ser numèric");
                    return;
                }

                if(stockReal < 0){
                    //myDialogs.showToast(this,"L'stock ha de ser 0 o més");
                    return;
                }
            } else {
                try{
                    stockReal = Double.parseDouble(stock);

                } catch(Exception e){
                    //myDialogs.showToast(this,"L'stock ha de ser numèric");
                    return;
                }
            }

        }

        // Mirem si estem creant o estem guardant
        if (idProduct == -1) {
            idProduct = bd.addProducte(codi,descripcio,PVPreal,stockReal);
        }
        else {
            bd.updateProducte(idProduct,descripcio,PVPreal,stockReal);

        }

        Intent mIntent = new Intent();
        mIntent.putExtra("id", idProduct);
        setResult(RESULT_OK, mIntent);

        finish();
    }

    private void cancelarCambios() {
        Intent mIntent = new Intent();
        mIntent.putExtra("id", idProduct);
        setResult(RESULT_CANCELED, mIntent);

        finish();
    }

    private void deleteProduct() {

        // Pedimos confirmación
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("¿Segur que desitja eliminar la tasca?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                bd.deleteProducte(idProduct);

                Intent mIntent = new Intent();
                mIntent.putExtra("id", -1);  // Devolvemos -1 indicant que s'ha eliminat
                setResult(RESULT_OK, mIntent);

                finish();
            }
        });

        builder.setNegativeButton("No", null);

        builder.show();

    }

}
