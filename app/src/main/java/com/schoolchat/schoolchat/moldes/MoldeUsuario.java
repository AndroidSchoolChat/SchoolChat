package com.schoolchat.schoolchat.moldes;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MoldeUsuario implements Parcelable{
    //informacion del receptor
    private String nombre;
    private String curso;
    private String email;
    private String creado;
    private String conexion;
    private String Uidreceptor;
    //informacion del emisor
    private String enombre;
    private String Uidemisor;
    private String eEmail;
    private String eCreado;

    public MoldeUsuario(){

    }

    private MoldeUsuario(Parcel in) {
        nombre = in.readString();
        curso =in.readString();
        email = in.readString();
        creado=in.readString();
        conexion = in.readString();
        Uidreceptor = in.readString();
        enombre = in.readString();
        Uidemisor = in.readString();
        eEmail = in.readString();
        eCreado=in.readString();
    }
    //informacion del receptor
    public String getnombre(){return nombre;}
    public String getcurso(){return curso;}
    public String getemail(){return email;}
    public String getcreado(){return creado;}
    public String getconexion(){return conexion;}
    public String getUidreceptor(){return Uidreceptor;}
    public void setUidreceptor(String Uidreceptor){this.Uidreceptor=Uidreceptor;}
    //informacion del emisor
    public void setEnombre(String enombre){this.enombre=enombre;}
    public void seteEmail(String eEmail){this.eEmail=eEmail;}
    public void seteCreado(String fecha){eCreado=fecha;}
    public void setUidemisor(String uidemisor){this.Uidemisor=uidemisor;}
    public String getEnombre(){return enombre;}
    public String geteEmail(){return eEmail;}
    public String geteCreado(){return eCreado;}
    public String getUidemisor(){return Uidemisor;}

    //parte para el chat
    public String getChatRef(){return createuniquechatref();}
    private String createuniquechatref(){
        String chatref="";
        if(fechaemisor()>fechareceptor()){
            chatref=limpiarEamil(geteEmail())+"-"+limpiarEamil(getemail());
        }else {
            chatref=limpiarEamil(getemail())+"-"+limpiarEamil(geteEmail());
        }
        return chatref;
    }
    private long fechaemisor(){return Long.parseLong(geteCreado());}
    private long fechareceptor(){return Long.parseLong(getcreado());}
    private String limpiarEamil(String mail){
        return mail.replace(".","-");
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre);
        dest.writeString(curso);
        dest.writeString(email);
        dest.writeString(creado);
        dest.writeString(conexion);
        dest.writeString(Uidreceptor);
        dest.writeString(enombre);
        dest.writeString(Uidemisor);
        dest.writeString(eEmail);
        dest.writeString(eCreado);
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
