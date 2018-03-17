package com.example.morgan.catalogmanager2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class InitialActivity extends AppCompatActivity {
    CMSQLiteHelper db = new CMSQLiteHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Button btnProductes = (Button) findViewById(R.id.btnProducts);
        Button btnMoviments = (Button) findViewById(R.id.btnMoviments);
        Button btnCiutats = (Button) findViewById(R.id.btnCiutats);

        btnProductes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(InitialActivity.this,MainActivity.class);
                startActivity(i);
            }
        });

        btnMoviments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(InitialActivity.this,MovementsActivity.class);
                startActivity(i);
            }
        });

        btnCiutats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(InitialActivity.this,CitiesActivity.class);
                startActivity(i);
            }
        });


    }

}
