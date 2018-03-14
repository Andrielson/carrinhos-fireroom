package tk.andrielson.carrinhos.androidapp.observable;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import java.util.Locale;

import tk.andrielson.carrinhos.androidapp.data.model.Vendedor;

import static tk.andrielson.carrinhos.androidapp.DI.newVendedor;

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
        this(newVendedor());
    }

    public VendedorObservable(@NonNull Vendedor vendedor) {
        this.vendedorModel = vendedor;
        codigo.set(String.valueOf(vendedor.getCodigo() == null ? 0L : vendedor.getCodigo()));
        nome.set(vendedor.getNome());
        comissao.set(vendedor.getComissao() == null ? "" : String.format(Locale.getDefault(), "%d %%", vendedor.getComissao()));
        ativo.set(vendedor.getAtivo() == null ? true : vendedor.getAtivo());
    }

    @NonNull
    public Vendedor getVendedorModel() {
        vendedorModel.setCodigo(codigo.get() == null ? 0L : Long.valueOf(codigo.get()));
        vendedorModel.setNome(nome.get());
        vendedorModel.setComissao(comissao.get() == null ? 0 : Integer.valueOf(comissao.get().replaceAll("\\D", "")));
        vendedorModel.setAtivo(ativo.get());
        return vendedorModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VendedorObservable that = (VendedorObservable) o;

        if (!codigo.equals(that.codigo)) return false;
        if (!nome.equals(that.nome)) return false;
        if (!comissao.equals(that.comissao)) return false;
        if (!ativo.equals(that.ativo)) return false;
        return vendedorModel.equals(that.vendedorModel);
    }

    @Override
    public int hashCode() {
        int result = codigo.hashCode();
        result = 31 * result + nome.hashCode();
        result = 31 * result + comissao.hashCode();
        result = 31 * result + ativo.hashCode();
        result = 31 * result + vendedorModel.hashCode();
        return result;
    }
}
