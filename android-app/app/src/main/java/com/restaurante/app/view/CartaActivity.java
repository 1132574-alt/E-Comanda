package com.restaurante.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.restaurante.app.R;
import com.restaurante.app.adapter.ProductoAdapter;
import com.restaurante.app.model.Producto;
import com.restaurante.app.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * PANTALLA DE CARTA / MENÚ
 * Muestra los productos disponibles filtrados por la categoría seleccionada previamente.
 * - Implementa la funcionalidad de "Visualización del Catálogo".
 * - Permite al cliente navegar hacia el Carrito de compras.
 */
public class CartaActivity extends AppCompatActivity {

    private ProductoAdapter adapter;
    private List<Producto> productosFiltrados;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carta);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        aplicarModoInmersivo();
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        String categoriaRecibida = getIntent().getStringExtra("categoria_seleccionada");
        
        TextView txtTituloCarta = findViewById(R.id.txtTituloCarta);
        if (txtTituloCarta != null && categoriaRecibida != null) {
            txtTituloCarta.setText(categoriaRecibida);
        }

        RecyclerView rv = findViewById(R.id.rvPlatos);
        rv.setLayoutManager(new LinearLayoutManager(this));

        productosFiltrados = new ArrayList<>();
        adapter = new ProductoAdapter(productosFiltrados);
        rv.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance(Constants.DB_URL).getReference("productos");

        crearProductosDePrueba();

        cargarProductosFiltrados(categoriaRecibida);

        Button btnPedido = findViewById(R.id.btnVerPedido);
        btnPedido.setOnClickListener(v -> {
            Intent intent = new Intent(this, PedidoActivity.class);
            startActivity(intent);
        });
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

    /**
     * Consulta a Firebase filtrada por categoría.
     * Demuestra la integración del sistema con servicios en la nube para el mantenimiento de la carta en tiempo real.
     */
    private void cargarProductosFiltrados(String categoria) {
        if (categoria == null) return;

        mDatabase.orderByChild("categoria").equalTo(categoria)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        productosFiltrados.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Producto p = data.getValue(Producto.class);
                            if (p != null) {
                                productosFiltrados.add(p);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase_Carta", "Error: " + error.getMessage());
                    }
                });
    }

    private void crearProductosDePrueba() {
        // Limpiamos para evitar duplicados de ejecuciones anteriores
        mDatabase.removeValue();

        List<Producto> lista = new ArrayList<>();
        lista.add(new Producto("p1", "Ensalada César", "Lechuga, pollo, picatostes y salsa", 8.50, "Entrantes", true));
        lista.add(new Producto("p2", "Croquetas Caseras", "6 unidades de jamón ibérico", 7.00, "Entrantes", true));

        lista.add(new Producto("p3", "Hamburguesa Gourmet", "Carne de buey y queso brie", 12.90, "Principales", true));
        lista.add(new Producto("p12", "Hamburguesa de la casa", "Carne picada de calidad con extra de bacon", 11.50, "Principales", true));
        lista.add(new Producto("p4", "Pizza Barbacoa", "Pollo, bacón y salsa barbacoa", 10.50, "Principales", true));
        lista.add(new Producto("p11", "Entrecot de Ternera", "300g de carne a la brasa con patatas", 18.00, "Principales", false)); // MARCADOR AGOTADO PARA TFG

        lista.add(new Producto("p5", "Refresco", "Cola, Naranja o Limón", 2.50, "Bebidas", true));
        lista.add(new Producto("p6", "Cerveza", "Tercio de Mahou", 3.00, "Bebidas", true));
        lista.add(new Producto("p9", "Copa de Vino Tinto", "Rioja Crianza de la casa", 3.50, "Bebidas", true));

        lista.add(new Producto("p7", "Tarta de Queso", "Con mermelada de arándanos", 5.50, "Postres", true));
        lista.add(new Producto("p10", "Tarta de la Abuela", "Galleta, chocolate y natillas caseras", 5.00, "Postres", true));
        lista.add(new Producto("p8", "Brownie con helado", "Helado de vainilla y chocolate", 6.00, "Postres", true));

        for (Producto p : lista) {
            mDatabase.child(p.getIdProducto()).setValue(p);
        }
    }
}
