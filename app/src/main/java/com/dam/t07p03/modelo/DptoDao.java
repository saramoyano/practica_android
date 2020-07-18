package com.dam.t07p03.modelo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DptoDao {

    @Query("SELECT * FROM departamentos ORDER BY id")
    LiveData<List<Departamento>> getAll();

    @Query("SELECT * FROM departamentos ORDER BY id")
    List<Departamento> getAllNoLive();

    @Query("SELECT * FROM departamentos WHERE id LIKE :id")
    List<Departamento> getDptoFiltroNoLive(String id);

    @Query("SELECT * FROM departamentos WHERE id = :id")
    Departamento existe(int id);

    @Insert
    void insert(Departamento dpto);

    @Update
    void update(Departamento dpto);

    @Delete
    void delete(Departamento dpto);

}
