package tk.andrielson.carrinhos.androidapp.observable;

import android.annotation.SuppressLint;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

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

@SuppressLint("DefaultLocale")
@SuppressWarnings("unchecked")
public final class VendaObservable extends AbsCodigoObservable {
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

    public VendaObservable(@NonNull Venda venda) {
        vendaModel = venda;
        codigoSet(venda.getCodigo());
        data.set(venda.getData() == null ? dateFormat.format(Calendar.getInstance().getTime()) : dateFormat.format(venda.getData()));
        total.set(Util.longToRS(venda.getValorTotal()));
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
            for (ItemVenda itv : venda.getItens()) {
                ItemVendaObservable ito = new ItemVendaObservable(itv);
                ito.total.addOnPropertyChangedCallback(new TotalAtualizador(this));
                lista.add(ito);
            }
        itens.set(lista);
        if (!ehNovo())
            calculaValoresComissaoPago();
    }

    public Venda getVendaModel() {
        vendaModel.setCodigo(codigoGet());
        vendaModel.setComissao(Util.RStoLong(comissao.get()).intValue());
        try {
            vendaModel.setData(dateFormat.parse(data.get()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        vendaModel.setValorTotal(Util.RStoLong(total.get()));
        vendaModel.setStatus(status.get());
        vendaModel.setVendedor(vendedor.get().getVendedorModel());
        List<ItemVenda> lista = new ArrayList<>();
        for (ItemVendaObservable ito : itens.get())
            lista.add(ito.getItemVendaModel());
        vendaModel.setItens(lista.toArray(new ItemVenda[0]));
        return vendaModel;
    }

    public void setItensVendaObservable(List<ItemVendaObservable> lista) {
        for (ItemVendaObservable ito : lista)
            ito.total.addOnPropertyChangedCallback(new TotalAtualizador(this));
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

    public boolean estahFinalizada() {
        return "FINALIZADA".equalsIgnoreCase(status.get());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VendaObservable that = (VendaObservable) o;

        return comissao.equals(that.comissao) && data.equals(that.data) && total.equals(that.total) && status.equals(that.status) && valorComissao.equals(that.valorComissao) && valorPago.equals(that.valorPago) && vendedor.equals(that.vendedor) && itens.equals(that.itens) && vendaModel.equals(that.vendaModel);
    }

    @Override
    public int hashCode() {
        int result = comissao.hashCode();
        result = 31 * result + data.hashCode();
        result = 31 * result + total.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + valorComissao.hashCode();
        result = 31 * result + valorPago.hashCode();
        result = 31 * result + vendedor.hashCode();
        result = 31 * result + itens.hashCode();
        result = 31 * result + vendaModel.hashCode();
        return result;
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
                total += Util.RStoLong(ito.total.get());
            vendaObservable.total.set(Util.longToRS(total));
            vendaObservable.calculaValoresComissaoPago();
        }
    }
}
