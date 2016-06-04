package com.schoolchat.schoolchat.UserInterface;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
//clase para la vista de los usuarios / grupos de la aplicacion

    protected Firebase starFirebase;
    protected RecyclerView listaRecyclerView;
    protected AdaptadorUsuarios AdaptadorUsuarios;
    private Firebase.AuthStateListener autentificar;
    private AuthData miAuthData;
    private String actualUsuarioUid;
    private String actualUsuarioEmail;
    private Firebase ramaUsuarios;
    private Firebase ramaProfesores;
    private Firebase ramaGrupo;
    private ChildEventListener listaUsuarios;
    private ChildEventListener listaProfesores;
    private ChildEventListener listaGrupos;
    private ArrayList<String> miListaClaveUsuarios;
    private Firebase EstadoConexion;
    private Firebase estadoConexionProfe, estadoConexionUser;
    private ValueEventListener cambioconexion;
    private View rootView;
    public boolean profesor= false;
    NavigationView navigationView = null;
    Toolbar toolbar = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //menu

        alumnoFragment fragment = new alumnoFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Con onPrepareOptionsMenu se elige el archivo xml que se va a usar dependiendo de la condición
    public boolean onPrepareOptionsMenu(Menu menu){
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        if (profesor==true){
            inflater.inflate(R.menu.menu_profesor, menu);
        }else{
            inflater.inflate(R.menu.menu_alumno, menu);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.salir:
                logout();
                break;
            case R.id.difusion:
                difusion();
                break;
            case R.id.grupo:
                crearGrupo();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.main) {
            alumnoFragment fragment = new alumnoFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }else if (id==R.id.profesor){
            profesorFragment fragment= new profesorFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //metodo para acabar la sesion
    private void logout(){

    }
    //metodo para lanzar la actividad difusion
    protected void difusion(){
        Intent i=new Intent(MainActivity.this,Difusion.class);
        startActivity(i);
    }
    protected void crearGrupo(){
        Intent i=new Intent(MainActivity.this,Grupo.class);
        startActivity(i);
    }
}
