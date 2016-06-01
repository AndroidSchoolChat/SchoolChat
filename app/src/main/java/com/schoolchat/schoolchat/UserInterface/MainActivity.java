package com.schoolchat.schoolchat.UserInterface;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.schoolchat.schoolchat.Adaptadores.AdaptadorUsuarios;
import com.schoolchat.schoolchat.Firebase.conexion;
import com.schoolchat.schoolchat.R;
import com.schoolchat.schoolchat.moldes.MoldeUsuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
//clase para la vista de los usuarios / grupos de la aplicacion

    private Firebase starFirebase;
    private RecyclerView listaRecyclerView;
    private AdaptadorUsuarios AdaptadorUsuarios;
    private Firebase.AuthStateListener autentificar;
    private AuthData miAuthData;
    private String actualUsuarioUid;
    private String actualUsuarioEmail;
    private Firebase ramaUsuarios;
    private Firebase ramaProfesores;
    private Firebase ramaGrupo;
    private ChildEventListener listaUsuarios;
    private ChildEventListener listaProfesores;
    private ChildEventListener listaGrupos;
    private ArrayList<String> miListaClaveUsuarios;
    private Firebase EstadoConexion;
    private Firebase estadoConexionProfe, estadoConexionUser;
    private ValueEventListener cambioconexion;
    private View rootView;
    public boolean profesor= false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //para poder usar snackBar
        rootView=findViewById(R.id.root);
        //Iniciamos Firebase
        starFirebase.setAndroidContext(this);
        starFirebase=new Firebase(conexion.FIREBASE_SCHOOLCHAT);
        //rama usuarios
        ramaUsuarios=new Firebase(conexion.FIREBASE_SCHOOLCHAT).child(conexion.CHILD_USERS);
        ramaProfesores=new Firebase(conexion.FIREBASE_SCHOOLCHAT).child(conexion.CHILD_PROFE);
        ramaGrupo=new Firebase(conexion.FIREBASE_SCHOOLCHAT).child(conexion.CHILD_GROUPS);
        //refencia al recyclerview
        listaRecyclerView=(RecyclerView)findViewById(R.id.RecyclerView);
        //inicializar adaptador
        List<MoldeUsuario> listavacia=new ArrayList<MoldeUsuario>();
        AdaptadorUsuarios =new AdaptadorUsuarios(this,listavacia);
        //conectar recyclerview al adpatadorusuario
        listaRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        listaRecyclerView.setHasFixedSize(true);
        listaRecyclerView.setAdapter(AdaptadorUsuarios);
        miListaClaveUsuarios=new ArrayList<String>();
        autentificar=new Firebase.AuthStateListener(){

            @Override
            public void onAuthStateChanged(AuthData authData) {
                setAuthenticatedUser(authData);
            }
        };
        starFirebase.addAuthStateListener(autentificar);

    }
    private void setAuthenticatedUser(AuthData authData){
        miAuthData=authData;
        if(authData!=null){
            //la autentificacion del usuario no a experido
            //obtener su Uid
            actualUsuarioUid=authData.getUid();
            //obtener su email
            actualUsuarioEmail=(String)authData.getProviderData().get(conexion.KEY_EMAIL);
            consultaUsuariosFirebase();
            consultaProfesorFirebase();
            consultaGruposfirebase();
        }else{
            irLogin();
        }
    }
    private void consultaUsuariosFirebase(){


        listaUsuarios=ramaUsuarios.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if(dataSnapshot.exists()){
                    //esto provoca un bucle que empieza con el primer hijo de usuarios y acaba con el ultimo
                    String Uidusuario=dataSnapshot.getKey();
                    if(!Uidusuario.equals(actualUsuarioUid)){
                        //obtener datos del receptor que estan firebase
                        MoldeUsuario usuario=dataSnapshot.getValue(MoldeUsuario.class);
                        //añadir uid del receptor
                        usuario.setUidreceptor(Uidusuario);
                        //añadir informacion del actual usuario emisor
                        usuario.seteEmail(actualUsuarioEmail);
                        usuario.setUidemisor(actualUsuarioUid);
                        miListaClaveUsuarios.add(Uidusuario);
                        AdaptadorUsuarios.refill(usuario);
                    }else{
                        //si se encuentra con si mismo en firebase se ponen los datos nombre y creacion para que funcione el chat
                        MoldeUsuario actualusuario=dataSnapshot.getValue(MoldeUsuario.class);
                        String nombreUsuario=actualusuario.getnombre();
                        String creado=actualusuario.getcreado();
                        AdaptadorUsuarios.setNombre_Fechauser(nombreUsuario,creado);
                        //guarda el estado de conexion del usuario
                        EstadoConexion=ramaUsuarios.child(actualUsuarioUid).child(conexion.CHILD_CONNECT);
                        //listener para cuando cambia el estado de conexion
                        cambioconexion=starFirebase.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                boolean conectado = (Boolean) dataSnapshot.getValue();
                                if (conectado) {
                                    EstadoConexion.setValue(conexion.ESTADO_ONLINE);
                                    //si el usuario se desconecta
                                    EstadoConexion.onDisconnect().setValue(conexion.ESTADO_OFFLINE);
                                    Snackbar.make(rootView, "conectado", Snackbar.LENGTH_LONG).show();
                                } else {
                                    Snackbar.make(rootView, "desconectado", Snackbar.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }
                }
            }
            //si el hay cambios en firebase sobre un usuario esto lo resgistra
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    String Uidusuario=dataSnapshot.getKey();
                    if(!Uidusuario.equals(actualUsuarioUid)){
                        MoldeUsuario usuario=dataSnapshot.getValue(MoldeUsuario.class);
                        usuario.setUidreceptor(Uidusuario);
                        usuario.seteEmail(actualUsuarioEmail);
                        usuario.setUidemisor(actualUsuarioUid);
                        int index=miListaClaveUsuarios.indexOf(Uidusuario);
                        AdaptadorUsuarios.cambioUsuario(index,usuario);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



    }
    private void consultaProfesorFirebase(){
        listaProfesores=ramaProfesores.addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s){
                if(dataSnapshot.exists()){
                    //esto provoca un bucle que empieza con el primer hijo de usuarios y acaba con el ultimo
                    String Uidprofe=dataSnapshot.getKey();
                    if(!Uidprofe.equals(actualUsuarioUid)){
                        //obtener datos del receptor que estan firebase
                        MoldeUsuario usuario=dataSnapshot.getValue(MoldeUsuario.class);
                        //añadir uid del receptor
                        usuario.setUidreceptor(Uidprofe);
                        //añadir informacion del actual usuario emisor
                        usuario.seteEmail(actualUsuarioEmail);
                        usuario.setUidemisor(actualUsuarioUid);
                        miListaClaveUsuarios.add(Uidprofe);
                        AdaptadorUsuarios.refill(usuario);
                    }else{
                        //si se encuentra con si mismo en firebase se ponen los datos nombre y creacion para que funcione el chat
                        MoldeUsuario actualusuario=dataSnapshot.getValue(MoldeUsuario.class);
                        String nombreUsuario=actualusuario.getnombre();
                        String creado=actualusuario.getcreado();
                        AdaptadorUsuarios.setNombre_Fechauser(nombreUsuario,creado);
                        //guarda el estado de conexion del usuario
                        EstadoConexion=ramaProfesores.child(actualUsuarioUid).child(conexion.CHILD_CONNECT);
                        //listener para cuando cambia el estado de conexion
                        cambioconexion=starFirebase.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                boolean conectado = (Boolean) dataSnapshot.getValue();
                                if (conectado) {
                                    EstadoConexion.setValue(conexion.ESTADO_ONLINE);
                                    //si el usuario se desconecta
                                    EstadoConexion.onDisconnect().setValue(conexion.ESTADO_OFFLINE);
                                    Snackbar.make(rootView, "conectado", Snackbar.LENGTH_SHORT).show();
                                    profesor=true;
                                } else {
                                    Snackbar.make(rootView, "desconectado", Snackbar.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                    }
                }
            }
            //si el hay cambios en firebase sobre un usuario esto lo resgistra
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()) {
                    String Uidprofe = dataSnapshot.getKey();
                    if (!Uidprofe.equals(actualUsuarioUid)) {
                        MoldeUsuario usuario = dataSnapshot.getValue(MoldeUsuario.class);
                        usuario.setUidreceptor(Uidprofe);
                        usuario.seteEmail(actualUsuarioEmail);
                        usuario.setUidemisor(actualUsuarioUid);


                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



    }
    //parte para los grupos
    private void consultaGruposfirebase(){
        listaGrupos=ramaGrupo.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    //cogemos el uid del grupo nombregrupo-uidprofecerador
                    String UidGrupo=dataSnapshot.getKey();
                    //buscamos donde esta el guion que une el nombre y el profe
                    int guion=UidGrupo.indexOf('-');
                    //obtenemos el nombre del grupo que esta dentro de Uid grupo
                    String nombreGrupo=UidGrupo.substring(0,guion);

                    if(dataSnapshot.child(actualUsuarioUid).exists()) {
                        //creamos lo que va a ser un grupo
                        MoldeUsuario usuario = dataSnapshot.getValue(MoldeUsuario.class);
                        //se añade toda la informacion necesaria para acceder al chat puesto que no esta guardada en firebase
                        //añadir uid del receptor
                        usuario.setUidreceptor(UidGrupo);
                        //añadir informacion del actual usuario emisor
                        usuario.seteEmail(UidGrupo);
                        usuario.setUidemisor(actualUsuarioUid);
                        //esto es necesario para que no se rompa la aplicacion
                        usuario.setConexion("desconectado");
                        usuario.setNombre(nombreGrupo);
                        //este campo esta vacia para tener una sola referencia en la rama chat y se pueda acceder a ella con distintos usuarios
                        usuario.setEmail("");
                        //el valor de esta variables da un poco igual pero son necesaria para que funcione la actividad del chat
                        usuario.setCreado("0");
                        usuario.seteCreado("1");
                        miListaClaveUsuarios.add(UidGrupo);
                        AdaptadorUsuarios.refill(usuario);

                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    //metodo para ir al login
    protected void irLogin(){
        Intent intent=new Intent(this,LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        starFirebase.removeAuthStateListener(autentificar);
        miListaClaveUsuarios.clear();
        //detener todos los listener
        if(listaUsuarios!=null){
            ramaUsuarios.removeEventListener(listaUsuarios);
        }
        if(cambioconexion!=null){
            starFirebase.getRoot().child(".info/connected").removeEventListener(cambioconexion);
        }
        if(listaProfesores!=null){
            ramaProfesores.removeEventListener(listaProfesores);
        }
    }

    //Con onPrepareOptionsMenu se elige el archivo xml que se va a usar dependiendo de la condición
    public boolean onPrepareOptionsMenu(Menu menu){
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        if (profesor==true){
            inflater.inflate(R.menu.menu_profesor, menu);
        }else{
            inflater.inflate(R.menu.menu_alumno, menu);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.salir:
                logout();
                break;
            case R.id.difusion:
                difusion();
                break;
            case R.id.grupo:
                crearGrupo();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    //metodo para acabar la sesion
    private void logout(){
        if(this.miAuthData!=null){
            EstadoConexion.setValue(conexion.ESTADO_OFFLINE);
            starFirebase.unauth();
            setAuthenticatedUser(null);
        }
    }
    //metodo para lanzar la actividad difusion
    protected void difusion(){
        Intent i=new Intent(MainActivity.this,Difusion.class);
        startActivity(i);
    }
    protected void crearGrupo(){
        Intent i=new Intent(MainActivity.this,Grupo.class);
        startActivity(i);
    }
}
