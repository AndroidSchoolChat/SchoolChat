package com.schoolchat.schoolchat.moldes;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by mcb on 29/04/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MoldeUsuario implements Parcelable{
    //informacion del receptor
    private String nombre;
    private String Email;
    private String conexion;
    private String Uidreceptor;
    //informacion del emisor
    private String enombre;
    private String Uidemisor;
    private String eEmail;

    public MoldeUsuario(){

    }

    private MoldeUsuario(Parcel in) {
        nombre = in.readString();
        Email = in.readString();
        conexion = in.readString();
        Uidreceptor = in.readString();
        enombre = in.readString();
        Uidemisor = in.readString();
        eEmail = in.readString();
    }
    //informacion del receptor
    public String getNombre(){return nombre;}
    public String getEmail(){return Email;}
    public String getConexion(){return conexion;}
    public String getUidreceptor(){return Uidreceptor;}
    public void setUidreceptor(String Uidreceptor){this.Uidreceptor=Uidreceptor;}
    //informacion del emisor
    public void setEnombre(String enombre){this.enombre=enombre;}
    public void seteEmail(String eEmail){this.eEmail=eEmail;}
    public void setUidemisor(String uidemisor){this.Uidemisor=uidemisor;}
    public String getEnombre(){return enombre;}
    public String geteEmail(){return eEmail;}
    public String getUidemisor(){return Uidemisor;}

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(Email);
        dest.writeString(conexion);
        dest.writeString(Uidreceptor);
        dest.writeString(enombre);
        dest.writeString(Uidemisor);
        dest.writeString(eEmail);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MoldeUsuario> CREATOR = new Creator<MoldeUsuario>() {
        @Override
        public MoldeUsuario createFromParcel(Parcel in) {
            return new MoldeUsuario(in);
        }

        @Override
        public MoldeUsuario[] newArray(int size) {
            return new MoldeUsuario[size];
        }
    };
}
