package com.restaurante.app.view;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.restaurante.app.R;
import com.restaurante.app.model.Carrito;
import com.restaurante.app.model.Producto;

/**
 * PANTALLA DE DETALLE DEL PRODUCTO
 * Permite al cliente ver la información ampliada de un plato y personalizar su pedido.
 * 
 * Relación TFG:
 * - Implementa la funcionalidad de "Personalización de Pedido", permitiendo añadir comentarios (entidad LineaComanda).
 * - Refuerza la lógica de negocio de disponibilidad: si el producto no tiene stock, se inhabilita el botón de compra.
 * - Conecta la vista con el Carrito (Singleton) para persistir la selección antes del envío a Firebase.
 */
public class DetalleProductoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);

        // Recuperamos el objeto Producto seleccionado desde el Intent
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

            // Verificación de seguridad: debe existir una sesión activa vinculada a una mesa
            if (Carrito.getInstance().getIdSesionActual() == null) {
                Toast.makeText(this, "Error: No hay sesión de mesa activa", Toast.LENGTH_LONG).show();
                return;
            }

            // Registro en el Carrito temporal
            Carrito.getInstance().agregarProducto(producto, 1, comentario);
            Toast.makeText(this, producto.getNombre() + " añadido al pedido", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
