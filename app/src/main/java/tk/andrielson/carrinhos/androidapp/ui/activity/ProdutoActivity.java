package tk.andrielson.carrinhos.androidapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import tk.andrielson.carrinhos.androidapp.DI;
import tk.andrielson.carrinhos.androidapp.R;
import tk.andrielson.carrinhos.androidapp.data.dao.ProdutoDao;
import tk.andrielson.carrinhos.androidapp.data.model.Produto;
import tk.andrielson.carrinhos.androidapp.ui.fragment.CadastroProdutoFragment;

public class ProdutoActivity extends AppCompatActivity implements CadastroProdutoFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto);
        Fragment fragment;
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("produtoCodigo")) {
            fragment = CadastroProdutoFragment.newInstance(intent.getLongExtra("produtoCodigo", 0));
        } else {
            fragment = CadastroProdutoFragment.newInstance(null);
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_holder, fragment);
        ft.commit();
    }

    @Override
    public void onFragmentInteraction(Produto produto, boolean insercao) {
        ProdutoDao dao = DI.newProdutoDao();
        if (insercao)
            dao.insert(produto);
        else
            dao.update(produto);
        Toast.makeText(this, "Produto " + (insercao ? "adicionado!" : "atualizado!"), Toast.LENGTH_SHORT).show();
        finish();
    }
}
