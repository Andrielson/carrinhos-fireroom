package tk.andrielson.carrinhos.androidapp.observable;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public final class RelatorioVendaPorDia extends RelatorioVendaPorMes {

    private static final String TAG = RelatorioVendaPorDia.class.getSimpleName();
    public final ObservableField<String> diaSemana = new ObservableField<>();

    public RelatorioVendaPorDia(@NonNull Date data, @NonNull SimpleArrayMap<String, Long> dados) {
        super(data, dados);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        this.data.set(dateFormat.format(data));
        DateFormat semanaFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        this.diaSemana.set(semanaFormat.format(data));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RelatorioVendaPorDia that = (RelatorioVendaPorDia) o;
        return Objects.equals(diaSemana, that.diaSemana);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), diaSemana);
    }
}
