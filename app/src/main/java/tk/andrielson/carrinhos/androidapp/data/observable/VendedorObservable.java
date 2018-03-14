package tk.andrielson.carrinhos.androidapp.data.observable;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import java.util.Locale;

import tk.andrielson.carrinhos.androidapp.DI;
import tk.andrielson.carrinhos.androidapp.data.model.Vendedor;

/**
 * Created by Andrielson on 13/03/2018.
 */

public final class VendedorObservable {
    public final ObservableField<String> codigo = new ObservableField<>();
    public final ObservableField<String> nome = new ObservableField<>();
    public final ObservableField<String> comissao = new ObservableField<>();
    public final ObservableBoolean ativo = new ObservableBoolean();

    private final Vendedor vendedorModel;

    public VendedorObservable() {
        vendedorModel = DI.newVendedor();
    }

    public VendedorObservable(Vendedor vendedor) {
        this.vendedorModel = vendedor;
        codigo.set(String.valueOf(vendedor.getCodigo() == null ? "0" : vendedor.getCodigo()));
        nome.set(vendedor.getNome());
        comissao.set(vendedor.getComissao() == null ? "0 %" : String.format(Locale.getDefault(), "%d %%", vendedor.getComissao()));
        ativo.set(vendedor.getAtivo() == null ? true : vendedor.getAtivo());
    }

    public Vendedor getVendedorModel() {
        vendedorModel.setCodigo(Long.valueOf(codigo.get()));
        vendedorModel.setNome(nome.get());
        vendedorModel.setComissao(Integer.valueOf(comissao.get().replaceAll("\\D", "")));
        vendedorModel.setAtivo(ativo.get());
        return vendedorModel;
    }
}
