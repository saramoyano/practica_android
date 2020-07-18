package com.dam.t07p03.vista.fragmentos;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dam.t07p03.R;
import com.dam.t07p03.modelo.Departamento;
import com.dam.t07p03.vista.adaptadores.AdaptadorDptos;
import com.dam.t07p03.vistamodelo.DptosViewModel;

import java.util.List;

public class BusDptosFragment extends Fragment {

    private RecyclerView rvDptos;
    private AdaptadorDptos mAdaptadorDptos;
    private Button btEliminar, btEditar, btCrear;

    private Departamento mLogin;

    public static final String TAG = "BusDptosFragment";
    private BusDptosFragInterface mListener;

    public interface BusDptosFragInterface {
        void onCrearBusDptosFrag();

        void onEditarBusDptosFrag(Departamento dpto);

        void onEliminarBusDptosFrag(Departamento dpto);
    }

    public BusDptosFragment() {
        // Required empty public constructor
    }

    public static BusDptosFragment newInstance(Bundle arguments) {
        BusDptosFragment frag = new BusDptosFragment();
        if (arguments != null) {
            frag.setArguments(arguments);
        }
        return frag;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BusDptosFragInterface) {
            mListener = (BusDptosFragInterface) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement BusDptosFragInterface");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ;
        }
        // Inits
        DptosViewModel dptosVM = ViewModelProviders.of(requireActivity()).get(DptosViewModel.class);
        mLogin = dptosVM.getLogin();    // Recuperamos el login del ViewModel

        if (mLogin != null) {
            // Inits Dptos Observer
            dptosVM.getDptos().observe(this, new Observer<List<Departamento>>() {
                @Override
                public void onChanged(List<Departamento> dptos) {
                    mAdaptadorDptos.setDatos(dptos);
                    mAdaptadorDptos.notifyDataSetChanged();
                    if (mAdaptadorDptos.getItemPos() != -1 &&
                            mAdaptadorDptos.getItemPos() < mAdaptadorDptos.getItemCount())
                        rvDptos.scrollToPosition(mAdaptadorDptos.getItemPos());
                    else if (mAdaptadorDptos.getItemCount() > 0)
                        rvDptos.scrollToPosition(mAdaptadorDptos.getItemCount() - 1);
                    mAdaptadorDptos.setItemPos(-1);
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
        View v = inflater.inflate(R.layout.fragment_bus_dptos, container, false);

        // FindViewByIds
        rvDptos = v.findViewById(R.id.rvDptos);
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
        rvDptos.setHasFixedSize(true);
        rvDptos.setLayoutManager(new LinearLayoutManager(requireActivity()));
        rvDptos.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
        if (mAdaptadorDptos == null) {
            mAdaptadorDptos = new AdaptadorDptos();
        } else if (mAdaptadorDptos.getItemPos() != -1) {
            btEliminar.setEnabled(true);
            btEditar.setEnabled(true);
            btCrear.setEnabled(false);
        }
        rvDptos.setAdapter(mAdaptadorDptos);

        // Listeners
        btCrear.setOnClickListener(btCrear_OnClickListener);
        btEditar.setOnClickListener(btEditar_OnClickListener);
        btEliminar.setOnClickListener(btEliminar_OnClickListener);
        mAdaptadorDptos.setOnClickListener(mAdaptadorDptos_OnClickListener);
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

    private View.OnClickListener mAdaptadorDptos_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = mAdaptadorDptos.getItemPos();
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
            int pos = mAdaptadorDptos.getItemPos();
            if (pos == -1) {
                if (mListener != null)
                    mListener.onCrearBusDptosFrag();
            }
        }
    };

    private View.OnClickListener btEditar_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = mAdaptadorDptos.getItemPos();
            if (pos >= 0) {
                if (mListener != null)
                    mListener.onEditarBusDptosFrag(mAdaptadorDptos.getItem(pos));
            }
        }
    };

    private View.OnClickListener btEliminar_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos = mAdaptadorDptos.getItemPos();
            if (pos >= 0) {
                if (mListener != null)
                    mListener.onEliminarBusDptosFrag(mAdaptadorDptos.getItem(pos));
            }
        }
    };

}
