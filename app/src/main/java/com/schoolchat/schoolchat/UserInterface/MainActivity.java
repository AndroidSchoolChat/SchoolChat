package com.schoolchat.schoolchat.UserInterface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.firebase.client.Firebase;

import com.schoolchat.schoolchat.Firebase.conexion;
import com.schoolchat.schoolchat.R;

public class MainActivity extends Activity {
//clase para la vista de los usuarios / grupos de la aplicacion

    private Firebase starFirebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Iniciamos Firebase
        starFirebase.setAndroidContext(this);
        starFirebase=new Firebase(conexion.FIREBASE_SCHOOLCHAT);
        Intent intent=new Intent(this,LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
