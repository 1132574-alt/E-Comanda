package com.restaurante.app.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.restaurante.app.R;
import com.restaurante.app.model.Producto;
import com.restaurante.app.view.DetalleProductoActivity;

import java.util.List;

/**
 * ADAPTER PARA LA CARTA DE PRODUCTOS
 * Gestiona la visualización de los platos y bebidas disponibles en la carta.
 * - Facilita la navegación hacia la pantalla de detalles del producto.
 */
public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {
    private List<Producto> productos;

    public ProductoAdapter(List<Producto> productos) {
        this.productos = productos;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = productos.get(position);

        holder.nombre.setText(producto.getNombre());
        holder.precio.setText(String.format("%.2f€", producto.getPrecio()));

        // GESTIÓN DE DISPONIBILIDAD (Control de Stock)
        // Relación TFG: Garantiza que la experiencia del cliente sea fluida evitando errores de pedido por falta de ingredientes.
        if (producto.isDisponible()) {
            holder.itemView.setAlpha(1.0f);
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), DetalleProductoActivity.class);
                intent.putExtra("producto", producto);
                v.getContext().startActivity(intent);
            });
            if (holder.txtEstado != null) holder.txtEstado.setVisibility(View.GONE);
        } else {
            holder.itemView.setAlpha(0.5f); // Efecto visual de deshabilitado
            holder.itemView.setOnClickListener(v -> {
                Toast.makeText(v.getContext(), "Producto temporalmente no disponible", Toast.LENGTH_SHORT).show();
            });
            
            if (holder.txtEstado != null) {
                holder.txtEstado.setVisibility(View.VISIBLE);
                holder.txtEstado.setText("AGOTADO");
                holder.txtEstado.setTextColor(Color.RED);
            }
        }
    }

    @Override
    public int getItemCount() { return productos.size(); }

    static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, precio, txtEstado;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.txtNombrePlato);
            precio = itemView.findViewById(R.id.txtPrecioPlato);
            txtEstado = itemView.findViewById(R.id.txtEstadoProducto);
        }
    }
}
