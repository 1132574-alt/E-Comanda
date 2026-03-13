package com.restaurante.app.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.restaurante.app.R;
import com.restaurante.app.model.Carrito;

public class PedidoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        RecyclerView rv = findViewById(R.id.rvPedido);
        TextView txtTotal = findViewById(R.id.txtTotalPedido);
        Button btnConfirmar = findViewById(R.id.btnConfirmarPedido);

        // Configurar RecyclerView
        rv.setLayoutManager(new LinearLayoutManager(this));
        // Reutilizamos el adaptador de platos para mostrar el resumen
        PlatoAdapter adapter = new PlatoAdapter(Carrito.getInstance().getProductos());
        rv.setAdapter(adapter);

        // Mostrar total
        txtTotal.setText(String.format("%.2f€", Carrito.getInstance().getTotal()));

        // Acción confirmar
        btnConfirmar.setOnClickListener(v -> {
            if (Carrito.getInstance().getProductos().isEmpty()) {
                Toast.makeText(this, "El pedido está vacío", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "¡Pedido enviado a cocina!", Toast.LENGTH_LONG).show();
                Carrito.getInstance().limpiarCarrito();
                finish(); // Volver a la carta
            }
        });
    }
}
