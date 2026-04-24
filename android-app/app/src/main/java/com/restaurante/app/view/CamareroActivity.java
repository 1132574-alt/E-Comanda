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
import com.restaurante.app.adapter.CamareroAdapter;
import com.restaurante.app.model.SesionMesa;

import java.util.ArrayList;
import java.util.List;

/**
 * ACTIVIDAD PRINCIPAL PARA CAMAREROS
 * Monitoriza en tiempo real las peticiones de las mesas activas.
 * - Se aplica un filtro doble: Solo se muestran mesas cuya sesión esté "ACTIVA" 
 *   Y que tengan algún aviso pendiente (pedidoCamarero o pedidoCuenta).
 * - Esto garantiza que una vez que el camarero cierra la mesa, esta desaparezca 
 *   automáticamente del panel de control.
 */
public class CamareroActivity extends AppCompatActivity {

    private RecyclerView rvAvisos;
    private CamareroAdapter adapter;
    private List<SesionMesa> listaSesiones;
    private DatabaseReference mDatabase;

    private static final String DB_URL = "https://e-comanda-aa795-default-rtdb.europe-west1.firebasedatabase.app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camarero);

        mDatabase = FirebaseDatabase.getInstance(DB_URL).getReference("sesiones_mesa");

        rvAvisos = findViewById(R.id.rvAvisosCamarero);
        rvAvisos.setLayoutManager(new LinearLayoutManager(this));

        listaSesiones = new ArrayList<>();
        adapter = new CamareroAdapter(listaSesiones, mDatabase);
        rvAvisos.setAdapter(adapter);

        escucharAvisos();
    }

    /**
     * ESCUCHA EN TIEMPO REAL CON FILTRADO
     */
    private void escucharAvisos() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaSesiones.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    SesionMesa sesion = data.getValue(SesionMesa.class);
                    if (sesion != null) {
                        // FILTRO CRÍTICO: Solo mesas ACTIVAS con avisos pendientes
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
