package com.schoolchat.schoolchat.UserInterface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
import java.util.List;

public class MainActivity extends Activity {
//clase para la vista de los usuarios / grupos de la aplicacion

    private Firebase starFirebase;
    private RecyclerView listaRecyclerView;
    private View barraProgresion;
    private AdaptadorUsuarios AdaptadorUsuarios;
    private Firebase.AuthStateListener autentificar;
    private AuthData miAuthData;
    private String actualUsuarioUid;
    private String actualUsuarioEmail;
    private Firebase ramaUsuarios;
    private ChildEventListener listaUsuarios;
    private ArrayList<String> miListaClaveUsuarios;
    private Firebase EstadoConexion;
    private ValueEventListener cambioconexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Iniciamos Firebase
        starFirebase.setAndroidContext(this);
        starFirebase=new Firebase(conexion.FIREBASE_SCHOOLCHAT);
        //rama usuarios
        ramaUsuarios=new Firebase(conexion.FIREBASE_SCHOOLCHAT).child(conexion.CHILD_USERS);
        //refencia al recyclerview
        listaRecyclerView=(RecyclerView)findViewById(R.id.RecyclerView);
        //referencia a barra de prograsion
        barraProgresion=findViewById(R.id.barraprogreso);
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
        }else{
            irLogin();
        }
    }
    private void consultaUsuariosFirebase(){
        MostrarBarraProgreso();
        listaUsuarios=ramaUsuarios.limitToFirst(50).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ocultarBarraPrograso();
                if(dataSnapshot.exists()){
                    String Uidusuario=dataSnapshot.getKey();
                    if(!Uidusuario.equals(actualUsuarioUid)){
                        //obtener nombre de receptor
                        MoldeUsuario usuario=dataSnapshot.getValue(MoldeUsuario.class);
                        //añadir uid del receptor
                        usuario.setUidreceptor(Uidusuario);
                        //añadir informacion del actual usuario emisor
                        usuario.seteEmail(actualUsuarioEmail);
                        usuario.setUidemisor(actualUsuarioUid);
                        miListaClaveUsuarios.add(Uidusuario);
                        AdaptadorUsuarios.refill(usuario);
                    }else{
                        MoldeUsuario actualusuario=dataSnapshot.getValue(MoldeUsuario.class);
                        String nombreUsuario=actualusuario.getEnombre();
                        AdaptadorUsuarios.setNombreuser(nombreUsuario);
                    }
                }
            }

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
        //guarda el estado de conexion del usuario
        EstadoConexion=ramaUsuarios.child(actualUsuarioUid).child(conexion.CHILD_CONNECT);
        //listener para cuando cambia el estado de conexion
        cambioconexion=starFirebase.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean conectado=(Boolean)dataSnapshot.getValue();
                if(conectado){
                    EstadoConexion.setValue(conexion.ESTADO_ONLINE);
                    //si el usuario se desconecta
                    EstadoConexion.onDisconnect().setValue(conexion.ESTADO_OFFLINE);
                    Toast.makeText(MainActivity.this,"conectado",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this,"desconectado",Toast.LENGTH_SHORT).show();
                }
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
    private void MostrarBarraProgreso(){
        barraProgresion.setVisibility(View.VISIBLE);
    }
    private void ocultarBarraPrograso(){
        if(barraProgresion.getVisibility()==View.VISIBLE){
            barraProgresion.setVisibility(View.GONE);
        }
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
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.salir){
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void logout(){
        if(this.miAuthData!=null){
            EstadoConexion.setValue(conexion.ESTADO_OFFLINE);
            starFirebase.unauth();
            setAuthenticatedUser(null);
        }
    }
}
