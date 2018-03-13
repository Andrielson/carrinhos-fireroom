package tk.andrielson.carrinhos.androidapp.data.observable;

import android.databinding.ObservableField;

import tk.andrielson.carrinhos.androidapp.DI;
import tk.andrielson.carrinhos.androidapp.data.model.Produto;

/**
 * Created by anfesilva on 13/03/2018.
 */

public class ProdutoObservable {
    public final ObservableField<Long> codigo = new ObservableField<>();
    public final ObservableField<String> nome = new ObservableField<>();
    public final ObservableField<String> sigla = new ObservableField<>();
    public final ObservableField<Long> preco = new ObservableField<>();
    public final ObservableField<Boolean> ativo = new ObservableField<>();

    private final Produto produtoModel;

    public ProdutoObservable() {
        produtoModel = DI.newProduto();
    }

    public ProdutoObservable(Produto produto) {
        produtoModel = produto;
        codigo.set(produto.getCodigo());
        nome.set(produto.getNome());
        sigla.set(produto.getSigla());
        preco.set(produto.getPreco());
        ativo.set(produto.getAtivo());
    }

    public Produto getProdutoModel() {
        return produtoModel;
    }
}
