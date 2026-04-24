package com.restaurante.app.adapter;

import android.app.AlertDialog;
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
import com.restaurante.app.model.SesionMesa;

import java.util.List;

/**
 * ADAPTER PARA LA VISTA DE CAMARERO
 * Gestiona la visualización de las mesas que han solicitado atención y el cierre de las mismas.
 * - Atender Avisos: Resetea los flags temporales de comunicación (pedidoCamarero/pedidoCuenta).
 * - Finalizar Mesa: Implementa el cierre definitivo de la entidad SesionMesa, marcando el estado 
 *   como 'FINALIZADA' y registrando la fecha de fin, lo que libera la mesa física para el siguiente uso.
 */
public class CamareroAdapter extends RecyclerView.Adapter<CamareroAdapter.CamareroViewHolder> {

    private List<SesionMesa> listaSesiones;
    private DatabaseReference mDatabase;

    public CamareroAdapter(List<SesionMesa> listaSesiones, DatabaseReference mDatabase) {
        this.listaSesiones = listaSesiones;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public CamareroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_aviso_camarero, parent, false);
        return new CamareroViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CamareroViewHolder holder, int position) {
        SesionMesa sesion = listaSesiones.get(position);

        holder.txtMesa.setText("MESA: " + sesion.getIdMesa());

        // LÓGICA DE AVISOS (TFG: Comunicación síncrona)
        StringBuilder aviso = new StringBuilder();
        if (sesion.isPedidoCamarero()) aviso.append("LLAMADA AL CAMARERO ");
        if (sesion.isPedidoCuenta()) aviso.append(aviso.length() > 0 ? "+ CUENTA" : "PIDE LA CUENTA");

        holder.txtAviso.setText(aviso.toString());

        // ACCIÓN 1: Atender aviso (limpia los botones de llamada en la tablet)
        holder.btnAtender.setOnClickListener(v -> {
            mDatabase.child(sesion.getIdSesion()).child("pedidoCamarero").setValue(false);
            mDatabase.child(sesion.getIdSesion()).child("pedidoCuenta").setValue(false)
                    .addOnSuccessListener(aVoid -> Toast.makeText(v.getContext(), "Aviso atendido", Toast.LENGTH_SHORT).show());
        });

        // ACCIÓN 2: Finalizar Mesa (Cierre de la entidad SesionMesa en el MER)
        holder.btnFinalizar.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Finalizar Sesión")
                    .setMessage("¿Confirmas que la mesa ha pagado y deseas cerrar la sesión?")
                    .setPositiveButton("Sí, Cerrar", (dialog, which) -> {
                        // Actualizamos el estado y la fecha de fin en Firebase
                        mDatabase.child(sesion.getIdSesion()).child("estadoSesion").setValue("FINALIZADA");
                        mDatabase.child(sesion.getIdSesion()).child("fechaFin").setValue(System.currentTimeMillis())
                                .addOnSuccessListener(aVoid -> Toast.makeText(v.getContext(), "Sesión finalizada correctamente", Toast.LENGTH_SHORT).show());
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return listaSesiones.size();
    }

    static class CamareroViewHolder extends RecyclerView.ViewHolder {
        TextView txtMesa, txtAviso;
        Button btnAtender, btnFinalizar;

        public CamareroViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMesa = itemView.findViewById(R.id.txtMesaAviso);
            txtAviso = itemView.findViewById(R.id.txtTipoAviso);
            btnAtender = itemView.findViewById(R.id.btnAtenderAviso);
            btnFinalizar = itemView.findViewById(R.id.btnFinalizarMesa);
        }
    }
}
