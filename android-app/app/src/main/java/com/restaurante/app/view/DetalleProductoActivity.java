package com.restaurante.app.view;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.restaurante.app.R;
import com.restaurante.app.model.Carrito;
import com.restaurante.app.model.Producto;

/**
 * PANTALLA DE DETALLE DEL PRODUCTO
 * Permite al cliente ver la información ampliada de un plato y personalizar su pedido.
 */
public class DetalleProductoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        aplicarModoInmersivo();
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        Producto producto = (Producto) getIntent().getSerializableExtra("producto");

        if (producto == null) {
            Toast.makeText(this, "Error: Producto no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        TextView txtNombre      = findViewById(R.id.txtNombreDetalle);
        TextView txtPrecio      = findViewById(R.id.txtPrecioDetalle);
        TextView txtDescripcion = findViewById(R.id.txtDescripcionDetalle);
        EditText etComentario   = findViewById(R.id.etComentario);
        Button btnAnadir        = findViewById(R.id.btnAnadirCarrito);

        txtNombre.setText(producto.getNombre());
        txtDescripcion.setText(producto.getDescripcion());
        txtPrecio.setText(String.format("%.2f€", producto.getPrecio()));

        if (!producto.isDisponible()) {
            btnAnadir.setEnabled(false);
            btnAnadir.setText("PRODUCTO AGOTADO");
            btnAnadir.setBackgroundColor(Color.GRAY);
            etComentario.setEnabled(false);
            etComentario.setHint("No disponible");
        }

        btnAnadir.setOnClickListener(v -> {
            String comentario = etComentario.getText().toString().trim();

            if (Carrito.getInstance().getIdSesionActual() == null) {
                Toast.makeText(this, "Error: No hay sesión de mesa activa", Toast.LENGTH_LONG).show();
                return;
            }

            Carrito.getInstance().agregarProducto(producto, 1, comentario);
            Toast.makeText(this, producto.getNombre() + " añadido al pedido", Toast.LENGTH_SHORT).show();
            finish();
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
}
