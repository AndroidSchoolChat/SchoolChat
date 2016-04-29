package com.schoolchat.schoolchat.moldes;

/**
 * Created by mcb on 29/04/2016.
 */
public class MoldeMensajes {
    private String mensaje;
    private String receptor;
    private String emisor;
    //metodos set
    public void setMensaje(String mensaje){
        this.mensaje=mensaje;
    }
    public void setReceptor(String receptor){
        this.receptor=receptor;
    }
    public void setEmisor(String emisor){
        this.emisor=emisor;
    }
    //metodos get
    public String getMensaje(){
        return mensaje;
    }
    public String getReceptor(){
        return receptor;
    }
    public String getEmisor(){
        return emisor;
    }
}
