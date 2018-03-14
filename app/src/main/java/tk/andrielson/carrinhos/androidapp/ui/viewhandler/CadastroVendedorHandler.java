package tk.andrielson.carrinhos.androidapp.ui.viewhandler;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
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

    public CadastroVendedorHandler(@NonNull final FragmentCadastroVendedorBinding binding, @NonNull final CadastroVendedorFragment.OnFragmentInteractionListener listener) {
        this.listener = listener;
        this.binding = binding;
    }

    public void onBotaoExcluirClick(@NonNull final VendedorObservable observable) {
        Snackbar confirmacao = Snackbar.make(binding.coordinatorLayout, "Tem certeza de que deseja excluir esse vendedor?", Snackbar.LENGTH_LONG);
        confirmacao.setAction("SIM", v -> listener.excluirVendedor(observable));
        confirmacao.show();
    }

    public void onBotaoSalvarClick(@NonNull final VendedorObservable observable) {
        if (ehNomeValido(observable) && ehComissaoValida(observable.comissao.get()))
            listener.salvarVendedor(observable);
        else {
            Toast.makeText(binding.getRoot().getContext(), "Por favor, corrija as informações incorretas!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean ehNomeValido(@NonNull VendedorObservable observable) {
        if (observable.nome.get() == null || observable.nome.get().isEmpty()) {
            binding.inputNome.setError("O nome do vendedor é obrigatório!");
            return false;
        }
        observable.nome.set(observable.nome.get().trim().toUpperCase());
        return true;
    }

    private boolean ehComissaoValida(String comissao) {
        if (comissao == null || comissao.isEmpty()) {
            binding.inputComissao.setError("A comissão é obrigatória!");
            return false;
        }
        return true;
    }
}
