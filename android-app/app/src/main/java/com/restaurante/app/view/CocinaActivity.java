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
import com.restaurante.app.adapter.CocinaAdapter;
import com.restaurante.app.model.Comanda;
import com.restaurante.app.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * PANTALLA DE COCINA
 * Interfaz dedicada al personal de cocina para la gestión y preparación de pedidos.
 */
public class CocinaActivity extends AppCompatActivity {

    private RecyclerView rvCocina;
    private CocinaAdapter adapter;
    private List<Comanda> listaComandas;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocina);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        aplicarModoInmersivo();
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        mDatabase = FirebaseDatabase.getInstance(Constants.DB_URL).getReference("comandas");

        rvCocina = findViewById(R.id.rvCocina);
        rvCocina.setLayoutManager(new LinearLayoutManager(this));

        listaComandas = new ArrayList<>();
        adapter = new CocinaAdapter(listaComandas, mDatabase);
        rvCocina.setAdapter(adapter);

        obtenerPedidosActivos();
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

    private void obtenerPedidosActivos() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaComandas.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Comanda c = data.getValue(Comanda.class);
                    if (c != null) {
                        String estado = c.getEstado();
                        if ("SOLICITADO".equals(estado) || "EN PREPARACIÓN".equals(estado)) {
                            listaComandas.add(c);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Cocina_Error", error.getMessage());
            }
        });
    }
}
