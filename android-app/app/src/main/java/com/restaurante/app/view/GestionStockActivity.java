package com.restaurante.app.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

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
import com.restaurante.app.adapter.GestionStockAdapter;
import com.restaurante.app.model.Producto;
import com.restaurante.app.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * ACTIVIDAD DE GESTIÓN DE STOCK
 * Permite al personal de cocina marcar productos como disponibles o agotados.
 */
public class GestionStockActivity extends AppCompatActivity {

    private RecyclerView rvStock;
    private GestionStockAdapter adapter;
    private List<Producto> listaProductos;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_stock);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        aplicarModoInmersivo();
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        mDatabase = FirebaseDatabase.getInstance(Constants.DB_URL).getReference("productos");

        rvStock = findViewById(R.id.rvGestionStock);
        rvStock.setLayoutManager(new LinearLayoutManager(this));

        listaProductos = new ArrayList<>();
        adapter = new GestionStockAdapter(listaProductos, mDatabase);
        rvStock.setAdapter(adapter);

        cargarProductos();
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
