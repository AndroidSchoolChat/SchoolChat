package com.schoolchat.schoolchat.Firebase;

public class conexion {
    //Pasamos la url de nuestra aplicacion en firebase
    public static final String FIREBASE_SCHOOLCHAT="https://schoolchatandroid.firebaseio.com";
    public static final String CHILD_USERS="Usuarios";
    public static final String CHILD_GROUPS="Grupos";
    public static final String CHILD_CHAT="Chat";
    public static final String CHILD_CONNECT="Conexion";
    public static final String INFO_USER="userData";

    //estos seran los campos que se guardaran con cada usuario en la base de datos
    public static final String KEY_EMAIL="email";
    public static final String NOMBRE="Nombre";
    public static final String USER_EMAIL="Email"; //CAMBIO*GABRI
    public static final String ESTADO_ONLINE="Online";
    public static final String ESTADO_OFFLINE="Offline";
    public static final String FECHA="Creado el";

}
