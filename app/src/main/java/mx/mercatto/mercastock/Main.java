package mx.mercatto.mercastock;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.FragmentManager;
import android.util.Log;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.json.JSONObject;


import mx.mercatto.mercastock.BGT.BGTAPI;

public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String PROJECT_NUMBER="917548048883";
    public static int controlUsuario =-1;
    public static int idSesion=0;
    public static int inicio=0;
    public static int bandera=0;
    public static String idRegistro;
    public static boolean FLAG_ON_DESTROY=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        controlUsuario=settings.getInt("controlusuario",-1);
            if (controlUsuario == -1) {
                setContentView(R.layout.activity_main);
            } else if (controlUsuario == 1)
                setContentView(R.layout.activity_main_logged);
            else
                setContentView(R.layout.activity_main_logged_user);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            GCMClientManager pushClientManager = new GCMClientManager(this, PROJECT_NUMBER);
            pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {

                @Override
                public void onSuccess(String registrationId, boolean isNewRegistration) {
                    Log.d("Registration id", registrationId);
                    idRegistro=registrationId;
                }

                @Override
                public void onFailure(String ex) {
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("usuario", "");
                    editor.putString("ClaveApi", "");
                    editor.putString("ip", "default");
                    editor.apply();
                    super.onFailure(ex);
                }
            });

            final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

                public void onDrawerStateChanged(int newState) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
                }
            };
            drawer.setDrawerListener(toggle);
            toggle.syncState();
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        Configuracion.settings=PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        if (Configuracion.settings.getString("ip", "").equals("")) {
            Configuracion.Inicializar(this);
        }
            revisarApi();



    }
    public  void showToast(String msg) {
        Context context = this.getApplicationContext();
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
    public  void revisarApi() {
        BGTAPI bgt;
        try {

            JSONObject jsonObj1 = new JSONObject();
            jsonObj1.put("claveApi",Configuracion.settings.getString("ClaveApi",""));
            bgt = new BGTAPI(Configuracion.getApiUrlRevisarApi(), this,jsonObj1,false );

            bgt.execute();
        } catch (Exception e){
            this.showToast(e.getMessage());
        }
    }


    @Override
    public void onBackPressed() {

        Fragment currentFragment = this.getFragmentManager().findFragmentById(R.id.content_main);

        if (currentFragment instanceof FragmentLogin) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
        if (currentFragment instanceof FragmentCategoria) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
        if (currentFragment instanceof FragmentArticulo) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                getFragmentManager().popBackStack();
            }
        }
        if (currentFragment instanceof FragmentFormularioArticulo) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                getFragmentManager().popBackStack();
            }
        }
        if (currentFragment instanceof FragmentPassword) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                FragmentCategoria fragment2 = new FragmentCategoria();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, fragment2);
                fragmentTransaction.commit();
            }
        }
        if (currentFragment instanceof FragmentSucursal) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                if(FragmentConexionPerdida.conexionPerdida&&!Configuracion.settings.getString("ip","").equals("")){
                    FragmentConexionPerdida fragment2 = new FragmentConexionPerdida();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_main, fragment2);
                    fragmentTransaction.commit();
                }
                else if(!FragmentConexionPerdida.conexionPerdida||!Configuracion.settings.getString("ip","").equals("")){
                FragmentLogin fragment2 = new FragmentLogin();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, fragment2);
                fragmentTransaction.commit();
                }
                else{
                    FragmentConexionPerdida fragment2 = new FragmentConexionPerdida();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_main, fragment2);
                    fragmentTransaction.commit();
                }
            }
        }
        if (currentFragment instanceof RegistroUsuario) {
            /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                getFragmentManager().popBackStack();
            }*/
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                FragmentCategoria fragment2 = new FragmentCategoria();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, fragment2);
                fragmentTransaction.commit();
            }
        }
        if (currentFragment instanceof FragmentSesion) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //final MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.activity_main_drawer, menu);
        return false;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.crearusuario) {
            RegistroUsuario fragment = new RegistroUsuario();
            FragmentManager fragmentManager = this.getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();

        }else if(id==R.id.cerrarsesion){
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("controlusuario",-1);
            editor.putString("usuario", "");
            editor.putString("ClaveApi", "");
            editor.apply();
            idSesion=0;
            controlUsuario =-1;
            inicio=0;

            finish();

            Intent intent = getIntent();
            startActivity(intent);
            FragmentLogin fragment = new FragmentLogin();
            FragmentManager fragmentManager = this.getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
            bandera=0;
        }else if(id==R.id.seleccionarsucursal){
            FragmentSucursal fragment = new FragmentSucursal();
            FragmentManager fragmentManager = this.getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
        }else if(id==R.id.cambiarcontrasena){
            FragmentPassword fragment = new FragmentPassword();
            FragmentManager fragmentManager = this.getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).addToBackStack(null).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDestroy() {



        super.onDestroy();
    }


    @Override
    public  void onResume(){
        Log.d("resume","resume");
/*
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        if(!settings.getBoolean("FLAG_DESTROY",false)){
            Log.d("destroy", "destroy");
            SharedPreferences.Editor editor = settings.edit();

            editor.putBoolean("FLAG_DESTROY", false);
            editor.apply();
        }else{
            SharedPreferences.Editor editor = settings.edit();
            Log.d("login","login");
            editor.putString("login", "false");
            editor.apply();
        }*/
        super.onResume();
    }
    @Override
    public  void onRestart(){
        Log.d("restart","restart");
        if(bandera==0&&idSesion==1){
        bandera=1;

        }
       else if(idSesion==1 && bandera==1) {
            revisarApi();
        }
        super.onRestart();
    }


}
