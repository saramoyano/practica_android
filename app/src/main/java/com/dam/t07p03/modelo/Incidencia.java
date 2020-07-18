package com.dam.t07p03.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.FractionRes;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = "Incidencias",
        primaryKeys = {"idDpto", "id", "fecha"},
        foreignKeys = @ForeignKey(entity = Departamento.class, parentColumns = "id", childColumns = "idDpto", onDelete = CASCADE)
)
@TypeConverters({Incidencia.Converters.class})

public class Incidencia implements Parcelable {
    @NonNull
    private int idDpto; //PK
    @NonNull
    private String id; //PK
    @NonNull
    private String fecha; //PK
    private String descripcion; //no null
    private TIPO tipo; //no null
    private boolean estado; //no null
    private String resolucion;

    public enum TIPO {RMI, RMA}

    public Incidencia() {
        idDpto = 0;
        id = "";
        fecha = "";
        descripcion = "";
        tipo = null;
        estado = false;
        resolucion = "";

    }

    protected Incidencia(Parcel in) {
        idDpto = in.readInt();
        id = in.readString();
        fecha = in.readString();
        descripcion = in.readString();
        estado = in.readByte() != 0;
        resolucion = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idDpto);
        dest.writeString(id);
        dest.writeString(fecha);
        dest.writeString(descripcion);
        dest.writeByte((byte) (estado ? 1 : 0));
        dest.writeString(resolucion);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Incidencia> CREATOR = new Creator<Incidencia>() {
        @Override
        public Incidencia createFromParcel(Parcel in) {
            return new Incidencia(in);
        }

        @Override
        public Incidencia[] newArray(int size) {
            return new Incidencia[size];
        }
    };

    public int getIdDpto() {
        return idDpto;
    }

    public void setIdDpto(int idDpto) {
        this.idDpto = idDpto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TIPO getTipo() {
        return tipo;
    }

    public void setTipoInt(int tipo) {
        if (tipo == 0) {
            this.tipo = TIPO.RMI;
        }
        if (tipo == 1) {
            this.tipo = TIPO.RMA;
        } else {
            this.tipo = TIPO.RMI;
        }
    }

    public void setTipo(TIPO tipo) {

        this.tipo = tipo;
    }


    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getResolucion() {
        return resolucion;
    }

    public void setResolucion(String resolucion) {
        this.resolucion = resolucion;
    }


    static class Converters {
        @TypeConverter
        public TIPO fromInt(int x) {
            return TIPO.values()[x];
        }

        @TypeConverter
        public int tipoToInt(TIPO tipo) {
            return tipo.ordinal();
        }
    }

}
