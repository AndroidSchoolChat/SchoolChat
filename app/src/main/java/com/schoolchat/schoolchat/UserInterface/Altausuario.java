package com.schoolchat.schoolchat.UserInterface;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.schoolchat.schoolchat.R;
import com.schoolchat.schoolchat.Firebase.conexion;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Altausuario extends AppCompatActivity {

    private EditText edEm;
    private EditText edNomUsu;
    private EditText edContras;
    private EditText edRepContras;
    private EditText edContrasVerif;


    private RadioButton rbProf;

    private Spinner spCurso;

    public String snapVerif;

    View rootView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);     //AÑADIDO DEBIDO A QUE ES NECESARIO PARA QUE FUNCIONE FIREBASE
        setContentView(R.layout.activity_altausuario);
        //para poder usar snackBar
        rootView=findViewById(R.id.root);

        edEm = (EditText) findViewById(R.id.ed_Email);
        edNomUsu = (EditText) findViewById(R.id.ed_NombreUsuario);
        edContras = (EditText) findViewById(R.id.ed_Contraseña);
        edRepContras = (EditText) findViewById(R.id.ed_RepContraseña);

        rbProf = (RadioButton) findViewById(R.id.rb_Profesor);


        spCurso = (Spinner) findViewById(R.id.sp_Curso);
        //Verificacion si es profesor:
        edContrasVerif = (EditText) findViewById(R.id.ed_ContraseñaVerif);

        //Creamos el arrayAdapter para añadir los valores del array al spinner:
        ArrayAdapter<CharSequence> adaptCurso = ArrayAdapter.createFromResource(this,
                R.array.Curso, android.R.layout.simple_spinner_item);

        //Especificar el tipo de Spinner y su layout:
        adaptCurso.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCurso.setAdapter(adaptCurso);

    }

    //Seleccion de radioButton que determinara si sera visible la lista de cursos o la contraseña de verificacion.
   public void onCurso(View view){
       boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.rb_Alumno:
                if (checked)
                spCurso.setVisibility(View.VISIBLE);
                edContrasVerif.setVisibility(View.INVISIBLE);
                    break;

            case R.id.rb_Profesor:
                if (checked)
                    edContrasVerif.setVisibility(View.VISIBLE);
                    spCurso.setVisibility(View.INVISIBLE);
                    break;
        }
    }





    /*  Accion a realizar al pulsar en REGISTRAR: Ir a la actividad MainActivity
      * y crear un nuevo usuario en la base de datos de firebase */





    public void onSign(View v) {

        //**--Codigo para el registro del PROFESOR:--*

        if (rbProf.isChecked()) {

            //variables para registro de profesor pasadas a string
            final String RegEmProf = edEm.getText().toString();
            final String RegNomUsuProf = edNomUsu.getText().toString();
            final String RegContrasProf = edContras.getText().toString();
            final String RegRepContras = edRepContras.getText().toString();

            //Comprobacion que en los campos se haya introducido datos:
            if (RegEmProf.isEmpty() || RegNomUsuProf.isEmpty() || RegContrasProf.isEmpty() || RegRepContras.isEmpty()) {
                MostrarError("Asegurese de que todos los campos esten rellenos.");

            }else {
                if (RegContrasProf.equals(RegRepContras)) {

                    //Conexion a FireBase para recuperar el valor de la contraseña de verificacion:

                    final Firebase FireBRegProf = new Firebase(conexion.FIREBASE_SCHOOLCHAT);
                    FireBRegProf.addListenerForSingleValueEvent(new ValueEventListener() {
                        public void onDataChange(DataSnapshot snapshot) {

                            snapVerif = (String) snapshot.child("VerifyPassword").child("pass").getValue();

                            //Comprobacion en el IF de que la contraseña de verificacion introducida es correcta.

                            if (snapVerif.equals(edContrasVerif.getText().toString())) {

                                //En caso de ser correcto creara el usuario.
                                final Firebase registroProfesor = new Firebase(conexion.FIREBASE_SCHOOLCHAT);

                                final String addEmProf = RegEmProf;
                                final String addContrasProf = RegContrasProf;
                                final String addNomUsuProf = RegNomUsuProf;

                                //crear el usuario
                                registroProfesor.createUser(RegEmProf, RegContrasProf, new Firebase.ValueResultHandler<Map<String, Object>>() {


                                    @Override
                                    public void onSuccess(Map<String, Object> stringObjectMap) {

                                        Snackbar.make(rootView, "Se ha registrado un nuevo profesor.", Snackbar.LENGTH_LONG).show();

                                        registroProfesor.authWithPassword(addEmProf, addContrasProf, new Firebase.AuthResultHandler() {

                                            @Override
                                            public void onAuthenticated(AuthData authData) {
                                                Map<String, Object> mapProf = new HashMap<String, Object>();
                                                mapProf.put(conexion.INFO_NOMBRE, addNomUsuProf);
                                                mapProf.put(conexion.INFO_EMAIL, addEmProf);
                                                mapProf.put(conexion.RAMA_CONEXION, conexion.ESTADO_OFFLINE);

                                                long fechaCreacionProf = new Date().getTime();
                                                mapProf.put(conexion.INFO_FECHA, String.valueOf(fechaCreacionProf));


                                                registroProfesor.child(conexion.RAMA_PROFESORES).child(authData.getUid()).setValue(mapProf); //Añade los datos a Firebase en la pestaña de PROFESOR

                                                Intent inReturnLogin = new Intent(Altausuario.this, LogInActivity.class);
                                                inReturnLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                inReturnLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(inReturnLogin);

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
                            } else {

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

                }else{
                    MostrarError("Los campos de contraseña de usuario no coinciden.");
                }

            }

        } else {

            //**--Codigo para el registro del ALUMNO:--*

            //variables para registro pasadas a string
            String RegEmUsu = edEm.getText().toString();
            String RegNomUsu = edNomUsu.getText().toString();
            String RegContrasUsu = edContras.getText().toString();
            String RegRepContrasUsu = edRepContras.getText().toString();

            if (RegEmUsu.isEmpty() || RegNomUsu.isEmpty() || RegContrasUsu.isEmpty() || RegRepContrasUsu.isEmpty()) {
                MostrarError("Asegurese de que todos los campos esten rellenos.");

            }else{
                if(RegContrasUsu.equals(RegRepContrasUsu)){


                    final Firebase FireBRegUsu = new Firebase(conexion.FIREBASE_SCHOOLCHAT);
                    final String addEmUsu = RegEmUsu;
                    final String addContrasUsu = RegContrasUsu;
                    final String addNomUsu = RegNomUsu;

                    //Obtiene la opcion elegida del spinner:
                    final String spCursoTexto= spCurso.getSelectedItem().toString();

                    //crear el usuario
                    FireBRegUsu.createUser(RegEmUsu, RegContrasUsu, new Firebase.ValueResultHandler<Map<String, Object>>() {


                        @Override
                        public void onSuccess(Map<String, Object> stringObjectMap) {

                            Snackbar.make(rootView, "Se ha registrado un nuevo alumno.", Snackbar.LENGTH_LONG).show();

                            FireBRegUsu.authWithPassword(addEmUsu, addContrasUsu, new Firebase.AuthResultHandler() {

                                @Override
                                public void onAuthenticated(AuthData authData) {
                                    Map<String, Object> mapAlum = new HashMap<String, Object>();
                                    mapAlum.put(conexion.INFO_NOMBRE, addNomUsu);
                                    mapAlum.put(conexion.INFO_EMAIL, addEmUsu);
                                    mapAlum.put(conexion.RAMA_CONEXION, conexion.ESTADO_OFFLINE);
                                    mapAlum.put(conexion.INFO_CURSO,spCursoTexto);

                                    long fechaCreacionAlum = new Date().getTime();
                                    mapAlum.put(conexion.INFO_FECHA, String.valueOf(fechaCreacionAlum));


                                    FireBRegUsu.child(conexion.RAMA_ALUMNOS).child(authData.getUid()).setValue(mapAlum); //Añade datos en la pestaña de usuarios.

                                    Intent inReturnLogin = new Intent(Altausuario.this, LogInActivity.class);
                                    inReturnLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    inReturnLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(inReturnLogin);
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

                }else {
                    MostrarError("Los campos de contraseña de usuario no coinciden.");
                }
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
        Intent inCancReg = new Intent(this, LogInActivity.class);
        inCancReg.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        inCancReg.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(inCancReg);
    }


}

