package com.restaurante.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.restaurante.app.R;
import com.restaurante.app.model.LineaComanda;

import java.util.List;

/**
 * ADAPTER PARA LÍNEAS DE COMANDA (CARRITO)
 * Muestra los productos seleccionados antes de enviar el pedido definitivo.
 * - Visualiza la composición de una Comanda en formación.
 * - Muestra detalles específicos como la cantidad solicitada y comentarios de personalización del cliente, 
 *   asegurando que la comunicación entre el cliente y la cocina sea precisa.
 */
public class LineaComandaAdapter extends RecyclerView.Adapter<LineaComandaAdapter.LineaViewHolder> {
    private List<LineaComanda> lineas;

    public LineaComandaAdapter(List<LineaComanda> lineas) {
        this.lineas = lineas;
    }

    @NonNull
    @Override
    public LineaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Reutilizamos el layout de item_producto para mantener la consistencia visual en la app
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new LineaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LineaViewHolder holder, int position) {
        LineaComanda linea = lineas.get(position);

        // Visualización clara de la cantidad y nombre del producto
        String nombreConCantidad = linea.getCantidad() + "x " + linea.getProducto().getNombre();
        holder.nombre.setText(nombreConCantidad);

        // Lógica de cálculo de subtotal por línea (Cantidad * Precio unitario capturado)
        double subtotal = linea.getPrecioVenta() * linea.getCantidad();
        if (linea.getComentario() != null && !linea.getComentario().isEmpty()) {
            holder.precio.setText(String.format("%.2f€ - Nota: %s", subtotal, linea.getComentario()));
        } else {
            holder.precio.setText(String.format("%.2f€", subtotal));
        }
    }

    @Override
    public int getItemCount() { return lineas.size(); }

    static class LineaViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, precio;

        public LineaViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.txtNombrePlato);
            precio = itemView.findViewById(R.id.txtPrecioPlato);
        }
    }
}
