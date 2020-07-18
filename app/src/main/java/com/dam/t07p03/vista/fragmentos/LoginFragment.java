package com.dam.t07p03.vista.fragmentos;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.dam.t07p03.R;
import com.dam.t07p03.modelo.Departamento;
import com.dam.t07p03.vistamodelo.DptosViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class LoginFragment extends Fragment {

    private Spinner spDptos;
    private EditText etClave;
    private Button btCancelar, btAceptar;

    private List<Departamento> mDptos;
    private ArrayAdapter<Departamento> mAdaptadorDtpos;

    public static final String TAG = "LoginFragment";
    private LoginFragInterface mListener;

    public interface LoginFragInterface {
        void onCancelarLoginFrag();

        void onAceptarLoginFrag(Departamento dpto);
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(Bundle arguments) {
        LoginFragment frag = new LoginFragment();
        if (arguments != null) {
            frag.setArguments(arguments);
        }
        return frag;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof LoginFragInterface) {
            mListener = (LoginFragInterface) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement LoginFragInterface");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ;
        }
        // Inits Dptos sin Observer
        DptosViewModel dptosVM = ViewModelProviders.of(requireActivity()).get(DptosViewModel.class);
        mDptos = dptosVM.getDptosNoLive();
        mAdaptadorDtpos = new ArrayAdapter<>(requireActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                mDptos);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        // FindViewByIds
        spDptos = v.findViewById(R.id.spLoginDptos);
        etClave = v.findViewById(R.id.etLoginClave);
        btCancelar = v.findViewById(R.id.btCancelar);
        btAceptar = v.findViewById(R.id.btAceptar);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Inits
        spDptos.setAdapter(mAdaptadorDtpos);

        // Listeners
        btCancelar.setOnClickListener(btCancelar_OnClickListener);
        btAceptar.setOnClickListener(btAceptar_OnClickListener);
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

            if (mListener != null)
                mListener.onCancelarLoginFrag();
        }
    };

    private View.OnClickListener btAceptar_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            if (mListener != null) {
                Departamento dpto = mDptos.get(spDptos.getSelectedItemPosition());
                if (dpto.getClave().equals(etClave.getText().toString())) {
                    mListener.onAceptarLoginFrag(dpto);
                } else {
                    Snackbar.make(requireActivity().findViewById(android.R.id.content), R.string.msg_LoginKO, Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    };

}
