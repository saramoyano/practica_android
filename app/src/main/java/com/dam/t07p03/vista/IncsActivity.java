package com.dam.t07p03.vista;

import android.content.Intent;
import android.os.Bundle;

import com.dam.t07p03.R;
import com.dam.t07p03.modelo.Departamento;
import com.dam.t07p03.modelo.Incidencia;
import com.dam.t07p03.vista.dialogos.DlgConfirmacion;
import com.dam.t07p03.vista.fragmentos.BusIncsFragment;
import com.dam.t07p03.vista.fragmentos.MtoIncsFragment;
import com.dam.t07p03.vistamodelo.IncsViewModel;
import com.dam.t07p03.modelo.Departamento;
import com.dam.t07p03.vista.dialogos.DlgConfirmacion;
import com.dam.t07p03.vista.fragmentos.BusIncsFragment;
import com.dam.t07p03.vista.fragmentos.MtoIncsFragment;
import com.dam.t07p03.vistamodelo.IncsViewModel;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class IncsActivity extends AppCompatActivity
        implements DlgConfirmacion.DlgConfirmacionListener, MtoIncsFragment.MtoIncsFragInterface, BusIncsFragment.BusIncsFragInterface {

    private NavController mNavC;

    private IncsViewModel mIncsVM;
    private Departamento mLogin;
//    private Incidencia mIncAEliminar;
//    private boolean mDatosModificados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incs);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // FindViewByIds

        // Inits
        mNavC = Navigation.findNavController(this, R.id.navhostfrag_incs);
        mIncsVM = ViewModelProviders.of(this).get(IncsViewModel.class);

        // Recuperamos el dpto login
        Intent i = getIntent();
        if (i != null) {
            Bundle b = i.getExtras();
            if (b != null) {
                mLogin = b.getParcelable("login");
                mIncsVM.setmLogin(mLogin);
            }
        }
        if (mLogin == null) {
            Snackbar.make(findViewById(android.R.id.content), R.string.msg_NoLogin, Snackbar.LENGTH_SHORT).show();
            finish();
            return;
        }


    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }


    @Override
    public void onDlgConfirmacionPositiveClick(DialogFragment dialog) {
        Incidencia incAEliminar = mIncsVM.getmIncAEliminar();
        if (incAEliminar != null) {
            if (mIncsVM.bajaIncidencia(incAEliminar)) {
                Snackbar.make(findViewById(android.R.id.content), R.string.msg_BajaIncidenciaOK, Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(findViewById(android.R.id.content), R.string.msg_BajaIncidenciaKO, Snackbar.LENGTH_SHORT).show();
            }
        }
        mIncsVM.setmIncAEliminar(null);
    }

    @Override
    public void onDlgConfirmacionNegativeClick(DialogFragment dialog) {

        mIncsVM.setmIncAEliminar(null);
    }

    @Override
    public void onCrearBusIncsFrag() {
        Bundle bundle = new Bundle();
        bundle.putInt("op", MtoIncsFragment.OP_CREAR);
        bundle.putInt("login", mLogin.getId());
        mNavC.navigate(R.id.action_busIncsFragment_to_mtoIncsFragment, bundle);
    }

    @Override
    public void onEditarBusIncsFrag(Incidencia inci) {
        Bundle bundle = new Bundle();
        bundle.putInt("op", MtoIncsFragment.OP_EDITAR);
        bundle.putParcelable("inc", inci);
        mNavC.navigate(R.id.action_busIncsFragment_to_mtoIncsFragment, bundle);

    }

    @Override
    public void onEliminarBusIncsFrag(Incidencia inci) {
        mIncsVM.setmIncAEliminar(inci);

        Bundle bundle = new Bundle();
        bundle.putInt("titulo", R.string.app_name);
        bundle.putInt("mensaje", R.string.msg_DlgConfirmacion_Eliminar);
        mNavC.navigate(R.id.action_global_dlgConfirmacion, bundle);
    }

    @Override
    public void onCancelarMtoIncsFrag() {

        mNavC.navigateUp();
    }

    @Override
    public void onAceptarMtoIncsFrag(int op, Incidencia inc) {
        switch (op) {
            case MtoIncsFragment.OP_CREAR:
                if (mIncsVM.altaIncidencia(inc)) {
                    Snackbar.make(findViewById(android.R.id.content), R.string.msg_AltaIncidenciaOK, Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(findViewById(android.R.id.content), R.string.msg_AltaIncidenciaKO, Snackbar.LENGTH_SHORT).show();
                }
                break;
            case MtoIncsFragment.OP_EDITAR:
                if (mIncsVM.editarIncidencia(inc)) {
                    Snackbar.make(findViewById(android.R.id.content), R.string.msg_EditarIncidenciaOK, Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(findViewById(android.R.id.content), R.string.msg_EditarIncidenciaKO, Snackbar.LENGTH_SHORT).show();
                }
                break;
            case MtoIncsFragment.OP_ELIMINAR:
                break;
        }

        // Cerramos MtoDptosFragment
        mNavC.navigateUp();
    }
}
