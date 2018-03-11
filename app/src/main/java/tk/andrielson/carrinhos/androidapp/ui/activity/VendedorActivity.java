package tk.andrielson.carrinhos.androidapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import tk.andrielson.carrinhos.androidapp.DI;
import tk.andrielson.carrinhos.androidapp.R;
import tk.andrielson.carrinhos.androidapp.data.dao.VendedorDao;
import tk.andrielson.carrinhos.androidapp.data.model.Vendedor;
import tk.andrielson.carrinhos.androidapp.ui.fragment.CadastroVendedorFragment;

public class VendedorActivity extends AppCompatActivity implements CadastroVendedorFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendedor);
        Intent intent = getIntent();
        Long codigoVendedor = (intent != null && intent.hasExtra("vendedorCodigo")) ? intent.getLongExtra("vendedorCodigo", 0) : null;
        Fragment fragment = CadastroVendedorFragment.newInstance(codigoVendedor);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_holder, fragment);
        ft.commit();
    }

    @Override
    public void salvarVendedor(Vendedor vendedor, boolean insercao) {
        VendedorDao dao = DI.newVendedorDao();
        if (insercao)
            dao.insert(vendedor);
        else
            dao.update(vendedor);
        Toast.makeText(this, "Vendedor " + (insercao ? "adicionado!" : "atualizado!"), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void excluirVendedor(Vendedor vendedor) {
        if (vendedor.getCodigo() != null && vendedor.getCodigo() != 0) {
            VendedorDao dao = DI.newVendedorDao();
            dao.delete(vendedor);
            Toast.makeText(this, "Vendedor excluído!", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
