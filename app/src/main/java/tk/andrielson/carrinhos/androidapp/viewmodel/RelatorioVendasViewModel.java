package tk.andrielson.carrinhos.androidapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tk.andrielson.carrinhos.androidapp.DI;
import tk.andrielson.carrinhos.androidapp.data.repository.RelatorioRepository;
import tk.andrielson.carrinhos.androidapp.fireroom.room.converters.DateToStringConverter;
import tk.andrielson.carrinhos.androidapp.observable.RelatorioVendaPorDia;
import tk.andrielson.carrinhos.androidapp.observable.RelatorioVendaPorMes;

import static tk.andrielson.carrinhos.androidapp.utils.Util.inicioMes;
import static tk.andrielson.carrinhos.androidapp.utils.Util.inicioSemana;

public final class RelatorioVendasViewModel extends ViewModel {
    private static final String TAG = RelatorioVendasViewModel.class.getSimpleName();
    private final MediatorLiveData<List<RelatorioVendaPorDia>> mediatorLiveData = new MediatorLiveData<>();
    private final RelatorioRepository relatorioRepo = DI.newRelatorioRepository();

    public RelatorioVendasViewModel() {
        mediatorLiveData.setValue(null);
        RelatorioRepository relatorioRepo = DI.newRelatorioRepository();
        Date fim = Calendar.getInstance().getTime();
        Date inicio = DateToStringConverter.dateFromString("2018-01-01");
        mediatorLiveData.addSource(relatorioRepo.vendasDiarias(inicio != null ? inicio : fim, fim), mediatorLiveData::setValue);
    }

    @NonNull
    public LiveData<List<RelatorioVendaPorDia>> getRelatorioDiario() {
        return mediatorLiveData;
    }

    @NonNull
    public LiveData<List<RelatorioVendaPorDia>> getRelatorioDiario(@NonNull RelatorioDiario rel) {
        Calendar calendar = Calendar.getInstance();
        Date inicio;
        Date hoje = calendar.getTime();
        switch (rel) {
            case QUINZE_DIAS:
                calendar.add(Calendar.DATE, -15);
                inicio = calendar.getTime();
                break;
            case ESTE_MES:
                inicio = inicioMes();
                break;
            case ESTA_SEMANA:
            default:
                inicio = inicioSemana();
        }
        return relatorioRepo.vendasDiarias(inicio, hoje);
    }

    @NonNull
    public LiveData<List<RelatorioVendaPorMes>> getRelatorioMensal(@NonNull RelatorioMensal rel) {
        Calendar calendar = Calendar.getInstance();
        Date inicio;
        Date hoje = calendar.getTime();
        switch (rel) {
            case ESTE_ANO:
                int doy = calendar.get(Calendar.DAY_OF_YEAR);
                calendar.add(Calendar.DATE, -1 * doy + 1);
                inicio = calendar.getTime();
                break;
            case SEIS_MESES:
            default:
                calendar.add(Calendar.MONTH, -6);
                inicio = calendar.getTime();
        }
        return relatorioRepo.vendasMensais(inicio, hoje);
    }

    public enum RelatorioDiario {
        ESTA_SEMANA,
        QUINZE_DIAS,
        ESTE_MES
    }

    public enum RelatorioMensal {
        SEIS_MESES,
        ESTE_ANO
    }
}
