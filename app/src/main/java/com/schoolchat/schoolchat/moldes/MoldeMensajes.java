package com.schoolchat.schoolchat.moldes;


public class MoldeMensajes {
    private String mensaje;
    private String receptor;
    private String emisor;
    private int ReceptorOEmisorEstado;
    //metodos set
    public void setMensaje(String mensaje){
        this.mensaje=mensaje;
    }
    public void setReceptorOEmisor(int ReceptorOEmisor){
        this.ReceptorOEmisorEstado=ReceptorOEmisor;
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
    public int getReceptorOEmisorEstado(){
        return ReceptorOEmisorEstado;
    }
}
