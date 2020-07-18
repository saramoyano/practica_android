package com.dam.t07p03.vista;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import com.dam.t07p03.R;
import com.dam.t07p03.modelo.AppDatabase;
import com.dam.t07p03.modelo.Departamento;
import com.dam.t07p03.vista.dialogos.DlgConfirmacion;
import com.dam.t07p03.vista.fragmentos.LoginFragment;
import com.dam.t07p03.vista.fragmentos.MainFragment;
import com.dam.t07p03.vistamodelo.MainViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity
        implements MainFragment.MainFragInterface,
        LoginFragment.LoginFragInterface,
        DlgConfirmacion.DlgConfirmacionListener {

    private DrawerLayout mDrawer;
    private NavController mNavC;

    private MainViewModel mMainVM;
    private Departamento mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Inits Navigation Drawer
        mDrawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(navView_OnNavigationItemSelected);

        // FindViewByIds

        // Inits
        mNavC = Navigation.findNavController(this, R.id.navhostfrag_main);
        mMainVM = ViewModelProviders.of(this).get(MainViewModel.class);
        mLogin = mMainVM.getLogin();    // Recuperamos el login del ViewModel

        if (mLogin == null && savedInstanceState == null) {
            // Esta condición de savedInstanceState==null evita que ante un "giro" se ejecute estas inicializaciones de nuevo.
            // En concreto, el navigate fallaría ya que no hace falta, pues se relanza del fragmento de login solo tras un "giro"!!

            // Establecemos preferencias
            PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

            // Splash
            boolean mostrarSplash = pref.getBoolean(getResources().getString(R.string.splashApp_key), false);
            if (mostrarSplash) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            // Lanzamos el LoginFragment
            boolean mostrarLogin = pref.getBoolean(getString(R.string.login_key), true);
            if (mostrarLogin) {
                mNavC.navigate(R.id.action_mainFragment_to_loginFragment);
            }
        }

        // Listeners

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.menuSalir:
                mostrarDlgSalir();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void mostrarDlgSalir() {
        // Lanzamos DlgConfirmacion
        Bundle bundle = new Bundle();
        bundle.putInt("titulo", R.string.app_name);
        bundle.putInt("mensaje", R.string.msg_DlgConfirmacion_Salir);
        mNavC.navigate(R.id.action_global_dlgConfirmacionMain, bundle);
    }

    @Override
    public void onDlgConfirmacionPositiveClick(DialogFragment dialog) {
        finish();
    }

    @Override
    public void onDlgConfirmacionNegativeClick(DialogFragment dialog) {
        ;
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            if (mNavC.getCurrentDestination() != null && mNavC.getCurrentDestination().getId() == R.id.mainFragment) {
                mostrarDlgSalir();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!AppDatabase.cerrarAppDatabase()) {
            //Snackbar.make(findViewById(android.R.id.content), R.string.msg_ErrorBaseDatos, Snackbar.LENGTH_SHORT).show();
        }
    }

    private NavigationView.OnNavigationItemSelectedListener navView_OnNavigationItemSelected = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menuDptos:
                    if (mLogin != null) {
                        if (mLogin.getId() == 0) {    // admin
                            if (mNavC.getCurrentDestination() != null && mNavC.getCurrentDestination().getId() == R.id.mainFragment) {
                                Intent i = new Intent(MainActivity.this, DptosActivity.class);
                                i.putExtra("login", mLogin);
                                startActivity(i);
                            }
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), R.string.msg_LoginPermisos, Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), R.string.msg_NoLogin, Snackbar.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.menuIncs:

                    if (mLogin != null) {
                        Intent i = new Intent(MainActivity.this, IncsActivity.class);
                        i.putExtra("login", mLogin);
                        startActivity(i);
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), R.string.msg_NoLogin, Snackbar.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.menuLogin:
                    if (mNavC.getCurrentDestination() != null && mNavC.getCurrentDestination().getId() == R.id.mainFragment) {
                        // Lanzamos LoginFragment
                        mNavC.navigate(R.id.action_mainFragment_to_loginFragment);
                    }
                    break;
                case R.id.menuPreferencias:
                    if (mNavC.getCurrentDestination() != null && mNavC.getCurrentDestination().getId() == R.id.mainFragment) {
                        // Lanzamos PrefFragment
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("login", mLogin);
                        mNavC.navigate(R.id.action_mainFragment_to_prefFragment, bundle);
                    }
                    break;
            }
            mDrawer.closeDrawer(GravityCompat.START);
            return true;
        }
    };

    @Override
    public void onClickBottomNavMainFrag(int menuItem) {
        switch (menuItem) {
            case R.id.menuDptos:
                if (mLogin != null) {
                    if (mLogin.getId() == 0) {    // admin
                        if (mNavC.getCurrentDestination() != null && mNavC.getCurrentDestination().getId() == R.id.mainFragment) {
                            Intent i = new Intent(MainActivity.this, DptosActivity.class);
                            i.putExtra("login", mLogin);
                            startActivity(i);
                        }
                    } else {
                        Snackbar.make(findViewById(android.R.id.content), R.string.msg_LoginPermisos, Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(findViewById(android.R.id.content), R.string.msg_NoLogin, Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.menuIncs:

                if (mLogin != null) {
                    Intent i = new Intent(MainActivity.this, IncsActivity.class);
                    i.putExtra("login", mLogin);
                    startActivity(i);
                } else {
                    Snackbar.make(findViewById(android.R.id.content), R.string.msg_NoLogin, Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onCancelarLoginFrag() {
        // Cerramos LoginFragment
        mNavC.navigateUp();

        mMainVM.setLogin(mLogin);   // Guardamos el login en el ViewModel
    }

    @Override
    public void onAceptarLoginFrag(Departamento dpto) {
        // Cerramos LoginFragment
        mNavC.navigateUp();

        mLogin = dpto;
        mMainVM.setLogin(mLogin);   // Guardamos el login en el ViewModel
        Snackbar.make(findViewById(android.R.id.content), R.string.msg_LoginOK, Snackbar.LENGTH_SHORT).show();
    }

}
