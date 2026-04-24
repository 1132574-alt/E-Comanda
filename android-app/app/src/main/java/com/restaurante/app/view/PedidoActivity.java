package com.restaurante.app.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.restaurante.app.R;
import com.restaurante.app.adapter.LineaComandaAdapter;
import com.restaurante.app.model.Carrito;
import com.restaurante.app.model.Comanda;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * PANTALLA DE REVISIÓN Y ENVÍO DE PEDIDO (CARRITO)
 * Permite al cliente revisar los productos seleccionados antes de enviarlos definitivamente a cocina.
 * 
 * Relación TFG:
 * - Implementa el proceso de persistencia de la entidad Comanda en la base de datos (Firebase).
 * - Gestiona el cálculo económico tanto del pedido actual como del acumulado de la sesión.
 * - Asegura la trazabilidad asignando automáticamente IDs, Marcas de tiempo y vinculando el pedido 
 *   con la SesionMesa actual.
 */
public class PedidoActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private Carrito carrito;
    
    // URL de la base de datos configurada para la región de Europa
    private static final String DB_URL = "https://e-comanda-aa795-default-rtdb.europe-west1.firebasedatabase.app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        mDatabase = FirebaseDatabase.getInstance(DB_URL).getReference();
        carrito = Carrito.getInstance();
        Comanda comandaActual = carrito.getComandaActual();

        RecyclerView rv = findViewById(R.id.rvPedido);
        TextView txtTotalActual = findViewById(R.id.txtTotalPedido);
        TextView txtCuentaTotal = findViewById(R.id.txtCuentaActual);
        Button btnConfirmar = findViewById(R.id.btnConfirmarPedido);

        rv.setLayoutManager(new LinearLayoutManager(this));
        LineaComandaAdapter adapter = new LineaComandaAdapter(comandaActual.getLineas());
        rv.setAdapter(adapter);

        // Visualización de importes (Cálculos derivados de las entidades del MER)
        double totalEstePedido = carrito.getTotalCarrito();
        double cuentaTotalSesion = carrito.getCuentaTotalSesion();

        txtTotalActual.setText(String.format(Locale.getDefault(), "Este pedido: %.2f€", totalEstePedido));
        
        if (txtCuentaTotal != null) {
            txtCuentaTotal.setText(String.format(Locale.getDefault(), "Cuenta total sesión: %.2f€", cuentaTotalSesion));
        }

        btnConfirmar.setOnClickListener(v -> {
            if (carrito.isEmpty()) {
                Toast.makeText(this, "El pedido está vacío", Toast.LENGTH_SHORT).show();
            } else {
                procesarEnvioComanda(comandaActual);
            }
        });
    }

    /**
     * PERSISTENCIA EN FIREBASE
     * Registra la comanda en el nodo 'comandas'.
     * Relación TFG: Establece el estado inicial 'SOLICITADO' y garantiza que el Carrito se limpie 
     * tras el éxito de la operación para evitar duplicidad de pedidos.
     */
    private void procesarEnvioComanda(Comanda comandaAEnviar) {
        String idSesionActual = carrito.getIdSesionActual();
        String idMesaActual = carrito.getIdMesa();

        if (idSesionActual == null) {
            Toast.makeText(this, "Error: No hay sesión de mesa activa", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generamos un ID único en Firebase para la nueva entidad Comanda
        String idGenerado = mDatabase.child("comandas").push().getKey();

        if (idGenerado != null) {
            comandaAEnviar.setIdComanda(idGenerado);
            comandaAEnviar.setIdSesion(idSesionActual);
            comandaAEnviar.setIdMesa(idMesaActual);
            comandaAEnviar.setEstado("SOLICITADO");
            comandaAEnviar.setFechaHoraEnvio(obtenerFechaActual());

            mDatabase.child("comandas").child(idGenerado).setValue(comandaAEnviar)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "¡Pedido enviado a cocina!", Toast.LENGTH_LONG).show();
                        // Guardamos en el histórico local para el cálculo de la cuenta total
                        carrito.añadirComandaAlHistorial(comandaAEnviar);
                        carrito.limpiarCarrito();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firebase_Error", "Error: " + e.getMessage());
                        Toast.makeText(this, "Fallo al enviar el pedido: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    /**
     * Formatea la fecha para cumplir con el estándar de trazabilidad del restaurante.
     */
    private String obtenerFechaActual() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
