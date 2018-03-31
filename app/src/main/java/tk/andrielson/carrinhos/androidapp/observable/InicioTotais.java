package tk.andrielson.carrinhos.androidapp.observable;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import tk.andrielson.carrinhos.androidapp.utils.Util;

public final class InicioTotais {
    public final ObservableField<String> vendido = new ObservableField<>();
    public final ObservableField<String> pago = new ObservableField<>();
    public final ObservableField<String> comissao = new ObservableField<>();

    public InicioTotais(@NonNull Long vendido, @NonNull Long pago, @NonNull Long comissao) {
        this.vendido.set(Util.longToRS(vendido));
        this.pago.set(Util.longToRS(pago));
        this.comissao.set(Util.longToRS(comissao));
    }

    public InicioTotais(String vendido, String pago, String comissao) {
        this.vendido.set(vendido);
        this.pago.set(pago);
        this.comissao.set(comissao);
    }
}
