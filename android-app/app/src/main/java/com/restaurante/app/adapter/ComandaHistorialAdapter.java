package com.restaurante.app.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.restaurante.app.R;
import com.restaurante.app.model.Comanda;

import java.util.List;

/**
 * ADAPTER PARA EL HISTORIAL DE PEDIDOS
 * Gestiona la lista de pedidos que el cliente ha realizado durante su estancia.
 * - Refleja los cambios de estado que realiza el cocinero (SOLICITADO -> EN PREPARACIÓN -> LISTO).
 * - Controla la regla de negocio de cancelación: un cliente solo puede cancelar un pedido si aún está en estado 'SOLICITADO'.
 */
public class ComandaHistorialAdapter extends RecyclerView.Adapter<ComandaHistorialAdapter.ViewHolder> {

    private List<Comanda> comandas;
    private OnCancelarClickListener listener;

    public interface OnCancelarClickListener {
        void onCancelarClick(Comanda comanda);
    }

    public ComandaHistorialAdapter(List<Comanda> comandas, OnCancelarClickListener listener) {
        this.comandas = comandas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comanda_historial, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comanda comanda = comandas.get(position);

        // Mostramos un ID corto para mejorar la legibilidad en la tablet
        holder.txtId.setText("Pedido: " + comanda.getIdComanda().substring(Math.max(0, comanda.getIdComanda().length() - 5)));
        holder.txtFecha.setText("Fecha: " + comanda.getFechaHoraEnvio());
        holder.txtTotal.setText(String.format("%.2f€", comanda.getTotal()));
        
        String estado = comanda.getEstado();
        holder.txtEstado.setText(estado);

        if ("SOLICITADO".equals(estado)) {
            holder.txtEstado.setTextColor(Color.parseColor("#2196F3")); // Azul
            holder.btnCancelar.setVisibility(View.VISIBLE);
            holder.btnCancelar.setEnabled(true);
        } else if ("EN PREPARACIÓN".equals(estado) || "PREPARANDO".equals(estado)) {
            holder.txtEstado.setTextColor(Color.parseColor("#FF9800")); // Naranja
            holder.btnCancelar.setVisibility(View.GONE); // Regla: No se cancela si ya se está cocinando
        } else if ("LISTO".equals(estado)) {
            holder.txtEstado.setTextColor(Color.parseColor("#4CAF50")); // Verde
            holder.btnCancelar.setVisibility(View.GONE);
        }

        holder.btnCancelar.setOnClickListener(v -> {
            if (listener != null) listener.onCancelarClick(comanda);
        });
    }

    @Override
    public int getItemCount() {
        return comandas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtId, txtFecha, txtEstado, txtTotal;
        Button btnCancelar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtId = itemView.findViewById(R.id.txtIdComanda);
            txtFecha = itemView.findViewById(R.id.txtFechaComanda);
            txtEstado = itemView.findViewById(R.id.txtEstadoComanda);
            txtTotal = itemView.findViewById(R.id.txtTotalComanda);
            btnCancelar = itemView.findViewById(R.id.btnCancelarPedido);
        }
    }
}
