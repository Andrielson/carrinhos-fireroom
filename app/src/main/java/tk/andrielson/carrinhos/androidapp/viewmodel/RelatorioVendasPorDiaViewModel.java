package tk.andrielson.carrinhos.androidapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tk.andrielson.carrinhos.androidapp.DI;
import tk.andrielson.carrinhos.androidapp.data.repository.RelatorioRepository;
import tk.andrielson.carrinhos.androidapp.fireroom.room.converters.DateToStringConverter;
import tk.andrielson.carrinhos.androidapp.observable.RelatorioVendaPorDia;

public final class RelatorioVendasPorDiaViewModel extends ViewModel {
    private static final String TAG = RelatorioVendasPorDiaViewModel.class.getSimpleName();
    private final MediatorLiveData<List<RelatorioVendaPorDia>> mediatorLiveData = new MediatorLiveData<>();

    public RelatorioVendasPorDiaViewModel() {
        mediatorLiveData.setValue(null);
        RelatorioRepository relatorioRepo = DI.newRelatorioRepository();
        Date fim = Calendar.getInstance().getTime();
        Date inicio = DateToStringConverter.dateFromString("2018-01-01");
        mediatorLiveData.addSource(relatorioRepo.vendasDiarias(inicio != null ? inicio : fim, fim), mediatorLiveData::setValue);
    }

    @Contract(pure = true)
    @NonNull
    public LiveData<List<RelatorioVendaPorDia>> getRelatorio() {
        return mediatorLiveData;
    }
}
