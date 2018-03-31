package tk.andrielson.carrinhos.androidapp.data.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;

import tk.andrielson.carrinhos.androidapp.observable.InicioTotais;
import tk.andrielson.carrinhos.androidapp.observable.RelatorioVendaPorDia;

public interface RelatorioRepository {
    LiveData<List<RelatorioVendaPorDia>> vendasDiarias(@NonNull Date inicio, @NonNull Date fim);

    LiveData<InicioTotais> totaisVendas(@NonNull Date inicio, @NonNull Date fim);
}
