package com.dam.t07p03.vistamodelo;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.dam.t07p03.modelo.AppDatabase;
import com.dam.t07p03.modelo.Departamento;
import com.dam.t07p03.modelo.DptoDao;

import java.util.List;

public class DptoLogica {

    /* Métodos Lógica Dptos ***********************************************************************/

    private static class AsyncTask_opDepartamento extends AsyncTask<Departamento, Void, Boolean> {
        private DptoDao dptoDao;
        private String op;

        private AsyncTask_opDepartamento(DptoDao dptoDao, String op) {
            this.dptoDao = dptoDao;
            this.op = op;
        }

        @Override
        protected Boolean doInBackground(Departamento... dptos) {
            try {
                switch (op) {
                    case "existe":
                        Departamento dpto = dptoDao.existe(dptos[0].getId());
                        return (dpto != null);
                    case "alta":
                        dptoDao.insert(dptos[0]);
                        break;
                    case "editar":
                        dptoDao.update(dptos[0]);
                        break;
                    case "baja":
                        dptoDao.delete(dptos[0]);
                        break;
                }
            } catch (Exception e) {
                return false;
            }
            return true;
        }

    }

    public static boolean existeDepartamento(Context context, Departamento dpto) {
        try {
            DptoDao dptoDao = AppDatabase.getAppDatabase(context).getDptoDao();
            new AsyncTask_opDepartamento(dptoDao, "existe").execute(dpto);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean altaDepartamento(Context context, Departamento dpto) {
        try {
            DptoDao dptoDao = AppDatabase.getAppDatabase(context).getDptoDao();
            new AsyncTask_opDepartamento(dptoDao, "alta").execute(dpto);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean editarDepartamento(Context context, Departamento dpto) {
        try {
            DptoDao dptoDao = AppDatabase.getAppDatabase(context).getDptoDao();
            new AsyncTask_opDepartamento(dptoDao, "editar").execute(dpto);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean bajaDepartamento(Context context, Departamento dpto) {
        try {
            DptoDao dptoDao = AppDatabase.getAppDatabase(context).getDptoDao();
            new AsyncTask_opDepartamento(dptoDao, "baja").execute(dpto);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static class AsyncTask_opDepartamentos extends AsyncTask<String, Void, List<Departamento>> {
        private DptoDao dptoDao;
        private String op;

        private AsyncTask_opDepartamentos(DptoDao dptoDao, String op) {
            this.dptoDao = dptoDao;
            this.op = op;
        }

        @Override
        protected List<Departamento> doInBackground(String... params) {
            try {
                switch (op) {
                    case "recuperarDptos":
                        return dptoDao.getAllNoLive();
                }
            } catch (Exception e) {
                return null;
            }
            return null;
        }
    }

    public static List<Departamento> recuperarDepartamentosNoLive(Context context) {
        try {
            DptoDao dptoDao = AppDatabase.getAppDatabase(context).getDptoDao();
            return new AsyncTask_opDepartamentos(dptoDao, "recuperarDptos").execute().get();
        } catch (Exception e) {
            return null;
        }
    }

    public static LiveData<List<Departamento>> recuperarDepartamentos(Context context) {
        DptoDao dptoDao = AppDatabase.getAppDatabase(context).getDptoDao();
        return dptoDao.getAll();
    }

}
