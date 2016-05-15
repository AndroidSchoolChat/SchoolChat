package com.schoolchat.schoolchat.UserInterface;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.schoolchat.schoolchat.R;
import com.schoolchat.schoolchat.Firebase.conexion;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Altausuario extends Activity {

    private Button botonRegistrarse;
    private Button botonCancelar;

    private EditText ed_Em;
    private EditText ed_NomUsu;
    private EditText ed_Cont;


    private EditText ed_ContVerif;
    private CheckBox cb_Profesor;

    public String verif;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);     //AÑADIDO DEBIDO A QUE ES NECESARIO PARA QUE FUNCIONE FIREBASE
        setContentView(R.layout.activity_altausuario);


        botonRegistrarse = (Button) findViewById(R.id.bt_Registrarse);
        botonCancelar = (Button) findViewById(R.id.bt_Cancelar);

        ed_Em = (EditText) findViewById(R.id.ed_Email);
        ed_NomUsu = (EditText) findViewById(R.id.ed_NombreUsuario);
        ed_Cont = (EditText) findViewById(R.id.ed_Contraseña);


        //Verificacion si es profesor:
        ed_ContVerif = (EditText) findViewById(R.id.ed_ContraseñaVerif);
        cb_Profesor = (CheckBox) findViewById(R.id.cb_Profesor);


    }


    //Muestra el campo de la contraseña de verificacion si el checkbox esta seleccionado
    public void onProfesor(View v) {
        if (cb_Profesor.isChecked()) {
            ed_ContVerif.setVisibility(View.VISIBLE);
        } else {
            ed_ContVerif.setVisibility(View.INVISIBLE);
        }
    }



    /*  Accion a realizar al pulsar en REGISTRAR: Ir a la actividad MainActivity
      * y crear un nuevo usuario en la base de datos de firebase */



    public void onSign(View v) {

        //**--Codigo para el registro del PROFESOR:--*

        if (cb_Profesor.isChecked()) {

            //variables para registro de profesor pasadas a string
            final String useremailProf = ed_Em.getText().toString();
            final String userNameProf = ed_NomUsu.getText().toString();
            final String userpasswordProf = ed_Cont.getText().toString();

            //Comprobacion que en los campos se haya introducido datos:
            if (useremailProf.isEmpty() || userNameProf.isEmpty() || userpasswordProf.isEmpty()) {
                MostrarError("Asegurese de que todos los campos esten rellenos");

            } else {

                //Conexion a FireBase para recuperar el valor de la contraseña de verificacion:

                final Firebase FireBaseDatos = new Firebase(conexion.FIREBASE_SCHOOLCHAT);
                FireBaseDatos.addValueEventListener(new ValueEventListener(){
                    public void onDataChange(DataSnapshot snapshot) {

                        verif = (String) snapshot.child("VerifyPassword").child("pass").getValue();

                        //Comprobacion en el IF de que la contraseña de verificacion introducida es correcta.

                        if(verif.equals(ed_ContVerif.getText().toString())){

                            //En caso de ser correcto creara el usuario.
                            final Firebase registroProfesor = new Firebase(conexion.FIREBASE_SCHOOLCHAT);

                            final String finalUserEmail = useremailProf;
                            final String finalUserPassword = userpasswordProf;
                            final String finalUserName = userNameProf;

                            //crear el usuario
                            registroProfesor.createUser(useremailProf, userpasswordProf, new Firebase.ValueResultHandler<Map<String, Object>>() {


                                @Override
                                public void onSuccess(Map<String, Object> stringObjectMap) {

                                 /*   AlertDialog.Builder errorVerif= new AlertDialog.Builder(Altausuario.this);
                                    errorVerif.setMessage(R.string.CorrectoProf);
                                    errorVerif.setPositiveButton(android.R.string.ok,null);
                                    errorVerif.show();
*/
                                    Toast.makeText(Altausuario.this, "Se ha registrado un nuevo profesor.", Toast.LENGTH_SHORT).show();

                                    registroProfesor.authWithPassword(finalUserEmail, finalUserPassword, new Firebase.AuthResultHandler() {

                                        @Override
                                        public void onAuthenticated(AuthData authData) {
                                            Map<String, Object> map = new HashMap<String, Object>();
                                            map.put(conexion.NOMBRE, finalUserName);
                                            map.put(conexion.USER_EMAIL, finalUserEmail);
                                            map.put(conexion.CHILD_CONNECT, conexion.ESTADO_OFFLINE);


                                            Date fecha = new Date(); //CAMBIADO*GABRI
                                            map.put(conexion.FECHA, fecha);


                                            registroProfesor.child("Profesores:").child(authData.getUid()).setValue(map); //Añade los datos a Firebase en la pestaña de PROFESOR



                                                Intent return_login = new Intent(Altausuario.this, LogInActivity.class);
                                                return_login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                return_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(return_login);

                                        }

                                        @Override
                                        public void onAuthenticationError(FirebaseError firebaseError) {

                                        }
                                    });
                                }

                                @Override
                                public void onError(FirebaseError firebaseError) {
                                    //si el usuario tiene problemas con el registro se le notificara un error
                                    MostrarError(firebaseError.getMessage());
                                }
                            });


                            //En caso de no ser correcta la contraseña se muestrara el mensaje de error correspondiente.
                        }else{


                                AlertDialog.Builder errorVerif = new AlertDialog.Builder(Altausuario.this);
                                errorVerif.setMessage(R.string.errorVerif);
                                errorVerif.setPositiveButton(android.R.string.ok, null);
                                errorVerif.show();

                        }

                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        System.out.println("The read failed: " + firebaseError.getMessage());
                    }
                });

            }


        } else {

            //**--Codigo para el registro del ALUMNO:--*

            //variables para registro pasadas a string
            String useremailAlum = ed_Em.getText().toString();
            String userNameAlum = ed_NomUsu.getText().toString();
            String userpasswordAlum = ed_Cont.getText().toString();
            if (useremailAlum.isEmpty() || userNameAlum.isEmpty() || userpasswordAlum.isEmpty()) {
                MostrarError("Asegurese de que todos los campos esten rellenos");
            } else {
                final Firebase registroUsuario = new Firebase(conexion.FIREBASE_SCHOOLCHAT);
                final String finalUserEmail = useremailAlum;
                final String finalUserPassword = userpasswordAlum;
                final String finalUserName = userNameAlum;

                //crear el usuario
                registroUsuario.createUser(useremailAlum, userpasswordAlum, new Firebase.ValueResultHandler<Map<String, Object>>() {


                    @Override
                    public void onSuccess(Map<String, Object> stringObjectMap) {

                        Toast.makeText(Altausuario.this, "Se ha registrado un nuevo alumno", Toast.LENGTH_SHORT).show();

                        registroUsuario.authWithPassword(finalUserEmail, finalUserPassword, new Firebase.AuthResultHandler() {

                            @Override
                            public void onAuthenticated(AuthData authData) {
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put(conexion.NOMBRE, finalUserName);
                                map.put(conexion.USER_EMAIL, finalUserEmail);
                                map.put(conexion.CHILD_CONNECT, conexion.ESTADO_OFFLINE);


                                Date fecha = new Date(); //CAMBIADO*GABRI
                                map.put(conexion.FECHA, fecha);


                                //comento esto de momento registroUsuario.child("Alumnos:").child(authData.getUid()).setValue(map); //Añade datos en la pestaña de ALUMNO.
                                registroUsuario.child(conexion.CHILD_USERS).child(authData.getUid()).setValue(map); //Añade datos en la pestaña de usuarios.


                                Intent return_login = new Intent(Altausuario.this, LogInActivity.class);
                                return_login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                return_login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(return_login);
                            }

                            @Override
                            public void onAuthenticationError(FirebaseError firebaseError) {

                            }
                        });
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        //si el usuario tiene problemas con el registro se le notificara un error
                        MostrarError(firebaseError.getMessage());
                    }
                });
            }


        }


    }


    //metodo para crear mensajes de alerta de errores
    private void MostrarError(String error) {
        //Create an AlertDialog to show error message
        AlertDialog.Builder builder = new AlertDialog.Builder(Altausuario.this);
        builder.setMessage(error)
                .setTitle(getString(R.string.title_LogIn))
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //La accion de cancelar te devuelve al la actividad de LogIn
    public void onCancelar(View v) {
        Intent j = new Intent(this, LogInActivity.class);
        j.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        j.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(j);
    }


}

