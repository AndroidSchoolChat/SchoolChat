package com.schoolchat.schoolchat.UserInterface;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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


public class alumnoFragment extends Fragment {


    public alumnoFragment() {
        // Required empty public constructor
    }

    private Firebase starFirebase;
    private RecyclerView listaRecyclerView;
    private AdaptadorUsuarios AdaptadorUsuarios;
    private Firebase.AuthStateListener autentificar;
    private AuthData miAuthData;
    private String actualUsuarioUid;
    private String actualUsuarioEmail;
    private Firebase ramaUsuarios;
    private ChildEventListener listaUsuarios;
    private ArrayList<String> miListaClaveUsuarios;
    private Firebase EstadoConexion;
    private View rootView;
    public boolean profesor= false;
    private ValueEventListener cambioconexion;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v =inflater.inflate(R.layout.fragment_alumno, container, false);

        rootView=v.findViewById(R.id.root);
        //Iniciamos Firebase
        starFirebase.setAndroidContext(this.getActivity());
        starFirebase=new Firebase(conexion.FIREBASE_SCHOOLCHAT);
        //rama usuarios
        ramaUsuarios=new Firebase(conexion.FIREBASE_SCHOOLCHAT).child(conexion.CHILD_USERS);
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
            consultaUsuariosFirebase();

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
        if(listaUsuarios!=null){
            ramaUsuarios.removeEventListener(listaUsuarios);
        }
        if(cambioconexion!=null){
            starFirebase.getRoot().child(".info/connected").removeEventListener(cambioconexion);
        }

    }
}
