package com.restaurante.app.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.restaurante.app.R;

/**
 * PANTALLA DE SELECCIÓN DE CATEGORÍAS
 * Permite al usuario elegir qué tipo de productos desea visualizar (Entrantes, Principales, etc.).
 */
public class CategoriasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        MaterialCardView cardEntrantes   = findViewById(R.id.cardEntrantes);
        MaterialCardView cardPrincipales = findViewById(R.id.cardPrincipales);
        MaterialCardView cardBebidas     = findViewById(R.id.cardBebidas);
        MaterialCardView cardPostres     = findViewById(R.id.cardPostres);

        // Pasamos el nombre exacto que queremos que aparezca como título
        cardEntrantes.setOnClickListener(v   -> abrirCarta("Entrantes"));
        cardPrincipales.setOnClickListener(v -> abrirCarta("Principales"));
        cardBebidas.setOnClickListener(v     -> abrirCarta("Bebidas"));
        cardPostres.setOnClickListener(v     -> abrirCarta("Postres"));
    }

    private void abrirCarta(String categoria) {
        Intent intent = new Intent(this, CartaActivity.class);
        intent.putExtra("categoria_seleccionada", categoria);
        startActivity(intent);
    }
}
