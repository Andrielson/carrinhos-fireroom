package tk.andrielson.carrinhos.androidapp.data.repository;

import android.arch.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

import tk.andrielson.carrinhos.androidapp.observable.RelatorioVendaPorDia;

public interface RelatorioRepository {
    LiveData<List<RelatorioVendaPorDia>> vendasDiarias(Date inicio, Date fim);
}
