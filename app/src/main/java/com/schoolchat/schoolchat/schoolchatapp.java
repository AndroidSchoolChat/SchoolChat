package com.schoolchat.schoolchat;

import android.app.Application;

import com.firebase.client.Firebase;

public class schoolchatapp extends Application {
 //para la conexion (?)
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
