package com.dam.t07p03.vista.fragmentos;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dam.t07p03.R;
import com.dam.t07p03.modelo.Departamento;
import com.dam.t07p03.modelo.Incidencia;
import com.dam.t07p03.vista.adaptadores.AdaptadorIncs;
import com.dam.t07p03.vistamodelo.IncsViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class BusIncsFragment extends Fragment {
    private RecyclerView rvIncs;
    private AdaptadorIncs mAdaptadorIncs;
    private Button btEliminar, btEditar, btCrear;
    private Departamento mLogin;

    public static final String TAG = "BusIncsFragment";
    private BusIncsFragInterface mListener;

    public interface BusIncsFragInterface {
        void onCrearBusIncsFrag();

        void onEditarBusIncsFrag(Incidencia inci);

        void onEliminarBusIncsFrag(Incidencia inci);
    }

    public BusIncsFragment() {
        // Required empty public constructor
    }

    public static BusIncsFragment newInstance(Bundle arguments) {
        BusIncsFragment frag = new BusIncsFragment();
        if (arguments != null) {
            frag.setArguments(arguments);
        }
        return frag;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BusIncsFragInterface) {
            mListener = (BusIncsFragInterface) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement BusIncsFragInterface");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ;
        }
        // Inits Incs Observer
        IncsViewModel incsViewModel = ViewModelProviders.of(requireActivity()).get(IncsViewModel.class);
        mLogin = incsViewModel.getmLogin();
        if (mLogin != null) {
            incsViewModel.getIncs().observe(this, new Observer<List<Incidencia>>() {
                @Override
                public void onChanged(List<Incidencia> incs) {
                    mAdaptadorIncs.setDatos(incs);
                    mAdaptadorIncs.notifyDataSetChanged();
                    if (mAdaptadorIncs.getItemPos() != -1 &&
                            mAdaptadorIncs.getItemPos() < mAdaptadorIncs.getItemCount())
                        rvIncs.scrollToPosition(mAdaptadorIncs.getItemPos());
                    else if (mAdaptadorIncs.getItemCount() > 0)
                        rvIncs.scrollToPosition(mAdaptadorIncs.getItemCount() - 1);
                    mAdaptadorIncs.setItemPos(-1);
                    btEliminar.setEnabled(false);
                    btEditar.setEnabled(false);
                    btCrear.setEnabled(true);
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bus_incs, container, false);

        // FindViewByIds
        rvIncs = v.findViewById(R.id.rvIncs);
        btEliminar = v.findViewById(R.id.btEliminar);
        btEditar = v.findViewById(R.id.btEditar);
        btCrear = v.findViewById(R.id.btCrear);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Inits
        btEliminar.setEnabled(false);
        btEditar.setEnabled(false);
        btCrear.setEnabled(true);

        // Init RecyclerView Dptos
        rvIncs.setHasFixedSize(true);
        rvIncs.setLayoutManager(new LinearLayoutManager(requireActivity()));
        rvIncs.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
        if (mAdaptadorIncs == null) {
            mAdaptadorIncs = new AdaptadorIncs();
        } else if (mAdaptadorIncs.getItemPos() != -1) {
            btEliminar.setEnabled(true);
            btEditar.setEnabled(true);
            btCrear.setEnabled(false);
        }
        rvIncs.setAdapter(mAdaptadorIncs);

        // Listeners
        btCrear.setOnClickListener(btCrear_OnClickListener);
        btEditar.setOnClickListener(btEditar_OnClickListener);
        btEliminar.setOnClickListener(btEliminar_OnClickListener);
        mAdaptadorIncs.setOnClickListener(mAdaptadorIncs_OnClickListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private View.OnClickListener mAdaptadorIncs_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = mAdaptadorIncs.getItemPos();
            if (pos != -1) {
                btEliminar.setEnabled(true);
                btEditar.setEnabled(true);
                btCrear.setEnabled(false);
            } else {
                btEliminar.setEnabled(false);
                btEditar.setEnabled(false);
                btCrear.setEnabled(true);
            }
        }
    };

    private View.OnClickListener btCrear_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = mAdaptadorIncs.getItemPos();
            if (pos == -1) {
                if (mListener != null)
                    mListener.onCrearBusIncsFrag();
            }
        }
    };

    private View.OnClickListener btEditar_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = mAdaptadorIncs.getItemPos();
            if (pos >= 0) {
                if (mListener != null)
                    mListener.onEditarBusIncsFrag(mAdaptadorIncs.getItem(pos));
            }
        }
    };

    private View.OnClickListener btEliminar_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = mAdaptadorIncs.getItemPos();
            if (pos >= 0) {
                if (mListener != null)
                    mListener.onEliminarBusIncsFrag(mAdaptadorIncs.getItem(pos));
            }
        }
    };

}
