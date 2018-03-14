package tk.andrielson.carrinhos.androidapp.ui.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import tk.andrielson.carrinhos.androidapp.R;
import tk.andrielson.carrinhos.androidapp.data.model.Vendedor;
import tk.andrielson.carrinhos.androidapp.observable.VendedorObservable;
import tk.andrielson.carrinhos.androidapp.ui.fragment.CadastroVendedorFragment;
import tk.andrielson.carrinhos.androidapp.viewmodel.CadastroVendedorViewModel;

import static tk.andrielson.carrinhos.androidapp.DI.newVendedor;

public class VendedorActivity extends AppCompatActivity implements CadastroVendedorFragment.OnFragmentInteractionListener {

    private CadastroVendedorViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendedor);
        // Inicializa o ViewModel
        viewModel = ViewModelProviders.of(this).get(CadastroVendedorViewModel.class);

        //Carrega os dados de vendedor
        Vendedor vendedor;
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("vendedor") && intent.getParcelableExtra("vendedor") != null)
            vendedor = intent.getParcelableExtra("vendedor");
        else
            vendedor = newVendedor();

        // Cria o fragment e passa o vendedor
        Fragment fragment = CadastroVendedorFragment.newInstance(vendedor);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_holder, fragment);
        ft.commit();
    }

    @Override
    public void salvarVendedor(VendedorObservable vendedor) {
        viewModel.salvarVendedor(vendedor);
        finish();
    }

    @Override
    public void excluirVendedor(VendedorObservable vendedor) {
        viewModel.excluirVendedor(vendedor);
        finish();
    }
}
