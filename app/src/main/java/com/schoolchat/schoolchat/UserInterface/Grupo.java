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
import java.util.List;

public class Grupo extends AppCompatActivity {
    private View rootView;
    private Firebase starFirebase;
    private Firebase ramaUsuarios;
    private RecyclerView listaRecyclerView;
    private AdaptadorDifusion adaptDifusion;
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
        setContentView(R.layout.activity_grupo);
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
        adaptDifusion =new AdaptadorDifusion(this,listavacia);
        //conectar recyclerview al adpatadorusuario
        listaRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        listaRecyclerView.setHasFixedSize(true);
        listaRecyclerView.setAdapter(adaptDifusion);
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
                        MoldeUsuario actualUsu=dataSnapshot.getValue(MoldeUsuario.class);
                        String nombreUsuario=actualUsu.getnombre();
                        String creado=actualUsu.getcreado();
                        adaptDifusion.setNombre_Fechauser(nombreUsuario,creado);
                    }
                }
            }
            //si el hay cambios en firebase sobre un usuario esto lo resgistra
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()) {
                    String UidProfe = dataSnapshot.getKey();
                    if (!UidProfe.equals(actualUsuarioUid)) {
                        MoldeUsuario moldeUsu = dataSnapshot.getValue(MoldeUsuario.class);
                        moldeUsu.setUidReceptor(UidProfe);
                        moldeUsu.setEmisorEmail(actualUsuarioEmail);
                        moldeUsu.setUidEmisor(actualUsuarioUid);


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
                        adaptDifusion.refill(moldeUsu);
                    }else{
                        //si se encuentra con si mismo en firebase se ponen los datos nombre y creacion para que funcione el chat
                        MoldeUsuario actualUsu=dataSnapshot.getValue(MoldeUsuario.class);
                        String nombreUsuario=actualUsu.getnombre();
                        String creadoUsu=actualUsu.getcreado();
                        adaptDifusion.setNombre_Fechauser(nombreUsuario,creadoUsu);
                    }
                }
            }
            //si el hay cambios en firebase sobre un usuario esto lo resgistra
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()){
                    String UidUsu=dataSnapshot.getKey();
                    if(!UidUsu.equals(actualUsuarioUid)){
                        MoldeUsuario moldeUsu=dataSnapshot.getValue(MoldeUsuario.class);
                        moldeUsu.setUidReceptor(UidUsu);
                        moldeUsu.setEmisorEmail(actualUsuarioEmail);
                        moldeUsu.setUidEmisor(actualUsuarioUid);
                        int index=miListaClaveUsuarios.indexOf(UidUsu);
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
        String nombreGrupo= tvMensaje.getText().toString();
        nombreGrupo=nombreGrupo.trim();
        //llamamos a la variable que contiene los usuarios seleccionados y le asignamos una local
        ArrayList<MoldeUsuario> grupoUsuarios = AdaptadorDifusion.DifusionUsuarios;
        if(!nombreGrupo.isEmpty() && grupoUsuarios.size()>0){

            for(int i=0;i<grupoUsuarios.size();i++){
                //recorremos los usuarios uno a uno
                MoldeUsuario moldeGrupoUsu=grupoUsuarios.get(i);
                //se estable una clave unica para cada grupo con el fin de tener mas de un grupo con el mismo nombre
                FirebaseChat=new Firebase(conexion.FIREBASE_SCHOOLCHAT).child(conexion.RAMA_GRUPOS).child(nombreGrupo+'-'+moldeGrupoUsu.getUidEmisor());
                //establecemos los objetos que se crearan en dentro de cada grupo
                //haciendolo de esta forma se podra añadir usuarios al grupo usando el mismo nombre del grupo
                FirebaseChat.child(moldeGrupoUsu.getUidReceptor()).setValue(moldeGrupoUsu.getnombre());
                tvMensaje.setText("");
            }
            //ahora se añade al profesor que ha creado el grupo
            MoldeUsuario usuario=grupoUsuarios.get(1);
            FirebaseChat.child(usuario.getUidEmisor()).setValue(usuario.getEmisorNombre());
            Snackbar.make(rootView,"Grupo creado correctamente",Snackbar.LENGTH_LONG).show();
        }else {
            if(nombreGrupo.isEmpty()){
                Snackbar.make(rootView, "Escribe el nombre del grupo", Snackbar.LENGTH_LONG).show();
            }else {
                if (grupoUsuarios.size() == 0) {
                    Snackbar.make(rootView, "Selecciona alumnos para hacer la creacion del grupo", Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }
}
