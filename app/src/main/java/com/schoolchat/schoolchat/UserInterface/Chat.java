package com.schoolchat.schoolchat.UserInterface;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.schoolchat.schoolchat.Adaptadores.AdaptadorConversacion;
import com.schoolchat.schoolchat.Firebase.conexion;
import com.schoolchat.schoolchat.R;
import com.schoolchat.schoolchat.moldes.MoldeMensajes;
import com.schoolchat.schoolchat.moldes.MoldeUsuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chat extends AppCompatActivity {
    private RecyclerView recyclerViewChat;
    private TextView mensajeTV;
    private AdaptadorConversacion adaptadorConversacion;
    //Emisor o Receptor
    private static final int Emisor=0;
    private static final int Receptor=1;
    //uid receptor
    private String Uidreceptor;
    //uid emisor
    private String Uidemisor;
    //referencia a firebase para una esta conversacion
    private Firebase FirebaseChat;
    //Listener para
    private ChildEventListener FirebaseChatListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //informacion de la actividad anterior
        Intent DatosUsuarios=getIntent();
        MoldeUsuario DatosUsuariosMolde=DatosUsuarios.getParcelableExtra(conexion.INFO_USER);
        //set uid receptor
        Uidreceptor=DatosUsuariosMolde.getUidreceptor();
        //set uid emisor
        Uidemisor=DatosUsuariosMolde.getUidemisor();
        //establecer adaptador para chat
        recyclerViewChat=(RecyclerView)findViewById(R.id.chat_recycler_view);
        //asignar el edittext a un textview
        mensajeTV=(TextView)findViewById(R.id.textoenviar);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChat.setHasFixedSize(true);
        List<MoldeMensajes> chatvacio=new ArrayList<MoldeMensajes>();
        adaptadorConversacion=new AdaptadorConversacion(chatvacio);
        recyclerViewChat.setAdapter(adaptadorConversacion);
        FirebaseChat=new Firebase(conexion.FIREBASE_SCHOOLCHAT).child(conexion.CHILD_CHAT).child(DatosUsuariosMolde.getChatRef());

    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onStart(){
        super.onStart();
        FirebaseChatListener=FirebaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //establece quie es el emisor y el receptor
                if(dataSnapshot.exists()){
                    MoldeMensajes nuevomensaje=dataSnapshot.getValue(MoldeMensajes.class);
                    if(nuevomensaje.getEmisor().equals(Uidemisor)){
                        nuevomensaje.setReceptorOEmisor(Emisor);
                    }else{
                        nuevomensaje.setReceptorOEmisor(Receptor);
                    }
                    adaptadorConversacion.refillAdapter(nuevomensaje);
                    recyclerViewChat.scrollToPosition(adaptadorConversacion.getItemCount()-1);
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
    @Override
    protected void onPause() {
        super.onPause();

    }
    @Override
    protected void onStop(){
        super.onStop();
        //eliminar listener
        if(FirebaseChatListener!=null){
            FirebaseChat.removeEventListener(FirebaseChatListener);
        }
        adaptadorConversacion.cleanUp();
    }
    //evento para enviar mensajes
    public void botonenviar(View view){
        String enviarmensaje=mensajeTV.getText().toString();
        enviarmensaje=enviarmensaje.trim();
        if(!enviarmensaje.isEmpty()){
            Map<String,String> nuevomensaje=new HashMap<>();
            nuevomensaje.put("emisor",Uidemisor);
            nuevomensaje.put("receptor",Uidreceptor);
            nuevomensaje.put("mensaje",enviarmensaje);
            FirebaseChat.push().setValue(nuevomensaje);
            mensajeTV.setText("");
        }
    }

}
