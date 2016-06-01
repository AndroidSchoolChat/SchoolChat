package com.schoolchat.schoolchat.UserInterface;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.schoolchat.schoolchat.Adaptadores.AdaptadorDifusion;
import com.schoolchat.schoolchat.Firebase.conexion;
import com.schoolchat.schoolchat.R;
import com.schoolchat.schoolchat.moldes.MoldeUsuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grupo extends AppCompatActivity {
    private View rootView;
    private Firebase starFirebase;
    private Firebase ramaUsuarios;
    private RecyclerView listaRecyclerView;
    private AdaptadorDifusion adaptadordifusion;
    private ArrayList<String> miListaClaveUsuarios;
    private Firebase.AuthStateListener autentificar;
    private AuthData miAuthData;
    private String actualUsuarioUid;
    private String actualUsuarioEmail;
    private ChildEventListener listaUsuarios;
    private TextView mensajeTV;
    private Firebase ramaProfesores;
    private ChildEventListener listaProfesores;
    private Firebase FirebaseChat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grupo);
        rootView=findViewById(R.id.root);
        mensajeTV=(TextView)findViewById(R.id.textoenviar);
        //Iniciamos Firebase
        starFirebase.setAndroidContext(this);
        starFirebase=new Firebase(conexion.FIREBASE_SCHOOLCHAT);
        //rama usuarios
        ramaUsuarios=new Firebase(conexion.FIREBASE_SCHOOLCHAT).child(conexion.CHILD_USERS);
        ramaProfesores=new Firebase(conexion.FIREBASE_SCHOOLCHAT).child(conexion.CHILD_PROFE);
        //refencia al recyclerview
        listaRecyclerView=(RecyclerView)findViewById(R.id.RecyclerView);
        //inicializar adaptador
        List<MoldeUsuario> listavacia=new ArrayList<MoldeUsuario>();
        adaptadordifusion =new AdaptadorDifusion(this,listavacia);
        //conectar recyclerview al adpatadorusuario
        listaRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        listaRecyclerView.setHasFixedSize(true);
        listaRecyclerView.setAdapter(adaptadordifusion);
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
        }else{
            irLogin();
        }
    }
    private void consultaProfesorFirebase(){
        listaProfesores=ramaProfesores.addChildEventListener(new ChildEventListener(){
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s){
                if(dataSnapshot.exists()){
                    //esto provoca un bucle que empieza con el primer hijo de usuarios y acaba con el ultimo
                    String Uidprofe=dataSnapshot.getKey();
                    if(Uidprofe.equals(actualUsuarioUid)){
                        //si se encuentra con si mismo en firebase se ponen los datos nombre y creacion para que funcione el chat
                        MoldeUsuario actualusuario=dataSnapshot.getValue(MoldeUsuario.class);
                        String nombreUsuario=actualusuario.getnombre();
                        String creado=actualusuario.getcreado();
                        adaptadordifusion.setNombre_Fechauser(nombreUsuario,creado);
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
                        adaptadordifusion.refill(usuario);
                    }else{
                        //si se encuentra con si mismo en firebase se ponen los datos nombre y creacion para que funcione el chat
                        MoldeUsuario actualusuario=dataSnapshot.getValue(MoldeUsuario.class);
                        String nombreUsuario=actualusuario.getnombre();
                        String creado=actualusuario.getcreado();
                        adaptadordifusion.setNombre_Fechauser(nombreUsuario,creado);
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
    protected void irLogin(){
        Intent intent=new Intent(this,LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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
    }
    public void botonenviar(View view){
        String nombregrupo=mensajeTV.getText().toString();
        nombregrupo=nombregrupo.trim();
        //llamamos a la variable que contiene los usuarios seleccionados y le asignamos una local
        ArrayList<MoldeUsuario> grupoUsuarios = AdaptadorDifusion.DifusionUsuarios;
        if(!nombregrupo.isEmpty() && grupoUsuarios.size()>0){

            for(int i=0;i<grupoUsuarios.size();i++){
                //recorremos los usuarios uno a uno
                MoldeUsuario usuario=grupoUsuarios.get(i);
                //se estable una clave unica para cada grupo con el fin de tener mas de un grupo con el mismo nombre
                FirebaseChat=new Firebase(conexion.FIREBASE_SCHOOLCHAT).child(conexion.CHILD_GROUPS).child(nombregrupo+'-'+usuario.getUidemisor());
                //establecemos los objetos que se crearan en dentro de cada grupo
                //haciendolo de esta forma se podra añadir usuarios al grupo usando el mismo nombre del grupo
                FirebaseChat.child(usuario.getUidreceptor()).setValue(usuario.getnombre());
                mensajeTV.setText("");
            }
            //ahora se añade al profesor que ha creado el grupo
            MoldeUsuario usuario=grupoUsuarios.get(1);
            FirebaseChat.child(usuario.getUidemisor()).setValue(usuario.getEnombre());
            Snackbar.make(rootView,"Grupo creado correctamente",Snackbar.LENGTH_LONG).show();
        }else {
            if(nombregrupo.isEmpty()){
                Snackbar.make(rootView, "Escribe el nombre del grupo", Snackbar.LENGTH_LONG).show();
            }else {
                if (grupoUsuarios.size() == 0) {
                    Snackbar.make(rootView, "Selecciona alumnos para hacer la creacion del grupo", Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }
}
