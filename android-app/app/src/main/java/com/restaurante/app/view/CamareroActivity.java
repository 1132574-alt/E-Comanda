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
import com.restaurante.app.adapter.CamareroAdapter;
import com.restaurante.app.model.SesionMesa;
import com.restaurante.app.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * ACTIVIDAD PRINCIPAL PARA CAMAREROS
 * Monitoriza en tiempo real las peticiones de las mesas activas.
 */
public class CamareroActivity extends AppCompatActivity {

    private RecyclerView rvAvisos;
    private CamareroAdapter adapter;
    private List<SesionMesa> listaSesiones;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camarero);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        aplicarModoInmersivo();
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        mDatabase = FirebaseDatabase.getInstance(Constants.DB_URL).getReference("sesiones_mesa");

        rvAvisos = findViewById(R.id.rvAvisosCamarero);
        rvAvisos.setLayoutManager(new LinearLayoutManager(this));

        listaSesiones = new ArrayList<>();
        adapter = new CamareroAdapter(listaSesiones, mDatabase);
        rvAvisos.setAdapter(adapter);

        escucharAvisos();
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

    private void escucharAvisos() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaSesiones.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    SesionMesa sesion = data.getValue(SesionMesa.class);
                    if (sesion != null) {
                        boolean esActiva = "ACTIVA".equals(sesion.getEstadoSesion());
                        boolean tieneAvisos = sesion.isPedidoCamarero() || sesion.isPedidoCuenta();
                        
                        if (esActiva && tieneAvisos) {
                            listaSesiones.add(sesion);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Camarero_Error", error.getMessage());
            }
        });
    }
}
