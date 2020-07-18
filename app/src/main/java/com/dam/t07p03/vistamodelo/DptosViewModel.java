package com.dam.t07p03.vistamodelo;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dam.t07p03.modelo.Departamento;

import java.util.List;

public class DptosViewModel extends AndroidViewModel {

    /* ViewModel Dptos ****************************************************************************/

    private LiveData<List<Departamento>> mDptos;
    private Context mContext;
    private Departamento mLogin;
    private Departamento mDptoAEliminar;

    public DptosViewModel(@NonNull Application application) {
        super(application);
        mContext = application;
        mLogin = null;
        mDptoAEliminar = null;
    }

    /* Métodos Mantenimiento Departamentos ********************************************************/

    public LiveData<List<Departamento>> getDptos() {
        if (mDptos == null)
            mDptos = DptoLogica.recuperarDepartamentos(mContext);
        return mDptos;
    }

    public List<Departamento> getDptosNoLive() {
        return DptoLogica.recuperarDepartamentosNoLive(mContext);
    }

    public boolean altaDepartamento(Departamento dpto) {
        return DptoLogica.altaDepartamento(mContext, dpto);
    }

    public boolean editarDepartamento(Departamento dpto) {
        return DptoLogica.editarDepartamento(mContext, dpto);
    }

    public boolean bajaDepartamento(Departamento dpto) {
        return DptoLogica.bajaDepartamento(mContext, dpto);
    }

    /* Getters & Setters Objetos Persistentes *****************************************************/

    public Departamento getLogin() {
        return mLogin;
    }

    public void setLogin(Departamento login) {
        mLogin = login;
    }

    public Departamento getDptoAEliminar() {
        return mDptoAEliminar;
    }

    public void setDptoAEliminar(Departamento dptoAEliminar) {
        mDptoAEliminar = dptoAEliminar;
    }

}
