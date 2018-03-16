package com.example.morgan.catalogmanager2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);



        Button btnCatalogo = (Button)findViewById(R.id.btnProductos);
        Button btnMovimientos = (Button)findViewById(R.id.btnMovimientos);
        Button btnTiempo = (Button)findViewById(R.id.btnTiempo);

        btnCatalogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        btnMovimientos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(IntroActivity.this, MovementsActivity.class);
                startActivity(i);
            }
        });

        btnTiempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(IntroActivity.this, WeatherActivity.class);
                startActivity(i);
            }
        });
    }


}
