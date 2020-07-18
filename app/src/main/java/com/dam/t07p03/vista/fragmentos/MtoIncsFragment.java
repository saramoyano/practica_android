package com.dam.t07p03.vista.fragmentos;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dam.t07p03.R;
import com.dam.t07p03.modelo.Incidencia;
import com.dam.t07p03.modelo.Incidencia;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MtoIncsFragment extends Fragment {
    private TextView tvCabecera;
    private EditText etIncId, etIncDptoId, etIncFecha, etIncDescripcion, etIncResolucion, etIncDptoNombre;
    private RadioGroup rgIncTipo;
    private RadioButton rbIncTipoRMI, rbIncTipoRMA;
    private CheckBox cbIncEstado;
    private Button btCancelar, btAceptar;

    private int mOp;    // Operación a realizar
    private Incidencia mInc;
    private int mLogin;

    public static final int OP_ELIMINAR = 1;
    public static final int OP_EDITAR = 2;
    public static final int OP_CREAR = 3;

    public static final String TAG = "MtoIncsFragment";
    private MtoIncsFragInterface mListener;

    public interface MtoIncsFragInterface {
        void onCancelarMtoIncsFrag();

        void onAceptarMtoIncsFrag(int op, Incidencia inc);
    }

    public MtoIncsFragment() {
        // Required empty public constructor
    }

    public static MtoIncsFragment newInstance(Bundle arguments) {
        MtoIncsFragment frag = new MtoIncsFragment();
        if (arguments != null) {
            frag.setArguments(arguments);
        }
        return frag;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MtoIncsFragInterface) {
            mListener = (MtoIncsFragInterface) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement MtoIncsFragInterface");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mOp = getArguments().getInt("op");
            mInc = getArguments().getParcelable("inc");
            mLogin = getArguments().getInt("login");
        } else {
            mOp = -1;
            mInc = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mto_incs, container, false);

        // FindViewByIds
        tvCabecera = v.findViewById(R.id.tvIncCabecera);
//
        etIncId = v.findViewById(R.id.etIncId);
        etIncDptoId = v.findViewById(R.id.etIncDptoId);
        etIncDptoNombre = v.findViewById(R.id.etIncDptoNombre);
        etIncFecha = v.findViewById(R.id.etIncFecha);
        etIncDescripcion = v.findViewById(R.id.etIncDescripcion);
        etIncResolucion = v.findViewById(R.id.etIncResolucion);
        rgIncTipo = v.findViewById(R.id.rgIncTipo);
        rbIncTipoRMI = v.findViewById(R.id.rbIncTipoRMI);
        rbIncTipoRMA = v.findViewById(R.id.rbIncTipoRMA);
        cbIncEstado = v.findViewById(R.id.cbIncEstado);
        btCancelar = v.findViewById(R.id.btCancelar);
        btAceptar = v.findViewById(R.id.btAceptar);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Inits
        if (mOp != -1) {                    // MtoIncsFrag requiere una operación válida!!
            btCancelar.setEnabled(true);
            btAceptar.setEnabled(true);

            switch (mOp) {
                case OP_CREAR:
                    tvCabecera.setText(getString(R.string.tv_Inc_Cabecera_Crear));
                    etIncDptoId.setText(String.valueOf(mLogin));
                    SimpleDateFormat format = new SimpleDateFormat("HHmmss");
                    etIncId.setText(String.format("Inc-%s", format.format(new Date())));
                    SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
                    etIncFecha.setText(formateador.format(new Date()));
                    etIncDptoId.setEnabled(false);
                    etIncDptoNombre.setEnabled(false);
                    etIncId.setEnabled(false);
                    etIncFecha.setEnabled(false);
                    etIncResolucion.setEnabled(false);
                    cbIncEstado.setEnabled(false);
                    break;
                case OP_EDITAR:
                    tvCabecera.setText(getString(R.string.tv_Inc_Cabecera_Editar));
                    etIncDptoId.setText(String.valueOf(mInc.getIdDpto()));
                    etIncId.setText(String.valueOf(mInc.getId()));
                    etIncFecha.setText(mInc.getFecha());
                    etIncDptoId.setEnabled(false);
                    etIncDptoNombre.setEnabled(false);
                    etIncId.setEnabled(false);
                    etIncFecha.setEnabled(false);
                    if (mInc.getTipo() != null) {
                        if (mInc.getTipo().equals(Incidencia.TIPO.RMI)) {
                            rbIncTipoRMI.setChecked(true);
                        } else {
                            rbIncTipoRMA.setChecked(true);
                        }
                    }
                    etIncDescripcion.setText(mInc.getDescripcion());

                    if (mInc.isEstado()) {
                        cbIncEstado.setChecked(true);
                    } else {
                        cbIncEstado.setChecked(false);
                    }
                    etIncResolucion.setText(mInc.getResolucion());
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
                mListener.onCancelarMtoIncsFrag();
            }
        }
    };

    private View.OnClickListener btAceptar_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            if (mListener != null) {
                if (!etIncDptoId.getText().toString().equals("") &&
                        !etIncId.getText().toString().equals("") &&
                        !etIncFecha.getText().toString().equals("") &&
                        !etIncDescripcion.getText().toString().equals("")
                        && (rgIncTipo.getCheckedRadioButtonId() != -1)
                ) {

                    // Creamos una nueva incidencia, independientemente de la operación a realizar!!
                    mInc = new Incidencia();
                    mInc.setIdDpto(Integer.parseInt(etIncDptoId.getText().toString()));
                    mInc.setId(etIncId.getText().toString());
                    mInc.setFecha(etIncFecha.getText().toString());
                    mInc.setDescripcion(etIncDescripcion.getText().toString());
                    mInc.setTipoInt(rbIncTipoRMI.isChecked() ? 0 : 1);
                    mInc.setEstado(cbIncEstado.isChecked());
                    mInc.setResolucion(etIncResolucion.getText().toString());
                    mListener.onAceptarMtoIncsFrag(mOp, mInc);
                } else {
                    Snackbar.make(requireActivity().findViewById(android.R.id.content), R.string.msg_FaltanDatosObligatorios, Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    };
}
