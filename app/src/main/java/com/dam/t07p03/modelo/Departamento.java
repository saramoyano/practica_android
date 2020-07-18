package com.dam.t07p03.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(
        tableName = "Departamentos",
        primaryKeys = {"id"}
)

public class Departamento implements Parcelable {

    /* Atributos **********************************************************************************/

    private int id;                 // PK
    private String nombre;
    private String clave;

    /* Constructor ********************************************************************************/

    public Departamento() {
        id = 0;
        nombre = "";
        clave = "";
    }

    /* Métodos Getters&Setters ********************************************************************/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    /* Métodos Parcelable *************************************************************************/

    protected Departamento(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        clave = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeString(clave);
    }

    public static final Creator<Departamento> CREATOR = new Creator<Departamento>() {
        @Override
        public Departamento createFromParcel(Parcel in) {
            return new Departamento(in);
        }

        @Override
        public Departamento[] newArray(int size) {
            return new Departamento[size];
        }
    };

    /* Métodos ************************************************************************************/

    @NonNull
    @Override
    public String toString() {
        return nombre;
    }

}
