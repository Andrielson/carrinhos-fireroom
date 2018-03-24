package tk.andrielson.carrinhos.androidapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import tk.andrielson.carrinhos.androidapp.DI;
import tk.andrielson.carrinhos.androidapp.data.repository.RelatorioRepository;
import tk.andrielson.carrinhos.androidapp.observable.RelatorioVendaPorDia;

public final class RelatorioVendasPorDiaViewModel extends ViewModel {
    private static final String TAG = RelatorioVendasPorDiaViewModel.class.getSimpleName();
    private final MediatorLiveData<List<RelatorioVendaPorDia>> mediatorLiveData = new MediatorLiveData<>();

    public RelatorioVendasPorDiaViewModel() {
        mediatorLiveData.setValue(null);
        RelatorioRepository relatorioRepo = DI.newRelatorioRepository();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date fim = Calendar.getInstance().getTime();
        Date inicio;
        try {
            inicio = dateFormat.parse("01/01/2018");
        } catch (ParseException e) {
            e.printStackTrace();
            inicio = Calendar.getInstance().getTime();
        }
        mediatorLiveData.addSource(relatorioRepo.vendasDiarias(inicio, fim), mediatorLiveData::setValue);
    }

    @Contract(pure = true)
    @NonNull
    public LiveData<List<RelatorioVendaPorDia>> getRelatorio() {
        return mediatorLiveData;
    }
}
