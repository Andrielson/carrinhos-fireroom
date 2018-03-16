package tk.andrielson.carrinhos.androidapp.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import tk.andrielson.carrinhos.androidapp.R;
import tk.andrielson.carrinhos.androidapp.data.model.Venda;
import tk.andrielson.carrinhos.androidapp.observable.VendaObservable;
import tk.andrielson.carrinhos.androidapp.observable.VendedorObservable;
import tk.andrielson.carrinhos.androidapp.ui.fragment.CadastroVendaFragment;
import tk.andrielson.carrinhos.androidapp.ui.fragment.ListaVendedorFragment;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;
import tk.andrielson.carrinhos.androidapp.viewmodel.CadastroVendaViewModel;

import static tk.andrielson.carrinhos.androidapp.DI.newVenda;

public class VendaActivity extends AppCompatActivity
        implements CadastroVendaFragment.OnFragmentInteractionListener,
        ListaVendedorFragment.OnListFragmentInteractionListener {

    public static final String INTENT_EXTRA_VENDA = "venda";

    private static final String TAG = VendaActivity.class.getSimpleName();
    private CadastroVendaFragment cadastroVendaFragment;
    private ListaVendedorFragment listaVendedorFragment;
    private CadastroVendaViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venda);

        //Inicializa o ViewModel
        viewModel = ViewModelProviders.of(this).get(CadastroVendaViewModel.class);

        //Carrega os dados da venda
        Venda venda;
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(INTENT_EXTRA_VENDA) && intent.getParcelableExtra(INTENT_EXTRA_VENDA) != null)
            venda = intent.getParcelableExtra(INTENT_EXTRA_VENDA);
        else
            venda = newVenda();

        cadastroVendaFragment = CadastroVendaFragment.newInstance(venda);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_holder, cadastroVendaFragment);
        ft.commit();
        if (venda.getCodigo() == null || venda.getCodigo().equals(0L))
            onClickSelecionarVendedor();
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
            actionBar.setTitle(R.string.activity_venda_title_nova_venda);
        }
        cadastroVendaFragment.setVendedorObservable(item);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.remove(listaVendedorFragment);
        ft.commit();
    }

    @Override
    public void salvarVenda(VendaObservable observable) {
        viewModel.salvarVenda(observable);
        finish();
    }

    @Override
    public void excluirVenda(VendaObservable observable) {
        viewModel.excluirVenda(observable);
        finish();
    }

    @Override
    public void finalizarVenda(VendaObservable observable) {
        Toast.makeText(this, "Um dia vai finalizar...", Toast.LENGTH_SHORT).show();
    }
}
