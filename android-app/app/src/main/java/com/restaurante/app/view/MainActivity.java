package com.restaurante.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.restaurante.app.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Enlazamos el botón del XML con Java
        Button btn = findViewById(R.id.btnVerCarta);

        // 2. Programamos el clic
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 3. Ahora vamos primero a la selección de CATEGORÍAS
                Intent intent = new Intent(MainActivity.this, CategoriasActivity.class);
                startActivity(intent);
            }
        });
    }
}
