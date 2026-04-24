package com.restaurante.app.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.restaurante.app.R;
import com.restaurante.app.model.Comanda;
import com.restaurante.app.model.LineaComanda;

import java.util.List;

/**
 * ADAPTER PARA LA VISTA DE COCINA
 * Este adaptador se encarga de gestionar cómo se muestran los pedidos (Comandas) en la lista del cocinero.
 * Implementa la lógica de visualización de la entidad Comanda y permite la transición de estados
 * (de SOLICITADO a EN PREPARACIÓN y de ahí a LISTO).
 */
public class CocinaAdapter extends RecyclerView.Adapter<CocinaAdapter.CocinaViewHolder> {

    private List<Comanda> listaComandas;
    private DatabaseReference mDatabase;

    public CocinaAdapter(List<Comanda> listaComandas, DatabaseReference mDatabase) {
        this.listaComandas = listaComandas;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public CocinaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflamos el diseño individual de cada item de cocina (definido en item_cocina.xml)
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cocina, parent, false);
        return new CocinaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CocinaViewHolder holder, int position) {
        Comanda comanda = listaComandas.get(position);

        // Mostramos los datos básicos de la Comanda
        holder.txtMesa.setText("Mesa: " + comanda.getIdMesa());
        holder.txtEstado.setText("Estado: " + comanda.getEstado());

        if ("SOLICITADO".equals(comanda.getEstado())) {
            holder.txtEstado.setTextColor(Color.BLUE);
            holder.btnAccion.setText("EMPEZAR A COCINAR");
            holder.btnAccion.setBackgroundColor(Color.parseColor("#FF9800")); // Naranja
        } else {
            holder.txtEstado.setTextColor(Color.parseColor("#FF9800"));
            holder.btnAccion.setText("MARCAR COMO LISTO");
            holder.btnAccion.setBackgroundColor(Color.parseColor("#4CAF50")); // Verde
        }

        // Listamos los productos que componen la comanda.
        StringBuilder sb = new StringBuilder();
        if (comanda.getLineas() != null) {
            for (LineaComanda linea : comanda.getLineas()) {
                sb.append("• ").append(linea.getCantidad()).append("x ")
                        .append(linea.getProducto().getNombre());
                if (linea.getComentario() != null && !linea.getComentario().isEmpty()) {
                    sb.append(" (").append(linea.getComentario()).append(")");
                }
                sb.append("\n");
            }
        }
        holder.txtDetalle.setText(sb.toString());

        holder.btnAccion.setOnClickListener(v -> {
            String nuevoEstado = "SOLICITADO".equals(comanda.getEstado()) ? "EN PREPARACIÓN" : "LISTO";

            mDatabase.child(comanda.getIdComanda()).child("estado").setValue(nuevoEstado)
                    .addOnSuccessListener(aVoid -> Toast.makeText(v.getContext(), "Pedido actualizado", Toast.LENGTH_SHORT).show());
        });
    }

    @Override
    public int getItemCount() {
        return listaComandas.size();
    }

    static class CocinaViewHolder extends RecyclerView.ViewHolder {
        TextView txtMesa, txtDetalle, txtEstado;
        Button btnAccion;

        public CocinaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMesa = itemView.findViewById(R.id.txtMesaCocina);
            txtDetalle = itemView.findViewById(R.id.txtDetallePedidoCocina);
            txtEstado = itemView.findViewById(R.id.txtEstadoPedidoCocina);
            btnAccion = itemView.findViewById(R.id.btnMarcarServido);
        }
    }
}
