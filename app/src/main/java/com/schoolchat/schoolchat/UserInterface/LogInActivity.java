package com.schoolchat.schoolchat.UserInterface;

import android.app.Activity;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.schoolchat.schoolchat.R;
import com.schoolchat.schoolchat.Firebase.conexion;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LogInActivity extends Activity {
    //creando variables para los botones y edittext
    private EditText Email;
    private EditText Password;
    private Button Login;
    private Button Registrar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        //this.getActionBar().hide();
        //asociando las variables
        Email=(EditText)findViewById(R.id.userEmail);
        Password=(EditText)findViewById(R.id.password);
        Login=(Button)findViewById(R.id.login);
        Registrar=(Button)findViewById(R.id.registrar);


    }
    public void onLogin(View v){
        //crear variables para almacenar el contenido de los edittext
        String username=Email.getText().toString();
        String password=Password.getText().toString();
        //comprobar que los campos no estan vacios
        if(username.isEmpty()||password.isEmpty()){
            showErrorMessageToUser("Asegurate que los campos email o contraseña no esten vacios");
        }else{
            //procede a la autentificacion
            Firebase autentificacionusuario=new Firebase(conexion.FIREBASE_SCHOOLCHAT);
            autentificacionusuario.authWithPassword(username,password,authResultHandler);
        }
    }
    public void onRegistrar(View v){

    }
    //crea un handler para manejar el resultado de la autentificacion
    //esto se guardara 24 horas o el tiempo establecido en firebase->Login and Auth
    Firebase.AuthResultHandler authResultHandler=new Firebase.AuthResultHandler(){
        @Override
        //la autentificacion ha ido bien y se lanza la mainactivity
        public void onAuthenticated(AuthData authData) {
            Intent i=new Intent(LogInActivity.this,MainActivity.class);
            startActivity(i);
        }
        //la autentificacion ha ido mal
        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            showErrorMessageToUser(firebaseError.getMessage());
        }
    };
    //creacion de mensaje de alerta
    private void showErrorMessageToUser(String errorMessage){
        AlertDialog.Builder builder=new AlertDialog.Builder(LogInActivity.this);
        builder.setMessage(errorMessage)
                .setTitle("Checking")
                .setPositiveButton(android.R.string.ok,null);
        AlertDialog dialog=builder.create();
        dialog.show();
    }
}
