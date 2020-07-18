package com.dam.t07p03.vistamodelo;

import android.app.Application;
import android.content.Context;

import com.dam.t07p03.modelo.Departamento;
import com.dam.t07p03.modelo.Incidencia;
import com.dam.t07p03.modelo.Departamento;
import com.dam.t07p03.modelo.Incidencia;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class IncsViewModel extends AndroidViewModel {

    private LiveData<List<Incidencia>> mIncidencias;
    private Context mContext;
    private Departamento mLogin;
    private Incidencia mIncAEliminar;


    public IncsViewModel(@NonNull Application application) {
        super(application);
        mIncidencias = new MutableLiveData<>();
        mContext = application;
        mLogin = null;
        mIncAEliminar = null;
    }

    public LiveData<List<Incidencia>> getIncs() {

        if (mLogin.getId() == 0) {
            mIncidencias = IncsLogica.recuperarIncidencias(mContext);
        } else {
            mIncidencias = IncsLogica.recuperarIncidenciasFiltro(mContext, mLogin.getId());
        }

        return mIncidencias;
    }

    public List<Incidencia> getIncsNoLive() {

        return IncsLogica.recuperarIncidenciasNoLive(mContext);
    }

    public boolean altaIncidencia(Incidencia inci) {
        return IncsLogica.altaIncidencia(mContext, inci);
    }

    public boolean editarIncidencia(Incidencia inci) {
        return IncsLogica.editarIncidencia(mContext, inci);
    }

    public boolean bajaIncidencia(Incidencia inci) {
        return IncsLogica.bajaIncidencia(mContext, inci);
    }

    public Departamento getmLogin() {
        return mLogin;
    }

    public void setmLogin(Departamento mLogin) {
        this.mLogin = mLogin;
    }

    public Incidencia getmIncAEliminar() {
        return mIncAEliminar;
    }

    public void setmIncAEliminar(Incidencia mIncAEliminar) {
        this.mIncAEliminar = mIncAEliminar;
    }
}
