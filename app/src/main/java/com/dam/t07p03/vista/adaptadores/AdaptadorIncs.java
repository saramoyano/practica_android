package com.dam.t07p03.vista.adaptadores;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dam.t07p03.R;
import com.dam.t07p03.modelo.Incidencia;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdaptadorIncs extends RecyclerView.Adapter<AdaptadorIncs.IncVH>{
    private List<Incidencia> mDatos;
    private int mItemPos;
    private View.OnClickListener mListener;

    public AdaptadorIncs() {
        mDatos = null;
        mItemPos = -1;
        mListener = null;
    }

    public void setDatos(List<Incidencia> datos) {
        mDatos = datos;
    }

    public int getItemPos() {
        return mItemPos;
    }

    public void setItemPos(int itemPos) {
        mItemPos = itemPos;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        mListener = listener;
    }

    public Incidencia getItem(int pos) {
        return mDatos.get(pos);
    }

    @NonNull
    @Override
    public AdaptadorIncs.IncVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_rv_incs, parent, false);
        return new IncVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorIncs.IncVH holder, int position) {
        if (mDatos != null) {
            holder.setItem(mDatos.get(position));
            holder.itemView.setBackgroundColor((mItemPos == position)
                    ? holder.itemView.getContext().getResources().getColor(R.color.colorPrimary)
                    : Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        if (mDatos != null)
            return mDatos.size();
        else
            return 0;
    }

    public class IncVH extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView tvDptoIdFecha;
        private TextView tvTipoEstado;
        private TextView tvIncDescripcion;
        private View mItemView;

        public IncVH(@NonNull View itemView) {
            super(itemView);
            tvDptoIdFecha = itemView.findViewById(R.id.tvIncDptoIdFecha);
            tvTipoEstado = itemView.findViewById(R.id.tvIncTipoEstado);
            tvIncDescripcion = itemView.findViewById(R.id.tvIncDescripcion);
            itemView.setOnClickListener(this);
            mItemView = itemView;
        }

        private void setItem(Incidencia inci) {
            tvDptoIdFecha.setText(String.format(mItemView.getContext().getResources().getString(R.string.msg_Inc_DptoIdFecha), inci.getIdDpto(), inci.getId(), inci.getFecha()));
            tvTipoEstado.setText(String.format(mItemView.getContext().getResources().getString(R.string.msg_Inc_TipoEstado), inci.getTipo(),
                  ((inci.isEstado() == true)? mItemView.getContext().getResources().getString(R.string.msg_Inc_Tipo_True) : mItemView.getContext().getResources().getString(R.string.msg_Inc_Tipo_False))));
            tvIncDescripcion.setText(String.format(mItemView.getContext().getResources().getString(R.string.msg_Inc_Descripcion), inci.getDescripcion()));
        }

        @Override
        public void onClick(View v) {
            int pos = getLayoutPosition();
            notifyItemChanged(mItemPos);
            mItemPos = (mItemPos == pos) ? -1 : pos;
            notifyItemChanged(mItemPos);
            if (mListener != null)
                mListener.onClick(v);
        }
    }

}
