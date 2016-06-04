package com.schoolchat.schoolchat.UserInterface;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class gruposFragment extends Fragment {


    public gruposFragment() {
        // Required empty public constructor
    }
    private Firebase starFirebase;
    private RecyclerView listaRecyclerView;
    private com.schoolchat.schoolchat.Adaptadores.AdaptadorUsuarios AdaptadorUsuarios;
    private Firebase.AuthStateListener autentificar;
    private AuthData miAuthData;
    private String actualUsuarioUid;
    private String actualUsuarioEmail;
    private Firebase ramaGrupo;
    private ChildEventListener listaGrupos;
    private ArrayList<String> miListaClaveUsuarios;
    private Firebase EstadoConexion;
    private View rootView;
    public boolean profesor= false;
    private ValueEventListener cambioconexion;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_grupos, container, false);

        rootView=v.findViewById(R.id.root);
        //Iniciamos Firebase
        starFirebase.setAndroidContext(this.getActivity());
        starFirebase=new Firebase(conexion.FIREBASE_SCHOOLCHAT);
        //rama usuarios
        ramaGrupo=new Firebase(conexion.FIREBASE_SCHOOLCHAT).child(conexion.CHILD_GROUPS);
        //refencia al recyclerview
        listaRecyclerView=(RecyclerView)v.findViewById(R.id.RecyclerView);
        //inicializar adaptador
        List<MoldeUsuario> listavacia=new ArrayList<MoldeUsuario>();
        AdaptadorUsuarios =new AdaptadorUsuarios(this.getActivity(),listavacia);
        //conectar recyclerview al adpatadorusuario
        listaRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
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
        return v;
    }

    private void setAuthenticatedUser(AuthData authData){
        miAuthData=authData;
        if(authData!=null){
            //la autentificacion del usuario no a experido
            //obtener su Uid
            actualUsuarioUid=authData.getUid();
            //obtener su email
            actualUsuarioEmail=(String)authData.getProviderData().get(conexion.KEY_EMAIL);
            consultaGruposFirebase();

        }else{
            irLogin();
        }
    }
    //parte para los grupos
    private void consultaGruposFirebase(){
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
        Intent intent=new Intent(this.getActivity(),LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        starFirebase.removeAuthStateListener(autentificar);
        miListaClaveUsuarios.clear();
        //detener todos los listener

        if(cambioconexion!=null){
            starFirebase.getRoot().child(".info/connected").removeEventListener(cambioconexion);
        }

    }


}
