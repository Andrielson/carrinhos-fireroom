package tk.andrielson.carrinhos.androidapp.observable;

import android.databinding.ObservableField;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import tk.andrielson.carrinhos.androidapp.DI;
import tk.andrielson.carrinhos.androidapp.data.model.ItemVenda;
import tk.andrielson.carrinhos.androidapp.data.model.Venda;
import tk.andrielson.carrinhos.androidapp.utils.Util;

/**
 * Created by Andrielson on 13/03/2018.
 */

public final class VendaObservable {
    public final ObservableField<String> codigo = new ObservableField<>();
    public final ObservableField<String> comissao = new ObservableField<>();
    public final ObservableField<String> data = new ObservableField<>();
    public final ObservableField<String> total = new ObservableField<>();
    public final ObservableField<String> status = new ObservableField<>();
    public final ObservableField<VendedorObservable> vendedor = new ObservableField<>();
    public final ObservableField<List<ItemVendaObservable>> itens = new ObservableField<>();

    private final Venda vendaModel;

    public VendaObservable() {
        vendaModel = DI.newVenda();
    }

    public VendaObservable(Venda venda) {
        vendaModel = venda;
        codigo.set(venda.getCodigo() == null ? "0" : String.valueOf(venda.getCodigo()));
        comissao.set(venda.getComissao() == null ? "0 %" : String.format(Locale.getDefault(), "%d %%", venda.getComissao()));
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        data.set(formatter.format(venda.getData()));
        total.set(Util.longToRS(venda.getTotal()));
        status.set(venda.getStatus());
        vendedor.set(new VendedorObservable(venda.getVendedor()));
        List<ItemVendaObservable> lista = new ArrayList<>();
        //noinspection unchecked
        for (ItemVenda itv : (List<ItemVenda>) venda.getItens())
            lista.add(new ItemVendaObservable(itv));
        itens.set(lista);
    }

    //TODO: carregar o model antes de retornar
    public Venda getVendaModel() {
        return vendaModel;
    }
}
