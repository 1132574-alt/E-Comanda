package com.restaurante.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.restaurante.app.R;
import com.restaurante.app.model.Carrito;
import com.restaurante.app.utils.Constants;

/**
 * PANTALLA PRINCIPAL (MODO CLIENTE/MESA)
 * Actúa como el centro de mando de la Tablet una vez que la sesión ha sido iniciada.
 * - Implementa el requisito de "Modo Kiosco" mediante el bloqueo del botón Atrás y el Modo Inmersivo, 
 *   asegurando que el cliente no salga de la aplicación del restaurante.
 * - Gestiona la comunicación directa con el personal de sala (Camareros) mediante notificaciones en tiempo real.
 * - Sirve de punto de entrada a las funcionalidades de Carta.
 */
public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance(Constants.DB_URL).getReference();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        aplicarModoInmersivo();
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Toast.makeText(MainActivity.this, "Modo mesa activo", Toast.LENGTH_SHORT).show();
            }
        });

        if (Carrito.getInstance().getIdSesionActual() == null) {
            Toast.makeText(this, "Error: No hay sesión activa.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Button btnVerCarta = findViewById(R.id.btnVerCarta);
        Button btnSeguimiento = findViewById(R.id.btnSeguimiento);
        Button btnLlamarCamarero = findViewById(R.id.btnLlamarCamarero);
        Button btnPedirCuenta = findViewById(R.id.btnPedirCuenta);

        btnVerCarta.setOnClickListener(v -> irACategorias());
        
        btnSeguimiento.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistorialActivity.class);
            startActivity(intent);
        });

        btnLlamarCamarero.setOnClickListener(v -> 
            confirmarAccion("Llamar al camarero", "¿Deseas llamar al camarero?", "pedidoCamarero"));
            
        btnPedirCuenta.setOnClickListener(v -> 
            confirmarAccion("Pedir la cuenta", "¿Deseas solicitar la cuenta?", "pedidoCuenta"));
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

    private void irACategorias() {
        Intent intent = new Intent(MainActivity.this, CategoriasActivity.class);
        startActivity(intent);
    }

    private void confirmarAccion(String titulo, String pregunta, String campoFirebase) {
        new AlertDialog.Builder(this)
                .setTitle(titulo)
                .setMessage(pregunta)
                .setPositiveButton("Sí", (dialog, which) -> enviarNotificacionAFirebase(campoFirebase))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void enviarNotificacionAFirebase(String campo) {
        String idSesion = Carrito.getInstance().getIdSesionActual();
        if (idSesion != null) {
            mDatabase.child("sesiones_mesa").child(idSesion).child(campo).setValue(true)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(MainActivity.this, "Solicitud enviada", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firebase_Error", "Error: " + e.getMessage());
                        Toast.makeText(MainActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
