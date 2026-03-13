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

        RecyclerView rv = findViewById(R.id.rvPlatos);
        rv.setLayoutManager(new LinearLayoutManager(this));

        List<Plato> listaPlatos = new ArrayList<>();
        listaPlatos.add(new Plato("Pizza Margarita", "Tomate, mozzarella y albahaca fresca.", 10.50));
        listaPlatos.add(new Plato("Pasta Carbonara", "Pasta con crema, huevo, guanciale y pimienta.", 12.00));
        listaPlatos.add(new Plato("Lasaña de Carne", "Capas de pasta con carne picada y bechamel.", 14.50));
        listaPlatos.add(new Plato("Ensalada César", "Lechuga, pollo, croutons y salsa césar.", 9.00));
        listaPlatos.add(new Plato("Risotto de Setas", "Arroz cremoso con variedad de setas silvestres.", 13.00));

        PlatoAdapter adapter = new PlatoAdapter(listaPlatos);
        rv.setAdapter(adapter);

        // Botón para ir a ver el resumen del pedido
        Button btnPedido = findViewById(R.id.btnVerPedido);
        btnPedido.setOnClickListener(v -> {
            Intent intent = new Intent(this, PedidoActivity.class);
            startActivity(intent);
        });
    }
}
