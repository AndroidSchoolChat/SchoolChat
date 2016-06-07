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
    private String UidReceptor;
    //informacion del emisor
    private String emisorNombre;
    private String UidEmisor;
    private String emisorEmail;
    private String emisorCreado;

    public MoldeUsuario(){

    }

    private MoldeUsuario(Parcel in) {
        nombre = in.readString();
        curso =in.readString();
        email = in.readString();
        creado=in.readString();
        conexion = in.readString();
        UidReceptor = in.readString();
        emisorNombre = in.readString();
        UidEmisor = in.readString();
        emisorEmail = in.readString();
        emisorCreado =in.readString();
    }
    //informacion del receptor
    public String getnombre(){return nombre;}
    public String getcurso(){return curso;}
    public String getemail(){return email;}
    public String getcreado(){return creado;}
    public String getconexion(){return conexion;}
    public String getUidReceptor(){return UidReceptor;}
    public void setUidReceptor(String Uidreceptor){this.UidReceptor =Uidreceptor;}
    public void setConexion(String conexion){this.conexion=conexion;}
    public void setNombre(String nombre){this.nombre=nombre;}
    public void setEmail(String Email){this.email=Email;}
    public void setCreado(String creado){this.creado=creado;}



    //informacion del emisor
    public void setEmisorNombre(String emisorNombre){this.emisorNombre = emisorNombre;}
    public void setEmisorEmail(String emisorEmail){this.emisorEmail = emisorEmail;}
    public void setEmisorCreado(String fecha){
        emisorCreado =fecha;}
    public void setUidEmisor(String uidEmisor){this.UidEmisor = uidEmisor;}
    public String getEmisorNombre(){return emisorNombre;}
    public String getEmisorEmail(){return emisorEmail;}
    public String getEmisorCreado(){return emisorCreado;}
    public String getUidEmisor(){return UidEmisor;}

    //parte para el chat
    public String getChatRef(){return createuniquechatref();}
    private String createuniquechatref(){
        String chatref="";
        if(fechaemisor()>fechareceptor()){
            chatref=limpiarEamil(getEmisorEmail())+"-"+limpiarEamil(getemail());
        }else {
            chatref=limpiarEamil(getemail())+"-"+limpiarEamil(getEmisorEmail());
        }
        return chatref;
    }
    private long fechaemisor(){return Long.parseLong(getEmisorCreado());}
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
        dest.writeString(UidReceptor);
        dest.writeString(emisorNombre);
        dest.writeString(UidEmisor);
        dest.writeString(emisorEmail);
        dest.writeString(emisorCreado);
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
