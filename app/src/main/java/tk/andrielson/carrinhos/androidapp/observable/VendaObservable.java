package tk.andrielson.carrinhos.androidapp.observable;

import android.annotation.SuppressLint;
import android.databinding.Observable;
import android.databinding.ObservableField;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import tk.andrielson.carrinhos.androidapp.DI;
import tk.andrielson.carrinhos.androidapp.data.model.ItemVenda;
import tk.andrielson.carrinhos.androidapp.data.model.Venda;
import tk.andrielson.carrinhos.androidapp.utils.Util;

/**
 * Created by Andrielson on 13/03/2018.
 */
@SuppressLint("DefaultLocale")
@SuppressWarnings("unchecked")
public final class VendaObservable {
    public final ObservableField<String> codigo = new ObservableField<>();
    public final ObservableField<String> comissao = new ObservableField<>();
    public final ObservableField<String> data = new ObservableField<>();
    public final ObservableField<String> total = new ObservableField<>();
    public final ObservableField<String> status = new ObservableField<>();
    public final ObservableField<String> valorComissao = new ObservableField<>();
    public final ObservableField<String> valorPago = new ObservableField<>();
    public final ObservableField<VendedorObservable> vendedor = new ObservableField<>();
    public final ObservableField<List<ItemVendaObservable>> itens = new ObservableField<>();

    private final Venda vendaModel;
    private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public VendaObservable() {
        this(DI.newVenda());
    }

    public VendaObservable(Venda venda) {
        vendaModel = venda;
        codigo.set(venda.getCodigo() == null ? "0" : String.valueOf(venda.getCodigo()));
        data.set(venda.getData() == null ? dateFormat.format(Calendar.getInstance().getTime()) : dateFormat.format(venda.getData()));
        total.set(Util.longToRS(venda.getTotal()));
        status.set(venda.getStatus());
        vendedor.set(venda.getVendedor() == null ? new VendedorObservable(DI.newVendedor()) : new VendedorObservable(venda.getVendedor()));
        if (venda.getComissao() != null)
            comissao.set(String.format("%d %%", venda.getComissao()));
        else if (venda.getVendedor() != null && venda.getVendedor().getComissao() != null)
            comissao.set(String.format("%d %%", venda.getVendedor().getComissao()));
        else
            comissao.set("40 %");
        List<ItemVendaObservable> lista = new ArrayList<>();
        if (venda.getItens() != null)
            for (ItemVenda itv : (List<ItemVenda>) venda.getItens()) {
                ItemVendaObservable ito = new ItemVendaObservable(itv);
                ito.valor.addOnPropertyChangedCallback(new TotalAtualizador(this));
                lista.add(ito);
            }
        itens.set(lista);
        //TODO: calculaValoresComissaoPago();
    }

    public Venda getVendaModel() {
        vendaModel.setCodigo(Long.valueOf(codigo.get()));
        vendaModel.setComissao(Util.RStoLong(comissao.get()).intValue());
        try {
            vendaModel.setData(dateFormat.parse(data.get()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        vendaModel.setTotal(Util.RStoLong(total.get()));
        vendaModel.setStatus(status.get());
        vendaModel.setVendedor(vendedor.get().getVendedorModel());
        List<ItemVenda> lista = new ArrayList<>();
        for (ItemVendaObservable ito : itens.get())
            lista.add(ito.getItemVendaModel());
        vendaModel.setItens(lista);
        return vendaModel;
    }

    public void setItensVendaObservable(List<ItemVendaObservable> lista) {
        for (ItemVendaObservable ito : lista)
            ito.valor.addOnPropertyChangedCallback(new TotalAtualizador(this));
        itens.set(lista);
    }

    private void calculaValoresComissaoPago() {
        long valorTotal = Util.RStoLong(total.get());
        int comissao = Util.RStoLong(this.comissao.get()).intValue();
        long valorComissao = valorTotal * comissao / 100;
        long valorPago = valorTotal - valorComissao;
        this.valorComissao.set(Util.longToRS(valorComissao));
        this.valorPago.set(Util.longToRS(valorPago));
    }

    private class TotalAtualizador extends Observable.OnPropertyChangedCallback {
        private final VendaObservable vendaObservable;

        TotalAtualizador(VendaObservable vendaObservable) {
            this.vendaObservable = vendaObservable;
        }

        @Override
        public void onPropertyChanged(Observable observable, int i) {
            long total = 0;
            for (ItemVendaObservable ito : vendaObservable.itens.get())
                total += Util.RStoLong(ito.valor.get());
            vendaObservable.total.set(Util.longToRS(total));
            vendaObservable.calculaValoresComissaoPago();
        }
    }
}
