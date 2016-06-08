package com.schoolchat.schoolchat.UserInterface;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.schoolchat.schoolchat.R;
import com.schoolchat.schoolchat.Firebase.conexion;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public class LogInActivity extends AppCompatActivity {
    //creando variables para los botones y edittext
    private EditText edEmail;
    private EditText edContras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        //this.getActionBar().hide();
        //asociando las variables
        edEmail =(EditText)findViewById(R.id.userEmail);
        edContras =(EditText)findViewById(R.id.password);

        //De esta manera controlamos que el teclado no se abra al iniciar la actividad
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


    }
    public void onLogin(View v){
        //crear variables para almacenar el contenido de los edittext
        String edEmText= edEmail.getText().toString();
        String edContrasText= edContras.getText().toString();
        //comprobar que los campos no estan vacios
        if(edEmText.isEmpty()||edContrasText.isEmpty()){
            MostrarError("Asegurate que los campos email o contraseña no esten vacios");
        }else{
            //procede a la autentificacion
            Firebase loginCorrecto=new Firebase(conexion.FIREBASE_SCHOOLCHAT);
            loginCorrecto.authWithPassword(edEmText,edContrasText,authResultHandler);
        }
    }
    public void onRegistrar(View v){
        Intent inRegistrar=new Intent(this,Altausuario.class);
        startActivity(inRegistrar);
    }
    //crea un handler para manejar el resultado de la autentificacion
    //esto se guardara 24 horas o el tiempo establecido en firebase->Login and Auth
    Firebase.AuthResultHandler authResultHandler=new Firebase.AuthResultHandler(){
        @Override
        //la autentificacion ha ido bien y se lanza la mainactivity
        public void onAuthenticated(AuthData authData) {
            Intent inMain=new Intent(LogInActivity.this,MainActivity.class);
            inMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            inMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(inMain);
        }
        //la autentificacion ha ido mal
        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            MostrarError(firebaseError.getMessage());
        }
    };
    //creacion de mensaje de alerta
    private void MostrarError(String error){
        AlertDialog.Builder builder=new AlertDialog.Builder(LogInActivity.this);
        builder.setMessage(error)
                .setTitle("Comprobar")
                .setPositiveButton(android.R.string.ok,null);
        AlertDialog dialog=builder.create();
        dialog.show();
    }
}
