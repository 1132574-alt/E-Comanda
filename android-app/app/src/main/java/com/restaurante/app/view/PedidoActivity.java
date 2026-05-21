package com.restaurante.app.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.restaurante.app.R;
import com.restaurante.app.adapter.LineaComandaAdapter;
import com.restaurante.app.model.Carrito;
import com.restaurante.app.model.Comanda;
import com.restaurante.app.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * PANTALLA DE REVISIÓN Y ENVÍO DE PEDIDO (CARRITO)
 * Permite al cliente revisar los productos seleccionados antes de enviarlos definitivamente a cocina.
 */
public class PedidoActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private Carrito carrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        aplicarModoInmersivo();
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        mDatabase = FirebaseDatabase.getInstance(Constants.DB_URL).getReference();
        carrito = Carrito.getInstance();
        Comanda comandaActual = carrito.getComandaActual();

        RecyclerView rv = findViewById(R.id.rvPedido);
        TextView txtTotalActual = findViewById(R.id.txtTotalPedido);
        TextView txtCuentaTotal = findViewById(R.id.txtCuentaActual);
        Button btnConfirmar = findViewById(R.id.btnConfirmarPedido);

        rv.setLayoutManager(new LinearLayoutManager(this));
        LineaComandaAdapter adapter = new LineaComandaAdapter(comandaActual.getLineas());
        rv.setAdapter(adapter);

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

    private void procesarEnvioComanda(Comanda comandaAEnviar) {
        String idSesionActual = carrito.getIdSesionActual();
        String idMesaActual = carrito.getIdMesa();

        if (idSesionActual == null) {
            Toast.makeText(this, "Error: No hay sesión de mesa activa", Toast.LENGTH_SHORT).show();
            return;
        }

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

    private String obtenerFechaActual() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
