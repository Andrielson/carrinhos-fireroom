package tk.andrielson.carrinhos.androidapp.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import tk.andrielson.carrinhos.androidapp.R;
import tk.andrielson.carrinhos.androidapp.observable.VendedorObservable;
import tk.andrielson.carrinhos.androidapp.ui.fragment.CadastroVendaFragment;
import tk.andrielson.carrinhos.androidapp.ui.fragment.ListaVendedorFragment;

public class VendaActivity extends AppCompatActivity
        implements CadastroVendaFragment.OnFragmentInteractionListener,
        ListaVendedorFragment.OnListFragmentInteractionListener {

    private CadastroVendaFragment cadastroVendaFragment;
    private ListaVendedorFragment listaVendedorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venda);
        cadastroVendaFragment = CadastroVendaFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_holder, cadastroVendaFragment);
        ft.commit();
    }

    /*TODO: verificar qual fragment está sendo exibido e manipular a ação do botão voltar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                break;
        }
        return true;
    }*/

    @Override
    public void onClickSelecionarVendedor() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle("Selecione um vendedor");
        }
        listaVendedorFragment = ListaVendedorFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_holder, listaVendedorFragment, "listaVendedorFragment");
        ft.addToBackStack("listaVendedorFragment");
        ft.commit();
    }

    @Override
    public void onClickVendedor(VendedorObservable item) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.activity_venda_label);
        }
        cadastroVendaFragment.setVendedorObservable(item);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(listaVendedorFragment);
        ft.commit();
    }
}
