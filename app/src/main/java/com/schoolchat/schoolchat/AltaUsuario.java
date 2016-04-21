package com.schoolchat.schoolchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Gabri on 20/04/2016.
 */


    /*  Actividad nueva para la interfaz de los datos que se pediran al registrarse.  */

public class AltaUsuario extends AppCompatActivity {

    /*  Declaracion de variables:  */

    Button botonRegistrarse;
    Button botonCancelar;

    EditText ed_Em;
    EditText ed_NomUsu;
    EditText ed_Cont;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altausuario);

        /*  Referenciando a los widgets  */


        botonRegistrarse = (Button) findViewById(R.id.bt_Registrar);
        botonCancelar = (Button) findViewById(R.id.bt_Cancelar);

        ed_Em = (EditText) findViewById(R.id.ed_Email);
        ed_NomUsu = (EditText) findViewById(R.id.ed_NombreUsuario);
        ed_Cont = (EditText) findViewById(R.id.ed_Contraseña);


    }



     /*  Inflacion del menu  */

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



  /*  Accion a realizar al pulsar en REGISTRAR: Ir a la actividad MainActivity  */


    public void onCancelar(View v){
        Intent j = new Intent(this,MainActivity.class);
        startActivity(j);
    }


}
