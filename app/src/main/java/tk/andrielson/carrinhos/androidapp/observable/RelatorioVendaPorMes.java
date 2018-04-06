package tk.andrielson.carrinhos.androidapp.observable;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import tk.andrielson.carrinhos.androidapp.utils.LogUtil;
import tk.andrielson.carrinhos.androidapp.utils.Util;

public class RelatorioVendaPorMes {

    private static final String TAG = RelatorioVendaPorMes.class.getSimpleName();
    public final ObservableField<String> data = new ObservableField<>();
    public final ObservableField<String> valorTotal = new ObservableField<>();
    public final ObservableField<String> valorPago = new ObservableField<>();
    public final ObservableField<String> valorComissao = new ObservableField<>();

    public RelatorioVendaPorMes(@NonNull Date data, @NonNull SimpleArrayMap<String, Long> dados) {
        DateFormat dateFormat = new SimpleDateFormat("MMMM/yyyy", Locale.getDefault());
        this.data.set(dateFormat.format(data));
        this.valorTotal.set(Util.longToRS(dados.get("total")));
        this.valorPago.set(Util.longToRS(dados.get("pago")));
        this.valorComissao.set(Util.longToRS(dados.get("comissao")));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelatorioVendaPorMes that = (RelatorioVendaPorMes) o;
        return Objects.equals(data, that.data) &&
                Objects.equals(valorTotal, that.valorTotal) &&
                Objects.equals(valorPago, that.valorPago) &&
                Objects.equals(valorComissao, that.valorComissao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, valorTotal, valorPago, valorComissao);
    }
}
