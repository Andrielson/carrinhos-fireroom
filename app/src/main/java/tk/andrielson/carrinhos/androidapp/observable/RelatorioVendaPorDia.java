package tk.andrielson.carrinhos.androidapp.observable;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import tk.andrielson.carrinhos.androidapp.utils.Util;

public final class RelatorioVendaPorDia {
    private static final String TAG = RelatorioVendaPorDia.class.getSimpleName();
    public final ObservableField<String> data = new ObservableField<>();
    public final ObservableField<String> diaSemana = new ObservableField<>();
    public final ObservableField<String> valorTotal = new ObservableField<>();
    public final ObservableField<String> valorPago = new ObservableField<>();
    public final ObservableField<String> valorComissao = new ObservableField<>();

    public RelatorioVendaPorDia(@NonNull Date data, @NonNull SimpleArrayMap<String, Long> dados) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        this.data.set(dateFormat.format(data));
        DateFormat semanaFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        this.diaSemana.set(semanaFormat.format(data));
        this.valorTotal.set(Util.longToRS(dados.get("total")));
        this.valorPago.set(Util.longToRS(dados.get("pago")));
        this.valorComissao.set(Util.longToRS(dados.get("comissao")));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RelatorioVendaPorDia that = (RelatorioVendaPorDia) o;

        return data.equals(that.data) && diaSemana.equals(that.diaSemana) && valorTotal.equals(that.valorTotal) && valorPago.equals(that.valorPago) && valorComissao.equals(that.valorComissao);
    }

    @Override
    public int hashCode() {
        int result = data.hashCode();
        result = 31 * result + diaSemana.hashCode();
        result = 31 * result + valorTotal.hashCode();
        result = 31 * result + valorPago.hashCode();
        result = 31 * result + valorComissao.hashCode();
        return result;
    }
}
