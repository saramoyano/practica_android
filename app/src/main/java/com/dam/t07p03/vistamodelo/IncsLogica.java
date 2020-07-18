package com.dam.t07p03.vistamodelo;

import android.content.Context;
import android.os.AsyncTask;

import com.dam.t07p03.modelo.AppDatabase;
import com.dam.t07p03.modelo.Departamento;
import com.dam.t07p03.modelo.DptoDao;
import com.dam.t07p03.modelo.Incidencia;
import com.dam.t07p03.modelo.IncsDao;

import java.util.List;

import androidx.lifecycle.LiveData;

public class IncsLogica {
    /* Métodos Lógica Dptos ***********************************************************************/

    private static class AsyncTask_opIncidencia extends AsyncTask<Incidencia, Void, Boolean> {
        private IncsDao incsDao;
        private String op;

        private AsyncTask_opIncidencia(IncsDao incsDao, String op) {
            this.incsDao = incsDao;
            this.op = op;
        }

        @Override
        protected Boolean doInBackground(Incidencia... incs) {
            try {
                switch (op) {
                    case "existe":
                        Incidencia inc = incsDao.existe(incs[0].getIdDpto(), incs[0].getId(), incs[0].getFecha());
                        return (inc != null);
                    case "alta":
                        incsDao.insert(incs[0]);
                        break;
                    case "editar":
                        incsDao.update(incs[0]);
                        break;
                    case "baja":
                        incsDao.delete(incs[0]);
                        break;
                }
            } catch (Exception e) {
                return false;
            }
            return true;
        }

    }

    public static boolean existeIncidencia(Context context, Incidencia incs) {
        try {
            IncsDao incsDao = AppDatabase.getAppDatabase(context).getIncsDao();
            new AsyncTask_opIncidencia(incsDao, "existe").execute(incs);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean altaIncidencia(Context context, Incidencia incs) {
        try {
            IncsDao incsDao = AppDatabase.getAppDatabase(context).getIncsDao();
            new AsyncTask_opIncidencia(incsDao, "alta").execute(incs);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean editarIncidencia(Context context, Incidencia incs) {
        try {
            IncsDao incsDao = AppDatabase.getAppDatabase(context).getIncsDao();
            new AsyncTask_opIncidencia(incsDao, "editar").execute(incs);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean bajaIncidencia(Context context, Incidencia incs) {
        try {
            IncsDao incsDao = AppDatabase.getAppDatabase(context).getIncsDao();
            new AsyncTask_opIncidencia(incsDao, "baja").execute(incs);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static class AsyncTask_opIncidencias extends AsyncTask<String, Void, List<Incidencia>> {
        private IncsDao incsDao;
        private String op;

        private AsyncTask_opIncidencias(IncsDao incsDao, String op) {
            this.incsDao = incsDao;
            this.op = op;
        }

        @Override
        protected List<Incidencia> doInBackground(String... params) {
            try {
                switch (op) {
                    case "recuperarIncs":
                        return incsDao.getAllNoLive();
                }
            } catch (Exception e) {
                return null;
            }
            return null;
        }
    }

    public static List<Incidencia> recuperarIncidenciasNoLive(Context context) {
        try {
            IncsDao incsDao = AppDatabase.getAppDatabase(context).getIncsDao();
            return new  AsyncTask_opIncidencias(incsDao, "recuperarIncs").execute().get();
        } catch (Exception e) {
            return null;
        }
    }

    public static LiveData<List<Incidencia>> recuperarIncidencias(Context context) {
        IncsDao incsDao = AppDatabase.getAppDatabase(context).getIncsDao();
        return incsDao.getAll();
    }

    public static LiveData<List<Incidencia>> recuperarIncidenciasFiltro(Context context, int mLogin) {
        IncsDao incsDao = AppDatabase.getAppDatabase(context).getIncsDao();
        return incsDao.getIncsFiltro(mLogin);
    }

}
