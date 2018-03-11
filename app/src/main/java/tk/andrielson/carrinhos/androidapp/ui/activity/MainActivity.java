package tk.andrielson.carrinhos.androidapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.Serializable;

import tk.andrielson.carrinhos.androidapp.R;
import tk.andrielson.carrinhos.androidapp.data.model.Produto;
import tk.andrielson.carrinhos.androidapp.ui.fragment.ListaProdutoFragment;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ListaProdutoFragment.OnListFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String STATE_FRAGMENTOS = "FRAGMENTOS";
    private Fragmentos fragmentoAtivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragmentoAtivo == Fragmentos.PRODUTO) {
                    Intent intent = new Intent(MainActivity.this, ProdutoActivity.class);
                    startActivity(intent);
                }
                Snackbar.make(view, fragmentoAtivo.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        LogUtil.Log(TAG, "onCreate", Log.VERBOSE);
        fragmentoAtivo = Fragmentos.INICIO;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(STATE_FRAGMENTOS, fragmentoAtivo);
        LogUtil.Log(TAG, "onSaveInstanceState", Log.VERBOSE);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LogUtil.Log(TAG, "onRestoreInstanceState", Log.DEBUG);
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.Log(TAG, "onStart", Log.VERBOSE);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.Log(TAG, "onResume", Log.VERBOSE);
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.Log(TAG, "onPause", Log.VERBOSE);
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.Log(TAG, "onStop", Log.VERBOSE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.Log(TAG, "onDestroy", Log.VERBOSE);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_inicio) {
            // Handle the camera action
        } else if (id == R.id.nav_vendas) {

        } else if (id == R.id.nav_produtos) {
            Fragment fragment = ListaProdutoFragment.newInstance();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            fragmentoAtivo = Fragmentos.PRODUTO;
            ft.commit();
        } else if (id == R.id.nav_vendedores) {

        } else if (id == R.id.nav_atualizar) {

        } else if (id == R.id.nav_backup) {

        } else if (id == R.id.nav_sobre) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void carregaFragment(int id) {
        Fragment fragment = new Fragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }

    @Override
    public void onListFragmentInteraction(Produto item) {
        Intent intent = new Intent(MainActivity.this, ProdutoActivity.class);
        intent.putExtra("produtoCodigo", item.getCodigo());
        startActivity(intent);
    }

    private enum Fragmentos {
        INICIO,
        PRODUTO,
        VENDEDOR,
        VENDA
    }
}
