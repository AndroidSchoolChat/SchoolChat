package com.schoolchat.schoolchat.Firebase;

public class conexion {
    //Pasamos la url de nuestra aplicacion en firebase
    public static final String FIREBASE_SCHOOLCHAT="https://schoolchatandroid.firebaseio.com";
    //rama para alumnos
    public static final String CHILD_USERS="Alumnos";
    public static final String CHILD_PROFE="Profesores";
    public static final String CHILD_GROUPS="Grupos";
    //rama para el chat
    public static final String CHILD_CHAT="Chat";
    public static final String CHILD_CONNECT="conexion";
    public static final String INFO_USER="userData";

    //estos seran los campos que se guardaran con cada usuario en la base de datos
    //deben empezar en minusculas y en el molde han de tener el mismo nombre y sin espacios en blanco
    public static final String KEY_EMAIL="email";
    public static final String NOMBRE="nombre";
    public static final String USER_EMAIL="email";
    public static final String ESTADO_ONLINE="Conectado";
    public static final String ESTADO_OFFLINE="Desconectado";
    public static final String FECHA="creado";
    public static final String CURSO="curso";

}
