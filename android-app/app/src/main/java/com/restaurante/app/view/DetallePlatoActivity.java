package com.restaurante.app.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.restaurante.app.R;
import com.restaurante.app.model.Carrito;
import com.restaurante.app.model.Plato;

public class DetallePlatoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_plato);

        // 1. Obtener objeto Plato del Intent
        Plato plato = (Plato) getIntent().getSerializableExtra("plato");

        // 2. Enlazar vistas
        TextView txtNombre = findViewById(R.id.txtNombreDetalle);
        TextView txtPrecio = findViewById(R.id.txtPrecioDetalle);
        TextView txtDescripcion = findViewById(R.id.txtDescripcionDetalle);
        Button btnAnadir = findViewById(R.id.btnAnadirCarrito);

        // 3. Asignar valores si el plato existe
        if (plato != null) {
            txtNombre.setText(plato.getNombre());
            txtPrecio.setText(plato.getPrecioFormateado());
            txtDescripcion.setText(plato.getDescripcion());
        }

        // 4. Añadir al carrito real
        btnAnadir.setOnClickListener(v -> {
            if (plato != null) {
                Carrito.getInstance().agregarPlato(plato);
                Toast.makeText(this, plato.getNombre() + " añadido al pedido", Toast.LENGTH_SHORT).show();
                finish(); 
            }
        });
    }
}
