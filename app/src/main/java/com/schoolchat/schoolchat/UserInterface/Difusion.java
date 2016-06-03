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

public class Difusion extends AppCompatActivity {

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
    private TextView tvMensaje;
    private Firebase ramaProfesores;
    private ChildEventListener listaProfesores;
    private Firebase FirebaseChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difusion);//para poder usar snackBar
        rootView=findViewById(R.id.root);
        tvMensaje =(TextView)findViewById(R.id.textoenviar);
        //Iniciamos Firebase
        starFirebase.setAndroidContext(this);
        starFirebase=new Firebase(conexion.FIREBASE_SCHOOLCHAT);
        //rama usuarios
        ramaUsuarios=new Firebase(conexion.FIREBASE_SCHOOLCHAT).child(conexion.RAMA_ALUMNOS);
        ramaProfesores=new Firebase(conexion.FIREBASE_SCHOOLCHAT).child(conexion.RAMA_PROFESORES);
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
                    String UidProfe=dataSnapshot.getKey();
                    if(UidProfe.equals(actualUsuarioUid)){
                        //si se encuentra con si mismo en firebase se ponen los datos nombre y creacion para que funcione el chat
                        MoldeUsuario moldeActualUsu=dataSnapshot.getValue(MoldeUsuario.class);
                        String nombreUsuario=moldeActualUsu.getnombre();
                        String creado=moldeActualUsu.getcreado();
                        adaptadordifusion.setNombre_Fechauser(nombreUsuario,creado);
                    }
                }
            }
            //si el hay cambios en firebase sobre un usuario esto lo resgistra
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()) {
                    String UidProfe = dataSnapshot.getKey();
                    if (!UidProfe.equals(actualUsuarioUid)) {
                        MoldeUsuario usuario = dataSnapshot.getValue(MoldeUsuario.class);
                        usuario.setUidReceptor(UidProfe);
                        usuario.setEmisorEmail(actualUsuarioEmail);
                        usuario.setUidEmisor(actualUsuarioUid);


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
                    String UidUsu=dataSnapshot.getKey();
                    if(!UidUsu.equals(actualUsuarioUid)){
                        //obtener datos del receptor que estan firebase
                        MoldeUsuario moldeUsu=dataSnapshot.getValue(MoldeUsuario.class);
                        //añadir uid del receptor
                        moldeUsu.setUidReceptor(UidUsu);
                        //añadir informacion del actual moldeUsu emisor
                        moldeUsu.setEmisorEmail(actualUsuarioEmail);
                        moldeUsu.setUidEmisor(actualUsuarioUid);
                        miListaClaveUsuarios.add(UidUsu);
                        adaptadordifusion.refill(moldeUsu);
                    }else{
                        //si se encuentra con si mismo en firebase se ponen los datos nombre y creacion para que funcione el chat
                        MoldeUsuario moldeActualUsu=dataSnapshot.getValue(MoldeUsuario.class);
                        String nombreUsuario=moldeActualUsu.getnombre();
                        String creado=moldeActualUsu.getcreado();
                        adaptadordifusion.setNombre_Fechauser(nombreUsuario,creado);
                    }
                }
            }
            //si el hay cambios en firebase sobre un usuario esto lo resgistra
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    String UidUsuario=dataSnapshot.getKey();
                    if(!UidUsuario.equals(actualUsuarioUid)){
                        MoldeUsuario usuario=dataSnapshot.getValue(MoldeUsuario.class);
                        usuario.setUidReceptor(UidUsuario);
                        usuario.setEmisorEmail(actualUsuarioEmail);
                        usuario.setUidEmisor(actualUsuarioUid);
                        int index=miListaClaveUsuarios.indexOf(UidUsuario);
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
        Intent iLogin=new Intent(this,LogInActivity.class);
        iLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        iLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(iLogin);
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
        String enviarmensaje= tvMensaje.getText().toString();
        enviarmensaje=enviarmensaje.trim();
        //llamamos a la variable que contiene los usuarios seleccionados y le asignamos una local
        ArrayList<MoldeUsuario> adaptDifUsu = AdaptadorDifusion.DifusionUsuarios;
        if(!enviarmensaje.isEmpty() && adaptDifUsu.size()>0){
            for(int i=0;i<adaptDifUsu.size();i++){
                //recorremos los usuarios uno a uno
                MoldeUsuario usuario=adaptDifUsu.get(i);
                //establecemos donde va hacer la conexion con firebase en la rama chat
                FirebaseChat=new Firebase(conexion.FIREBASE_SCHOOLCHAT).child(conexion.RAMA_CHAT).child(usuario.getChatRef());
                //establecemos el mensaje
                Map<String,String> mapDifMensaje=new HashMap<>();
                mapDifMensaje.put("emisor",usuario.getUidEmisor());
                mapDifMensaje.put("receptor",usuario.getUidReceptor());
                mapDifMensaje.put("mensaje",enviarmensaje);
                FirebaseChat.push().setValue(mapDifMensaje);
                tvMensaje.setText("");
            }
            Snackbar.make(rootView,"Mensaje enviado correctamente",Snackbar.LENGTH_LONG).show();
        }else {
            if(enviarmensaje.isEmpty()){
                Snackbar.make(rootView, "Escribe para poder enviar el mensaje", Snackbar.LENGTH_LONG).show();
            }else {
                if (adaptDifUsu.size() == 0) {
                    Snackbar.make(rootView, "Selecciona alumnos para hacer la difusion", Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }
}
