package com.restaurante.app.view;

import android.os.Bundle;
import android.util.Log;

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
import com.restaurante.app.adapter.GestionStockAdapter;
import com.restaurante.app.model.Producto;

import java.util.ArrayList;
import java.util.List;

/**
 * ACTIVIDAD DE GESTIÓN DE STOCK
 * Permite al personal de cocina marcar productos como disponibles o agotados.
 * - Implementa el mantenimiento del catálogo de productos.
 * - Los cambios impactan directamente en lo que los clientes pueden pedir desde sus tablets.
 */
public class GestionStockActivity extends AppCompatActivity {

    private RecyclerView rvStock;
    private GestionStockAdapter adapter;
    private List<Producto> listaProductos;
    private DatabaseReference mDatabase;

    private static final String DB_URL = "https://e-comanda-aa795-default-rtdb.europe-west1.firebasedatabase.app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_stock);

        mDatabase = FirebaseDatabase.getInstance(DB_URL).getReference("productos");

        rvStock = findViewById(R.id.rvGestionStock);
        rvStock.setLayoutManager(new LinearLayoutManager(this));

        listaProductos = new ArrayList<>();
        adapter = new GestionStockAdapter(listaProductos, mDatabase);
        rvStock.setAdapter(adapter);

        cargarProductos();
    }

    private void cargarProductos() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaProductos.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Producto p = data.getValue(Producto.class);
                    if (p != null) {
                        listaProductos.add(p);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Stock_Error", error.getMessage());
            }
        });
    }
}
