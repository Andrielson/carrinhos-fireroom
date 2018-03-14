package tk.andrielson.carrinhos.androidapp.ui.viewhandler;

import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.view.Gravity;
import android.widget.Toast;

import tk.andrielson.carrinhos.androidapp.databinding.FragmentCadastroProdutoBinding;
import tk.andrielson.carrinhos.androidapp.observable.ProdutoObservable;
import tk.andrielson.carrinhos.androidapp.ui.fragment.CadastroProdutoFragment;
import tk.andrielson.carrinhos.androidapp.utils.Util;

public class CadastroProdutoHandler {
    private static final String TAG = CadastroProdutoHandler.class.getSimpleName();
    private final CadastroProdutoFragment.OnFragmentInteractionListener listener;
    private final FragmentCadastroProdutoBinding binding;

    public CadastroProdutoHandler(FragmentCadastroProdutoBinding binding, CadastroProdutoFragment.OnFragmentInteractionListener listener) {
        this.listener = listener;
        this.binding = binding;
    }

    public void afterTextChangedSigla(Editable s) {
        String result = s.toString();
        if (s.toString().contains(" ")) {
            result = s.toString().replaceAll(" ", "");
            Toast toast = Toast.makeText(binding.getRoot().getContext(), "Não é permitido digitar espaço na sigla!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        if (!s.toString().equals(result)) {
            binding.inputSigla.setText(result.toUpperCase());
            binding.inputSigla.setSelection(result.length());
        }
    }

    public void onBotaoExcluirClick(final ProdutoObservable produto) {
        Snackbar confirmacao = Snackbar.make(binding.coordinatorLayout, "Tem certeza de que quer excluir esse produto?", Snackbar.LENGTH_LONG);
        confirmacao.setAction("SIM", v -> listener.excluirProduto(produto));
        confirmacao.show();
    }

    public void onBotaoSalvarClick(ProdutoObservable observable) {
        if (ehNomeValido(observable) && ehSiglaValida(observable.sigla.get()) && ehPrecoValido(observable.preco.get()))
            listener.salvarProduto(observable);
        else
            Toast.makeText(binding.getRoot().getContext(), "Por favor, corrija as informações incorretas!", Toast.LENGTH_SHORT).show();
    }

    private boolean ehNomeValido(ProdutoObservable produto) {
        if (produto.nome.get() == null || produto.nome.get().isEmpty()) {
            binding.inputNome.setError("O nome do produto é obrigatório!");
            return false;
        }
        produto.nome.set((produto.nome.get().trim().toUpperCase()));
        return true;
    }

    private boolean ehSiglaValida(String sigla) {
        if (sigla == null || sigla.isEmpty()) {
            binding.inputSigla.setError("A sigla é obrigatória!");
            return false;
        }
        return true;
    }

    private boolean ehPrecoValido(String preco) {
        if (preco == null || preco.isEmpty() || Util.RStoLong(preco).equals(0L)) {
            binding.inputPreco.setError("Informe um preço maior que zero!");
            return false;
        }
        return true;
    }
}
