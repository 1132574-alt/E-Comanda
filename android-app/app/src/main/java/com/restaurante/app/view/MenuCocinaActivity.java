package com.restaurante.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.restaurante.app.R;

/**
 * PANTALLA DE MENÚ PRINCIPAL PARA COCINA
 * Permite al personal elegir entre gestionar los pedidos entrantes o el stock de productos.
 * - Implementa la segregación de funciones dentro del rol de Cocina.
 * - Facilita el mantenimiento de la carta en tiempo real (disponibilidad de productos).
 */
public class MenuCocinaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_cocina);

        Button btnPedidos = findViewById(R.id.btnGestionPedidos);
        Button btnProductos = findViewById(R.id.btnGestionProductos);

        // Navegación hacia la gestión de comandas (ya existente)
        btnPedidos.setOnClickListener(v -> {
            Intent intent = new Intent(this, CocinaActivity.class);
            startActivity(intent);
        });

        // Navegación hacia la nueva gestión de stock de productos
        btnProductos.setOnClickListener(v -> {
            Intent intent = new Intent(this, GestionStockActivity.class);
            startActivity(intent);
        });
    }
}
