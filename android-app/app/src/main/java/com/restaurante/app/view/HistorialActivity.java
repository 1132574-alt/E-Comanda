package com.restaurante.app.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.restaurante.app.R;
import com.restaurante.app.adapter.ComandaHistorialAdapter;
import com.restaurante.app.model.Carrito;
import com.restaurante.app.model.Comanda;
import com.restaurante.app.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * PANTALLA DE SEGUIMIENTO DE PEDIDOS (HISTORIAL)
 * Permite al cliente ver el estado de sus pedidos en tiempo real.
 * - Garantiza la privacidad de los datos: el filtro por 'idSesion' asegura que una mesa solo vea sus propios pedidos 
 *   y no los de otras mesas del restaurante.
 */
public class HistorialActivity extends AppCompatActivity {

    private RecyclerView rv;
    private ComandaHistorialAdapter adapter;
    private List<Comanda> listaComandas;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        aplicarModoInmersivo();
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        mDatabase = FirebaseDatabase.getInstance(Constants.DB_URL).getReference().child("comandas");
        listaComandas = new ArrayList<>();

        rv = findViewById(R.id.rvHistorial);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ComandaHistorialAdapter(listaComandas, this::confirmarCancelacion);
        rv.setAdapter(adapter);

        escucharPedidosEnTiempoReal();
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
     * CONSULTA FILTRADA POR SESIÓN
     */
    private void escucharPedidosEnTiempoReal() {
        String idSesion = Carrito.getInstance().getIdSesionActual();
        if (idSesion == null) {
            Toast.makeText(this, "Error: No se ha detectado una sesión activa", Toast.LENGTH_SHORT).show();
            return;
        }

        // Escuchamos solo los pedidos que pertenecen a ESTA sesión actual de la mesa
        mDatabase.orderByChild("idSesion").equalTo(idSesion)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listaComandas.clear();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            Comanda comanda = postSnapshot.getValue(Comanda.class);
                            if (comanda != null) {
                                listaComandas.add(comanda);
                            }
                        }
                        // Ordenamos para que los pedidos más nuevos aparezcan arriba
                        Collections.reverse(listaComandas);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase_Error", "Fallo al leer pedidos: " + error.getMessage());
                    }
                });
    }

    /**
     * Lógica de cancelación controlada por el estado de la entidad.
     */
    private void confirmarCancelacion(Comanda comanda) {
        new AlertDialog.Builder(this)
                .setTitle("Cancelar Pedido")
                .setMessage("¿Estás seguro de que deseas cancelar este pedido?")
                .setPositiveButton("Sí, cancelar", (dialog, which) -> cancelarPedido(comanda))
                .setNegativeButton("No", null)
                .show();
    }

    private void cancelarPedido(Comanda comanda) {
        // Validación de regla de negocio definida en el TFG
        if ("SOLICITADO".equals(comanda.getEstado())) {
            mDatabase.child(comanda.getIdComanda()).removeValue()
                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Pedido cancelado", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "No se puede cancelar un pedido que ya está en cocina", Toast.LENGTH_LONG).show();
        }
    }
}
