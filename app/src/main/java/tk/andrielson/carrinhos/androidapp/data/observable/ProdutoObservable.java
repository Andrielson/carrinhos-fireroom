package tk.andrielson.carrinhos.androidapp.data.observable;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import java.util.Locale;

import tk.andrielson.carrinhos.androidapp.DI;
import tk.andrielson.carrinhos.androidapp.data.model.Produto;

/**
 * Created by anfesilva on 13/03/2018.
 */

public final class ProdutoObservable {
    public final ObservableField<String> codigo = new ObservableField<>();
    public final ObservableField<String> nome = new ObservableField<>();
    public final ObservableField<String> sigla = new ObservableField<>();
    public final ObservableField<String> preco = new ObservableField<>();
    public final ObservableBoolean ativo = new ObservableBoolean();

    private final Produto produtoModel;

    public ProdutoObservable() {
        produtoModel = DI.newProduto();
    }

    public ProdutoObservable(@NonNull Produto produto) {
        produtoModel = produto;
        codigo.set(produto.getCodigo() == null ? "0" : String.valueOf(produto.getCodigo()));
        nome.set(produto.getNome());
        sigla.set(produto.getSigla());
        preco.set(produto.getPreco() == null ? "R$ 0,00" : String.format(Locale.getDefault(), "R$ %.2f", (double) produto.getPreco() / 100));
        ativo.set(produto.getAtivo() == null ? true : produto.getAtivo());
    }

    public Produto getProdutoModel() {
        produtoModel.setCodigo(Long.valueOf(codigo.get()));
        produtoModel.setNome(nome.get());
        produtoModel.setSigla(sigla.get());
        produtoModel.setPreco(Long.valueOf(preco.get().replaceAll("\\D", "")));
        produtoModel.setAtivo(ativo.get());
        return produtoModel;
    }
}
