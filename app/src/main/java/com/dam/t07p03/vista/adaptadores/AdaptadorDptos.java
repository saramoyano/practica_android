package com.dam.t07p03.vista.adaptadores;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.t07p03.R;
import com.dam.t07p03.modelo.Departamento;

import java.util.List;

public class AdaptadorDptos extends RecyclerView.Adapter<AdaptadorDptos.DptoVH> {

    private List<Departamento> mDatos;
    private int mItemPos;
    private View.OnClickListener mListener;

    public AdaptadorDptos() {
        mDatos = null;
        mItemPos = -1;
        mListener = null;
    }

    public void setDatos(List<Departamento> datos) {
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

    public Departamento getItem(int pos) {
        return mDatos.get(pos);
    }

    @NonNull
    @Override
    public AdaptadorDptos.DptoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_rv_dptos, parent, false);
        return new DptoVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorDptos.DptoVH holder, int position) {
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

    public class DptoVH extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView tvDptoId;
        private TextView tvDptoNombre;
        private View mItemView;

        public DptoVH(@NonNull View itemView) {
            super(itemView);
            tvDptoId = itemView.findViewById(R.id.tvDptoId);
            tvDptoNombre = itemView.findViewById(R.id.tvDptoNombre);
            itemView.setOnClickListener(this);
            mItemView = itemView;
        }

        private void setItem(Departamento dpto) {
            tvDptoId.setText(String.format(mItemView.getContext().getResources().getString(R.string.msg_Dpto_Id), dpto.getId()));
            tvDptoNombre.setText(String.format(mItemView.getContext().getResources().getString(R.string.msg_Dpto_Nombre), dpto.getNombre()));
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
