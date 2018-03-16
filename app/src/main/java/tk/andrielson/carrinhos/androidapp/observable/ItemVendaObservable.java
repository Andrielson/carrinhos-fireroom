package tk.andrielson.carrinhos.androidapp.observable;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import tk.andrielson.carrinhos.androidapp.data.model.ItemVenda;
import tk.andrielson.carrinhos.androidapp.utils.Util;

import static tk.andrielson.carrinhos.androidapp.DI.newItemVenda;

public final class ItemVendaObservable {
    public final ObservableField<ProdutoObservable> produto = new ObservableField<>();
    public final ObservableField<String> qtSaiu = new ObservableField<>();
    public final ObservableField<String> qtVoltou = new ObservableField<>();
    public final ObservableField<String> qtVendeu = new ObservableField<>();
    public final ObservableField<String> valor = new ObservableField<>();

    private final ItemVenda itemVendaModel;

    public ItemVendaObservable() {
        this(newItemVenda());
    }

    public ItemVendaObservable(@NonNull ItemVenda itemVenda) {
        itemVendaModel = itemVenda;
        produto.set(itemVenda.getProduto() == null ? new ProdutoObservable() : new ProdutoObservable(itemVenda.getProduto()));
        qtSaiu.set(String.valueOf(itemVenda.getQtSaiu() == null ? "" : itemVenda.getQtSaiu()));
        qtVoltou.set(String.valueOf(itemVenda.getQtVoltou() == null ? "" : itemVenda.getQtVoltou()));
        qtVendeu.set(String.valueOf(itemVenda.getQtVendeu() == null ? "0" : itemVenda.getQtVendeu()));
        valor.set(Util.longToRS(itemVenda.getValor()));
    }

    public ItemVenda getItemVendaModel() {
        //noinspection unchecked
        itemVendaModel.setProduto(produto.get().getProdutoModel());
        itemVendaModel.setQtSaiu(Integer.valueOf(qtSaiu.get().isEmpty() ? "0" : qtSaiu.get()));
        itemVendaModel.setQtVoltou(Integer.valueOf(qtVoltou.get().isEmpty() ? "0" : qtVoltou.get()));
        itemVendaModel.setQtVendeu(Integer.valueOf(qtVendeu.get()));
        itemVendaModel.setValor(Util.RStoLong(valor.get()));
        return itemVendaModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemVendaObservable that = (ItemVendaObservable) o;

        if (!produto.equals(that.produto)) return false;
        if (!qtSaiu.equals(that.qtSaiu)) return false;
        if (!qtVoltou.equals(that.qtVoltou)) return false;
        if (!qtVendeu.equals(that.qtVendeu)) return false;
        if (!valor.equals(that.valor)) return false;
        return itemVendaModel.equals(that.itemVendaModel);
    }

    @Override
    public int hashCode() {
        int result = produto.hashCode();
        result = 31 * result + qtSaiu.hashCode();
        result = 31 * result + qtVoltou.hashCode();
        result = 31 * result + qtVendeu.hashCode();
        result = 31 * result + valor.hashCode();
        result = 31 * result + itemVendaModel.hashCode();
        return result;
    }
}
