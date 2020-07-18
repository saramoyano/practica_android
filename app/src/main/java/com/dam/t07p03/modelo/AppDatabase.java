package com.dam.t07p03.modelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.dam.t07p03.R;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Database(
        entities = {Departamento.class, Incidencia.class},
        version = 1,
        exportSchema = false
)
@TypeConverters({Incidencia.Converters.class})

public abstract class AppDatabase extends RoomDatabase {

    public abstract DptoDao getDptoDao();
    public abstract IncsDao getIncsDao();

    private static volatile AppDatabase db = null;  // Singleton

    public static AppDatabase getAppDatabase(Context context) {
        if (db == null) {
            synchronized (AppDatabase.class) {
                if (db == null) {
                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
                    String nombreBD = pref.getString(context.getResources().getString(R.string.SQLite_name_key), "");
                    if (!nombreBD.equals("")) {
                        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, nombreBD)
                                .addCallback(dbCallback)
                                .build();
                    }
                }
            }
        }
        return db;
    }

    public static boolean cerrarAppDatabase() {
        if (db != null && db.isOpen()) {
            db.close();
            db = null;
            return true;
        }
        return false;
    }

    private static Callback dbCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase dbsqlite) {
            super.onCreate(dbsqlite);

            String sql = "insert into Departamentos values (0,'admin','a')";
            dbsqlite.execSQL(sql);

//            Executors.newSingleThreadExecutor().execute(new Runnable() {
//                @Override
//                public void run() {
//                    Departamento dpto = new Departamento();
//                    dpto.setId(0);
//                    dpto.setNombre("admin");
//                    dpto.setClave("a");
//                    db.getDptoDao().insert(dpto);
//                }
//            });
//
//            try {
//                new IniBDAsync(db).execute().get(10, TimeUnit.SECONDS);
//            } catch (Exception e) {
//                ;
//            }
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

//    private static class IniBDAsync extends AsyncTask<Void, Void, Void> {
//
//        private DptoDao dptoDao;
//        private IncsDao incsDao;
//
//        public IniBDAsync(AppDatabase db) {
//            this.dptoDao = db.getDptoDao();
//            this.incsDao = db.getIncsDao();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            Departamento dpto = new Departamento();
//            dpto.setId(0);
//            dpto.setNombre("admin");
//            dpto.setClave("a");
//            dptoDao.insert(dpto);
//            return null;
//        }
//    }


}
