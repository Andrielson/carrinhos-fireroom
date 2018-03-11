package tk.andrielson.carrinhos.androidapp.ui.viewhandler;

import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import tk.andrielson.carrinhos.androidapp.data.model.Produto;
import tk.andrielson.carrinhos.androidapp.databinding.FragmentCadastroProdutoBinding;
import tk.andrielson.carrinhos.androidapp.ui.fragment.CadastroProdutoFragment;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;

/**
 * Created by anfesilva on 09/03/2018.
 */

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

    public void onBotaoExcluirClick(View view, final Produto produto) {
        Snackbar confirmacao = Snackbar.make(binding.coordinatorLayout, "Tem certeza de que quer excluir esse produto?", Snackbar.LENGTH_LONG);
        confirmacao.setAction("SIM", v -> listener.excluirProduto(produto));
        confirmacao.show();
    }

    public void onBotaoSalvarClick(View view, Produto produto) {
        if (produto != null && ehNomeValido(produto.getNome(), produto) && ehSiglaValida(produto.getSigla()) && ehPrecoValido(produto.getPreco()))
            listener.salvarProduto(produto, produto.getCodigo() == null);
        else {
            Toast.makeText(view.getContext(), "Por favor, corrija as informações incorretas!", Toast.LENGTH_SHORT).show();
            LogUtil.Log(TAG, "Produto nulo ou inválido!", Log.ERROR);
        }
    }

    private boolean ehNomeValido(String nome, Produto produto) {
        if (nome == null || nome.isEmpty()) {
            binding.inputNome.setError("O nome do produto é obrigatório!");
            return false;
        }
        produto.setNome(nome.trim().toUpperCase());
        return true;
    }

    private boolean ehSiglaValida(String sigla) {
        if (sigla == null || sigla.isEmpty()) {
            binding.inputSigla.setError("A sigla é obrigatória!");
            return false;
        }
        return true;
    }

    private boolean ehPrecoValido(Long preco) {
        if (preco == null || preco == 0L) {
            binding.inputPreco.setError("Informe um preço maior que zero!");
            return false;
        }
        return true;
    }
}
