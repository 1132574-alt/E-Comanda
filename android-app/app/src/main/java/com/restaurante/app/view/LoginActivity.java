package com.restaurante.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.restaurante.app.R;
import com.restaurante.app.model.Carrito;
import com.restaurante.app.model.SesionMesa;
import com.restaurante.app.utils.Constants;

/**
 * PANTALLA DE LOGIN / ACCESO AL SISTEMA
 * Esta clase gestiona el punto de entrada para todos los roles del sistema (Mesa, Cocina, Camarero).
 * Implementa el control de acceso y la inicialización de la entidad SesionMesa.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText etUsuario, etPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        aplicarModoInmersivo();
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance(Constants.DB_URL).getReference();

        etUsuario = findViewById(R.id.etUsuarioTablet);
        etPassword = findViewById(R.id.etPasswordTablet);
        Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> validarAcceso());
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
     * GESTIÓN DE ROLES POR NOMBRE DE USUARIO
     * Se utiliza una lógica simple para derivar el rol basándose en el nombre de usuario introducido.
     * - "cocina": Accede al panel de gestión de cocina (Menú Intermedio).
     * - "camarero": Accede al panel de avisos de mesas.
     */
    private void validarAcceso() {
        String user = etUsuario.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Rellena los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String emailFake = user.toLowerCase() + "@restaurante.com";

        mAuth.signInWithEmailAndPassword(emailFake, pass)
                .addOnSuccessListener(authResult -> {
                    String userLower = user.toLowerCase();
                    try {
                        startLockTask();
                    } catch (Exception e) {
                    }

                    if (userLower.equals("cocina")) {
                        irAMenuCocina();
                    } else if (userLower.equals("camarero")) {
                        irACamarero();
                    } else {
                        crearSesionMesa(user);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error de acceso: Credenciales incorrectas", Toast.LENGTH_LONG).show();
                });
    }

    /**
     * INICIALIZACIÓN DE ENTIDAD SESIÓN MESA
     * Esto garantiza que los pedidos se agrupen por sesión, permitiendo separar
     * el historial de diferentes clientes que usen la misma mesa física en momentos distintos.
     */
    private void crearSesionMesa(String nombreMesa) {
        String idSesion = mDatabase.child("sesiones_mesa").push().getKey();
        if (idSesion == null) idSesion = "LOCAL_" + System.currentTimeMillis();
        
        SesionMesa nuevaSesion = new SesionMesa(idSesion, nombreMesa);

        Carrito.getInstance().setIdSesionActual(idSesion);
        Carrito.getInstance().setIdMesa(nombreMesa);

        mDatabase.child("sesiones_mesa").child(idSesion).setValue(nuevaSesion)
                .addOnCompleteListener(task -> {
                    irAMain();
                });
    }

    private void irAMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void irAMenuCocina() {
        Intent intent = new Intent(this, MenuCocinaActivity.class);
        startActivity(intent);
        finish();
    }

    private void irACamarero() {
        Intent intent = new Intent(this, CamareroActivity.class);
        startActivity(intent);
        finish();
    }
}
