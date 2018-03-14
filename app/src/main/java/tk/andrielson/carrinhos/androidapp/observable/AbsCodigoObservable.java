package tk.andrielson.carrinhos.androidapp.observable;

import android.databinding.ObservableField;

/**
 * Created by anfesilva on 14/03/2018.
 */

public abstract class AbsCodigoObservable {
    public final ObservableField<String> codigo = new ObservableField<>();

    protected Long codigoGet() {
        return codigo.get() == null || codigo.get().isEmpty() ? 0L : Long.valueOf(codigo.get());
    }

    protected void codigoSet(Long codigo) {
        this.codigo.set(String.valueOf(codigo == null ? 0L : codigo));
    }

    public boolean ehNovo() {
        return (codigo.get() == null || codigo.get().isEmpty() || codigo.get().equals("0"));
    }
}
