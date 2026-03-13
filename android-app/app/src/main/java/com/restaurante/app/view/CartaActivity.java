package com.restaurante.app.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.restaurante.app.R;
import com.restaurante.app.model.Plato;

import java.util.ArrayList;
import java.util.List;

public class CartaActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_carta);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Recibir la categoría seleccionada
        String categoriaRecibida = getIntent().getStringExtra("categoria_seleccionada");

        RecyclerView rv = findViewById(R.id.rvPlatos);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // 2. Crear la lista completa de platos
        List<Plato> todosLosPlatos = new ArrayList<>();
        todosLosPlatos.add(new Plato("Pizza Margarita", "Tomate y mozzarella.", 10.50, "Principal"));
        todosLosPlatos.add(new Plato("Pasta Carbonara", "Huevo y guanciale.", 12.00, "Principal"));
        todosLosPlatos.add(new Plato("Ensalada César", "Pollo y salsa césar.", 9.00, "Entrante"));
        todosLosPlatos.add(new Plato("Cerveza 33cl", "Rubia muy fría.", 3.50, "Bebida"));
        todosLosPlatos.add(new Plato("Refresco Cola", "Con hielo y limón.", 2.50, "Bebida"));
        todosLosPlatos.add(new Plato("Tarta de Queso", "Casera con arándanos.", 5.50, "Postre"));

        // 3. Filtrar la lista según la categoría
        List<Plato> platosFiltrados = new ArrayList<>();
        for (Plato p : todosLosPlatos) {
            if (p.getCategoria().equals(categoriaRecibida)) {
                platosFiltrados.add(p);
            }
        }

        // 4. Pasar la lista filtrada al adaptador
        PlatoAdapter adapter = new PlatoAdapter(platosFiltrados);
        rv.setAdapter(adapter);

        Button btnPedido = findViewById(R.id.btnVerPedido);
        btnPedido.setOnClickListener(v -> {
            Intent intent = new Intent(this, PedidoActivity.class);
            startActivity(intent);
        });
    }
}
