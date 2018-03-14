package tk.andrielson.carrinhos.androidapp.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import tk.andrielson.carrinhos.androidapp.R;
import tk.andrielson.carrinhos.androidapp.data.model.Produto;
import tk.andrielson.carrinhos.androidapp.observable.ProdutoObservable;
import tk.andrielson.carrinhos.androidapp.ui.fragment.CadastroProdutoFragment;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;
import tk.andrielson.carrinhos.androidapp.viewmodel.CadastroProdutoViewModel;

import static tk.andrielson.carrinhos.androidapp.DI.newProduto;

public class ProdutoActivity extends AppCompatActivity implements CadastroProdutoFragment.OnFragmentInteractionListener {

    private static final String TAG = ProdutoActivity.class.getSimpleName();
    private CadastroProdutoViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto);
        // Inicializa o ViewModel
        viewModel = ViewModelProviders.of(this).get(CadastroProdutoViewModel.class);

        //Carrega os dados de produto
        Produto produto;
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("produto") && intent.getParcelableExtra("produto") != null) {
            produto = intent.getParcelableExtra("produto");
            LogUtil.Log(TAG, produto.getNome(), Log.DEBUG);
        } else {
            produto = newProduto();
            LogUtil.Log(TAG, "PRODUTO NOVO", Log.DEBUG);
        }

        // Cria o fragment e passa o produto
        Fragment fragment = CadastroProdutoFragment.newInstance(produto);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_holder, fragment);
        ft.commit();
    }

    @Override
    public void salvarProduto(ProdutoObservable produto) {
        viewModel.salvarProduto(produto);
        finish();
    }

    @Override
    public void excluirProduto(ProdutoObservable produto) {
        viewModel.excluirProduto(produto);
        finish();
    }
}
