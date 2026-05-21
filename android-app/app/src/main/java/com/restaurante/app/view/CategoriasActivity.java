package com.restaurante.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.activity.OnBackPressedCallback;
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

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        aplicarModoInmersivo();
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        MaterialCardView cardEntrantes   = findViewById(R.id.cardEntrantes);
        MaterialCardView cardPrincipales = findViewById(R.id.cardPrincipales);
        MaterialCardView cardBebidas     = findViewById(R.id.cardBebidas);
        MaterialCardView cardPostres     = findViewById(R.id.cardPostres);

        cardEntrantes.setOnClickListener(v   -> abrirCarta("Entrantes"));
        cardPrincipales.setOnClickListener(v -> abrirCarta("Principales"));
        cardBebidas.setOnClickListener(v     -> abrirCarta("Bebidas"));
        cardPostres.setOnClickListener(v     -> abrirCarta("Postres"));
    }

    private void aplicarModoInmersivo() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            aplicarModoInmersivo();
        }
    }

    private void abrirCarta(String categoria) {
        Intent intent = new Intent(this, CartaActivity.class);
        intent.putExtra("categoria_seleccionada", categoria);
        startActivity(intent);
    }
}
