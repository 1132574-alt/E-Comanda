package com.restaurante.app.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

/**
 * PANTALLA DE COCINA
 * Interfaz dedicada al personal de cocina para la gestión y preparación de pedidos.
 * - Implementa el rol de Cocinero dentro del ecosistema del restaurante.
 * - Permite el avance del ciclo de vida de la Comanda, cambiando su estado
 *   para informar al cliente sobre el progreso de su comida.
 */
public class CocinaActivity extends AppCompatActivity {

    private RecyclerView rvCocina;
    private CocinaAdapter adapter;
    private List<Comanda> listaComandas;
    private DatabaseReference mDatabase;

    private static final String DB_URL = "https://e-comanda-aa795-default-rtdb.europe-west1.firebasedatabase.app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocina);

        // Referenciamos el nodo de comandas en la base de datos NoSQL
        mDatabase = FirebaseDatabase.getInstance(DB_URL).getReference("comandas");

        rvCocina = findViewById(R.id.rvCocina);
        rvCocina.setLayoutManager(new LinearLayoutManager(this));

        listaComandas = new ArrayList<>();
        adapter = new CocinaAdapter(listaComandas, mDatabase);
        rvCocina.setAdapter(adapter);

        obtenerPedidosActivos();
    }

    /**
     * ESCUCHA ACTIVA DE PEDIDOS
     * Aplica lógica de filtrado para mostrar únicamente los pedidos que requieren acción inmediata
     * (SOLICITADO o EN PREPARACIÓN), ocultando los ya finalizados o cancelados para no saturar la vista.
     */
    private void obtenerPedidosActivos() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaComandas.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Comanda c = data.getValue(Comanda.class);
                    if (c != null) {
                        String estado = c.getEstado();
                        // Filtro de negocio: Solo pedidos pendientes de servir
                        if ("SOLICITADO".equals(estado) || "EN PREPARACIÓN".equals(estado)) {
                            listaComandas.add(c);
                        }
                    }
                }
                // Notificamos al adaptador para refrescar la lista en pantalla
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Cocina_Error", error.getMessage());
            }
        });
    }
}
