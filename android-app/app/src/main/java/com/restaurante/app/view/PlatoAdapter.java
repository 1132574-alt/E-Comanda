package com.restaurante.app.view;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.restaurante.app.R;
import com.restaurante.app.model.Plato;

import java.util.List;

public class PlatoAdapter extends RecyclerView.Adapter<PlatoAdapter.PlatoViewHolder> {
    private List<Plato> platos;

    public PlatoAdapter(List<Plato> platos) { this.platos = platos; }

    @NonNull
    @Override
    public PlatoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plato, parent, false);
        return new PlatoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlatoViewHolder holder, int position) {
        Plato plato = platos.get(position);
        
        holder.nombre.setText(plato.getNombre());
        holder.precio.setText(plato.getPrecioFormateado());

        // Al pulsar en el item, vamos al detalle pasando el objeto Plato
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetallePlatoActivity.class);
            intent.putExtra("plato", plato);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return platos.size(); }

    class PlatoViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, precio;
        public PlatoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.txtNombrePlato);
            precio = itemView.findViewById(R.id.txtPrecioPlato);
        }
    }
}
