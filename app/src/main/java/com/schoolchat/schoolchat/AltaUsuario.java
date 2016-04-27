package com.schoolchat.schoolchat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;



    /*  Actividad nueva para la interfaz de los datos que se pediran al registrarse.  */

public class AltaUsuario extends Activity {

    /*  Declaracion de variables:  */

    Button botonRegistrarse;
    Button botonCancelar;

    EditText ed_Em;
    EditText ed_NomUsu;
    EditText ed_Cont;


    private static final String TAG = AltaUsuario.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_altausuario);



        botonRegistrarse = (Button) findViewById(R.id.bt_Registrarse);
        botonCancelar = (Button) findViewById(R.id.bt_Cancelar);

        ed_Em = (EditText) findViewById(R.id.ed_Email);
        ed_NomUsu = (EditText) findViewById(R.id.ed_NombreUsuario);
        ed_Cont = (EditText) findViewById(R.id.ed_Contraseña);


        botonRegistrarse.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {



                String textoEm = ed_Em.getText().toString();
                String textoNomUsu = ed_NomUsu.getText().toString();
                String textoContr = ed_Cont.getText().toString();


        /* Quitamos los espacios: */
                textoEm = textoEm.trim();
                textoNomUsu = textoNomUsu.trim();
                textoContr = textoContr.trim();


        /* Si los campos correo, nombre de usuario y la contraseña estan vacios, se mostrara un mensaje de error. */

                if (textoEm.isEmpty() || textoNomUsu.isEmpty() || textoContr.isEmpty()) {
                    Toast.makeText(AltaUsuario.this, (getString(R.string.error_registro_mensaje)), Toast.LENGTH_LONG).show();

            /* Se creara el usuario y si los datos introducidos son validos, se le logeara. */

                } else {

                    final Firebase regUsuSchoolChat = new Firebase(ReferenciasURL.FIREBASE_CHAT_URL);
                    final String CorreoAceptado = textoEm;
                    final String NombreUsuAceptado = textoNomUsu;
                    final String ContraseñaAcepatada = textoContr;


                    regUsuSchoolChat.createUser(textoEm, textoContr, new Firebase.ValueResultHandler<Map<String, Object>>() {

                        @Override
                        public void onSuccess(Map<String, Object> result) {


                            Toast.makeText(AltaUsuario.this, "Registro realizado correctamente!", Toast.LENGTH_SHORT).show();

                            regUsuSchoolChat.authWithPassword(CorreoAceptado, ContraseñaAcepatada, new Firebase.AuthResultHandler() {

                                @Override
                                public void onAuthenticated(AuthData authData) {

                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put(ReferenciasURL.provider, authData.getProvider()); //El metodo de autentificacion.

                                    map.put(ReferenciasURL.nom, NombreUsuAceptado); //El nombre de usuario.

                                    map.put(ReferenciasURL.email, (String) authData.getProviderData().get(ReferenciasURL.email)); //El correo utilizado.

                                    map.put(ReferenciasURL.conexion, ReferenciasURL.InfoOnline); //El estado del usuario.

                                    //Esto seria para el id del usuario:   map.put(ReferenciasURL.UsuarioId, Ch)


                                    long Tiempo = new Date().getTime();
                                    map.put(ReferenciasURL.horaCreacion, String.valueOf(Tiempo));


                                    //Guardamos toda la informacion en FIREBASE.
                                    regUsuSchoolChat.child(ReferenciasURL.usuarios).child(authData.getUid()).setValue(map);

                                    Intent intent = new Intent(AltaUsuario.this, ChatActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }

                                @Override
                                public void onAuthenticationError(FirebaseError firebaseError) {

                                    Toast.makeText(AltaUsuario.this, "Ha ocurrido un error.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }


                            });

                        }


                        @Override
                        public void onError(FirebaseError firebaseError) {
                            Log.e(TAG, "Error creating user");

                        }


                    });

                }
            }


        });}


        public void onCancelar(View view){
            Intent i = new Intent(this,MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
    }
       ;


    }













    /*
       Inflacion del menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_altausuario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

 */

  /*  Accion a realizar al pulsar en REGISTRAR: Ir a la actividad MainActivity  */






