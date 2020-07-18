package com.dam.t07p03.modelo;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface IncsDao {

    @Query("SELECT * FROM Incidencias ORDER BY fecha")
    LiveData<List<Incidencia>> getAll();

    @Query("SELECT * FROM Incidencias ORDER BY fecha")
    List<Incidencia> getAllNoLive();

    @Query("SELECT * FROM Incidencias WHERE idDpto LIKE :idDpto ORDER BY fecha")
    LiveData<List<Incidencia>> getIncsFiltro(int idDpto);

    @Query("SELECT * FROM Incidencias WHERE idDpto = :idDpto AND id = :id AND fecha = :fecha")
    Incidencia existe(int idDpto, String id, String fecha);

    @Insert
    void insert(Incidencia inc);

    @Update
    void update(Incidencia inc);

    @Delete
    void delete(Incidencia inc);

}
