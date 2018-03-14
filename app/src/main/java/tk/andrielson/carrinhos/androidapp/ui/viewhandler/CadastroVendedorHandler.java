package tk.andrielson.carrinhos.androidapp.ui.viewhandler;

import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import tk.andrielson.carrinhos.androidapp.data.model.Vendedor;
import tk.andrielson.carrinhos.androidapp.databinding.FragmentCadastroVendedorBinding;
import tk.andrielson.carrinhos.androidapp.observable.VendedorObservable;
import tk.andrielson.carrinhos.androidapp.ui.fragment.CadastroVendedorFragment;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;

/**
 * Created by anfesilva on 09/03/2018.
 */

public class CadastroVendedorHandler {
    private static final String TAG = CadastroVendedorHandler.class.getSimpleName();
    private final CadastroVendedorFragment.OnFragmentInteractionListener listener;
    private final FragmentCadastroVendedorBinding binding;

    public CadastroVendedorHandler(FragmentCadastroVendedorBinding binding, CadastroVendedorFragment.OnFragmentInteractionListener listener) {
        this.listener = listener;
        this.binding = binding;
    }

    public void onBotaoExcluirClick(View view, final VendedorObservable observable) {
        Vendedor vendedor = observable.getVendedorModel();
        Snackbar confirmacao = Snackbar.make(binding.coordinatorLayout, "Tem certeza de que quer excluir esse vendedor?", Snackbar.LENGTH_LONG);
        confirmacao.setAction("SIM", v -> listener.excluirVendedor(vendedor));
        confirmacao.show();
    }

    public void onBotaoSalvarClick(View view, VendedorObservable observable) {
        Vendedor vendedor = observable.getVendedorModel();
        if (vendedor != null && ehNomeValido(vendedor.getNome(), vendedor) && ehComissaoValida(vendedor.getComissao()))
            listener.salvarVendedor(vendedor, vendedor.getCodigo() == null || vendedor.getCodigo().equals(0L));
        else {
            Toast.makeText(view.getContext(), "Por favor, corrija as informações incorretas!", Toast.LENGTH_SHORT).show();
            LogUtil.Log(TAG, "Vendedor nulo ou inválido!", Log.ERROR);
        }
    }

    private boolean ehNomeValido(String nome, Vendedor vendedor) {
        if (nome == null || nome.isEmpty()) {
            binding.inputNome.setError("O nome do vendedor é obrigatório!");
            return false;
        }
        vendedor.setNome(nome.trim().toUpperCase());
        return true;
    }

    private boolean ehComissaoValida(Integer comissao) {
        if (comissao == null || comissao == 0) {
            binding.inputComissao.setError("A comissão é obrigatória!");
            return false;
        }
        return true;
    }
}
