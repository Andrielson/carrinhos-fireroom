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
import android.widget.Toast;

import tk.andrielson.carrinhos.androidapp.R;
import tk.andrielson.carrinhos.androidapp.fireroom.SincronizadorFirestoreRoom;
import tk.andrielson.carrinhos.androidapp.observable.ProdutoObservable;
import tk.andrielson.carrinhos.androidapp.observable.VendaObservable;
import tk.andrielson.carrinhos.androidapp.observable.VendedorObservable;
import tk.andrielson.carrinhos.androidapp.ui.fragment.ListaProdutoFragment;
import tk.andrielson.carrinhos.androidapp.ui.fragment.ListaVendaFragment;
import tk.andrielson.carrinhos.androidapp.ui.fragment.ListaVendedorFragment;
import tk.andrielson.carrinhos.androidapp.ui.fragment.RelatorioVendasPorDiaFragment;
import tk.andrielson.carrinhos.androidapp.ui.fragment.RelatoriosFragment;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ListaProdutoFragment.OnListFragmentInteractionListener,
        ListaVendedorFragment.OnListFragmentInteractionListener,
        ListaVendaFragment.OnListFragmentInteractionListener,
        RelatoriosFragment.OnFragmentInteractionListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String STATE_FRAGMENTOS = "FRAGMENTOS";
    private Fragmentos fragmentoAtivo;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent;
            switch (fragmentoAtivo) {
                case VENDA:
                    intent = new Intent(MainActivity.this, VendaActivity.class);
                    startActivity(intent);
                    break;
                case PRODUTO:
                    intent = new Intent(MainActivity.this, ProdutoActivity.class);
                    startActivity(intent);
                    break;
                case VENDEDOR:
                    intent = new Intent(MainActivity.this, VendedorActivity.class);
                    startActivity(intent);
                    break;
                default:
                    Snackbar.make(view, fragmentoAtivo.toString(), Snackbar.LENGTH_LONG).show();
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState != null && savedInstanceState.getSerializable(STATE_FRAGMENTOS) != null)
            carregaFragment((Fragmentos) savedInstanceState.getSerializable(STATE_FRAGMENTOS));
//        else
//            carregaFragment(Fragmentos.PRODUTO);

        LogUtil.Log(TAG, "onCreate", Log.VERBOSE);
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
        if (savedInstanceState != null && savedInstanceState.getSerializable(STATE_FRAGMENTOS) != null)
            fragmentoAtivo = (Fragmentos) savedInstanceState.getSerializable(STATE_FRAGMENTOS);
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_inicio:
                fragmentoAtivo = Fragmentos.INICIO;
                break;
            case R.id.nav_vendas:
                carregaFragment(Fragmentos.VENDA);
                break;
            case R.id.nav_produtos:
                carregaFragment(Fragmentos.PRODUTO);
                break;
            case R.id.nav_vendedores:
                carregaFragment(Fragmentos.VENDEDOR);
                break;
            case R.id.nav_relatorios:
                carregaFragment(Fragmentos.RELATORIOS);
                break;
            case R.id.nav_atualizar:
                break;
            case R.id.nav_backup:
                SincronizadorFirestoreRoom repositorio = SincronizadorFirestoreRoom.getInstancia();
                break;
            case R.id.nav_sobre:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClickProduto(ProdutoObservable item) {
        Intent intent = new Intent(this, ProdutoActivity.class);
        intent.putExtra(ProdutoActivity.INTENT_EXTRA_PRODUTO, item.getProdutoModel());
        startActivity(intent);
    }

    @Override
    public void onClickVendedor(VendedorObservable item) {
        Intent intent = new Intent(this, VendedorActivity.class);
        intent.putExtra(VendedorActivity.INTENT_EXTRA_VENDEDOR, item.getVendedorModel());
        startActivity(intent);
    }

    @Override
    public void onClickVenda(VendaObservable item) {
        Intent intent = new Intent(this, VendaActivity.class);
        intent.putExtra(VendaActivity.INTENT_EXTRA_VENDA, item.getVendaModel());
        startActivity(intent);
    }

    @Override
    public void onClickVendas() {
        carregaFragment(Fragmentos.RELATORIO_VENDAS_POR_DIA);
    }

    @Override
    public void onClickProdutos() {
        Toast.makeText(this, "Calma...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickVendedores() {
        Toast.makeText(this, "Calma...", Toast.LENGTH_SHORT).show();
    }

    private void carregaFragment(Fragmentos frag) {
        Fragment fragment = null;
        fragmentoAtivo = frag;
        switch (frag) {
            case VENDA:
                fragment = ListaVendaFragment.newInstance();
                toolbar.setTitle(R.string.activity_venda_label);
                break;
            case PRODUTO:
                fragment = ListaProdutoFragment.newInstance();
                toolbar.setTitle(R.string.activity_produto_label);
                break;
            case VENDEDOR:
                fragment = ListaVendedorFragment.newInstance();
                toolbar.setTitle(R.string.activity_vendedor_label);
                break;
            case RELATORIOS:
                fragment = new RelatoriosFragment();
                toolbar.setTitle("Relatórios");
                break;
            case RELATORIO_VENDAS_POR_DIA:
                fragment = new RelatorioVendasPorDiaFragment();
                toolbar.setTitle("Relatório de Vendas");
                break;
            default:
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }

    private enum Fragmentos {
        INICIO,
        PRODUTO,
        VENDEDOR,
        VENDA,
        RELATORIOS,
        RELATORIO_VENDAS_POR_DIA
    }
}
