package com.restaurante.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.restaurante.app.R;
import com.restaurante.app.model.Producto;

import java.util.List;

/**
 * ADAPTER PARA LA GESTIÓN DE STOCK
 * Permite al personal de cocina habilitar o deshabilitar productos.
 * - Interactúa directamente con el campo 'disponible' de la entidad Producto.
 */
public class GestionStockAdapter extends RecyclerView.Adapter<GestionStockAdapter.StockViewHolder> {

    private List<Producto> listaProductos;
    private DatabaseReference mDatabase;

    public GestionStockAdapter(List<Producto> listaProductos, DatabaseReference mDatabase) {
        this.listaProductos = listaProductos;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gestion_stock, parent, false);
        return new StockViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        Producto producto = listaProductos.get(position);

        holder.txtNombre.setText(producto.getNombre());
        holder.txtCategoria.setText(producto.getCategoria());
        
        // Configuramos el switch según el estado actual en la BD
        holder.swDisponible.setOnCheckedChangeListener(null); // Evitamos disparos accidentales al reciclar
        holder.swDisponible.setChecked(producto.isDisponible());

        holder.swDisponible.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Actualizamos en Firebase
            mDatabase.child(producto.getIdProducto()).child("disponible").setValue(isChecked)
                    .addOnSuccessListener(aVoid -> {
                        String msg = isChecked ? "Producto disponible" : "Producto agotado";
                        Toast.makeText(buttonView.getContext(), msg, Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    static class StockViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtCategoria;
        Switch swDisponible;

        public StockViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreProductoStock);
            txtCategoria = itemView.findViewById(R.id.txtCategoriaProductoStock);
            swDisponible = itemView.findViewById(R.id.switchDisponible);
        }
    }
}
