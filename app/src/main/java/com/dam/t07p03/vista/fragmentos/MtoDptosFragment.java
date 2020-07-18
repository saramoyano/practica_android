package com.dam.t07p03.vista.fragmentos;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dam.t07p03.R;
import com.dam.t07p03.modelo.Departamento;
import com.google.android.material.snackbar.Snackbar;

public class MtoDptosFragment extends Fragment {

    private TextView tvCabecera;
    private EditText etId, etNombre, etClave;
    private Button btCancelar, btAceptar;

    private int mOp;    // Operación a realizar
    private Departamento mDpto;

    public static final int OP_ELIMINAR = 1;
    public static final int OP_EDITAR = 2;
    public static final int OP_CREAR = 3;

    public static final String TAG = "MtoDptosFragment";
    private MtoDptosFragInterface mListener;

    public interface MtoDptosFragInterface {
        void onCancelarMtoDptosFrag();

        void onAceptarMtoDptosFrag(int op, Departamento dpto);
    }

    public MtoDptosFragment() {
        // Required empty public constructor
    }

    public static MtoDptosFragment newInstance(Bundle arguments) {
        MtoDptosFragment frag = new MtoDptosFragment();
        if (arguments != null) {
            frag.setArguments(arguments);
        }
        return frag;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MtoDptosFragInterface) {
            mListener = (MtoDptosFragInterface) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement MtoDptosFragInterface");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mOp = getArguments().getInt("op");
            mDpto = getArguments().getParcelable("dpto");
        } else {
            mOp = -1;
            mDpto = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mto_dptos, container, false);

        // FindViewByIds
        tvCabecera = v.findViewById(R.id.tvDptoCabecera);
        etId = v.findViewById(R.id.etDptoId);
        etNombre = v.findViewById(R.id.etDptoNombre);
        etClave = v.findViewById(R.id.etDptoClave);
        btCancelar = v.findViewById(R.id.btCancelar);
        btAceptar = v.findViewById(R.id.btAceptar);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Inits
        if (mOp != -1) {                    // MtoDptosFragment requiere una operación válida!!
            btCancelar.setEnabled(true);
            btAceptar.setEnabled(true);

            switch (mOp) {
                case OP_CREAR:
                    tvCabecera.setText(getString(R.string.tv_Dpto_Cabecera_Crear));
                    break;
                case OP_EDITAR:
                    tvCabecera.setText(getString(R.string.tv_Dpto_Cabecera_Editar));
                    etId.setText(String.valueOf(mDpto.getId()));
                    etNombre.setText(mDpto.getNombre());
                    etClave.setText(mDpto.getClave());
                    etId.setEnabled(false);
                    break;
            }

            // Listeners
            btCancelar.setOnClickListener(btCancelar_OnClickListener);
            btAceptar.setOnClickListener(btAceptar_OnClickListener);

        } else {
            btCancelar.setEnabled(false);
            btAceptar.setEnabled(false);
        }
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

    private View.OnClickListener btCancelar_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            if (mListener != null) {
                mListener.onCancelarMtoDptosFrag();
            }
        }
    };

    private View.OnClickListener btAceptar_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            if (mListener != null) {
                if (!etId.getText().toString().equals("") &&
                        !etNombre.getText().toString().equals("") &&
                        !etClave.getText().toString().equals("")) {

                    // Creamos un nuevo departameto, independientemente de la operación a realizar!!
                    mDpto = new Departamento();
                    mDpto.setId(Integer.parseInt(etId.getText().toString()));
                    mDpto.setNombre(etNombre.getText().toString());
                    mDpto.setClave(etClave.getText().toString());

                    mListener.onAceptarMtoDptosFrag(mOp, mDpto);
                } else {
                    Snackbar.make(requireActivity().findViewById(android.R.id.content), R.string.msg_FaltanDatosObligatorios, Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    };

}
