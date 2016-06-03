package com.schoolchat.schoolchat.Firebase;

public class conexion {
    //Pasamos la url de nuestra aplicacion en firebase
    public static final String FIREBASE_SCHOOLCHAT="https://schoolchatandroid.firebaseio.com";

    //rama para alumnos
    public static final String RAMA_ALUMNOS ="Alumnos";
    public static final String RAMA_PROFESORES ="Profesores";
    public static final String RAMA_GRUPOS ="Grupos";
    //rama para el chat
    public static final String RAMA_CHAT="Chat";
    public static final String RAMA_CONEXION ="conexion";
    public static final String INFO_USUARIO ="userData";

    //estos seran los campos que se guardaran con cada usuario en la base de datos
    //deben empezar en minusculas y en el molde han de tener el mismo nombre y sin espacios en blanco
    public static final String KEY_EMAIL="email";

    public static final String INFO_NOMBRE ="nombre";
    public static final String INFO_EMAIL ="email";
    public static final String ESTADO_ONLINE ="Conectado";
    public static final String ESTADO_OFFLINE ="Desconectado";
    public static final String INFO_FECHA ="creado";
    public static final String INFO_CURSO ="curso";

}
