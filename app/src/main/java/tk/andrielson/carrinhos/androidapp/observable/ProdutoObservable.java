package tk.andrielson.carrinhos.androidapp.observable;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import tk.andrielson.carrinhos.androidapp.data.model.Produto;
import tk.andrielson.carrinhos.androidapp.utils.Util;

import static tk.andrielson.carrinhos.androidapp.DI.newProduto;

/**
 * Created by anfesilva on 13/03/2018.
 */

public final class ProdutoObservable extends AbsCodigoObservable {
    public final ObservableField<String> nome = new ObservableField<>();
    public final ObservableField<String> sigla = new ObservableField<>();
    public final ObservableField<String> preco = new ObservableField<>();
    public final ObservableField<String> labelLista = new ObservableField<>();
    public final ObservableBoolean ativo = new ObservableBoolean();

    private final Produto produtoModel;

    public ProdutoObservable() {
        this(newProduto());
    }

    public ProdutoObservable(@NonNull Produto produto) {
        produtoModel = produto;
        codigoSet(produto.getCodigo());
        nome.set(produto.getNome());
        sigla.set(produto.getSigla());
        preco.set(Util.longToRS(produto.getPreco()));
        ativo.set(produto.getAtivo() == null ? true : produto.getAtivo());
        labelLista.set(String.format("%s (%s)", nome.get(), sigla.get()));
    }

    public Produto getProdutoModel() {
        produtoModel.setCodigo(codigoGet());
        produtoModel.setNome(nome.get());
        produtoModel.setSigla(sigla.get());
        produtoModel.setPreco(Util.RStoLong(preco.get()));
        produtoModel.setAtivo(ativo.get());
        return produtoModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProdutoObservable that = (ProdutoObservable) o;

        if (!codigo.equals(that.codigo)) return false;
        if (!nome.equals(that.nome)) return false;
        if (!sigla.equals(that.sigla)) return false;
        if (!preco.equals(that.preco)) return false;
        if (!ativo.equals(that.ativo)) return false;
        return produtoModel.equals(that.produtoModel);
    }

    @Override
    public int hashCode() {
        int result = codigo.hashCode();
        result = 31 * result + nome.hashCode();
        result = 31 * result + sigla.hashCode();
        result = 31 * result + preco.hashCode();
        result = 31 * result + ativo.hashCode();
        result = 31 * result + produtoModel.hashCode();
        return result;
    }
}
