package com.restaurante.app.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.restaurante.app.R;

public class CategoriasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        // Enlazar Cards
        MaterialCardView cardEntrantes = findViewById(R.id.cardEntrantes);
        MaterialCardView cardPrincipales = findViewById(R.id.cardPrincipales);
        MaterialCardView cardBebidas = findViewById(R.id.cardBebidas);
        MaterialCardView cardPostres = findViewById(R.id.cardPostres);

        // Configurar clics
        cardEntrantes.setOnClickListener(v -> abrirCarta("Entrante"));
        cardPrincipales.setOnClickListener(v -> abrirCarta("Principal"));
        cardBebidas.setOnClickListener(v -> abrirCarta("Bebida"));
        cardPostres.setOnClickListener(v -> abrirCarta("Postre"));
    }

    private void abrirCarta(String categoria) {
        Intent intent = new Intent(this, CartaActivity.class);
        intent.putExtra("categoria_seleccionada", categoria);
        startActivity(intent);
    }
}
