package tk.andrielson.carrinhos.androidapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;

import java.util.Date;

import tk.andrielson.carrinhos.androidapp.DI;
import tk.andrielson.carrinhos.androidapp.data.repository.RelatorioRepository;
import tk.andrielson.carrinhos.androidapp.fireroom.room.converters.DateToStringConverter;
import tk.andrielson.carrinhos.androidapp.observable.InicioTotais;

import static tk.andrielson.carrinhos.androidapp.utils.Util.inicioMes;
import static tk.andrielson.carrinhos.androidapp.utils.Util.inicioSemana;

public final class InicioViewModel extends ViewModel {
    private static final String TAG = InicioViewModel.class.getSimpleName();
    private final RelatorioRepository relatorioRepo = DI.newRelatorioRepository();

    @NonNull
    public LiveData<InicioTotais> getTotalHoje() {
//        Date hoje = Calendar.getInstance().getTime();
        Date hoje = DateToStringConverter.dateFromString("2018-01-26");
        return relatorioRepo.totaisVendas(hoje, hoje);
    }

    @NonNull
    public LiveData<InicioTotais> getTotalSemana() {
        return relatorioRepo.totaisVendas(inicioSemana(), DateToStringConverter.dateFromString("2018-01-26"));
    }

    @NonNull
    public LiveData<InicioTotais> getTotalMes() {
        return relatorioRepo.totaisVendas(inicioMes(), DateToStringConverter.dateFromString("2018-01-26"));
    }

    @NonNull
    public LiveData<SimpleArrayMap<String, String>> getTopVendedorSemana() {
        return relatorioRepo.getTopVendedor(inicioSemana(), DateToStringConverter.dateFromString("2018-01-26"));
    }

    @NonNull
    public LiveData<SimpleArrayMap<String, String>> getTopVendedorMes() {
        return relatorioRepo.getTopVendedor(inicioMes(), DateToStringConverter.dateFromString("2018-01-26"));
    }
/*
    private Date inicioSemana() {
        Calendar calendar = Calendar.getInstance();
        Date hoje = DateToStringConverter.dateFromString("2018-01-26");
        calendar.setTime(hoje);
        int dow = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE, -1 * dow + 1);
        DateFormat format = new SimpleDateFormat("EEEE", Locale.getDefault());
        LogUtil.Log(TAG, format.format(calendar.getTime()), Log.DEBUG);
        return calendar.getTime();
    }

    private Date inicioMes() {
        Calendar calendar = Calendar.getInstance();
        Date hoje = DateToStringConverter.dateFromString("2018-01-26");
        calendar.setTime(hoje);
        int dom = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DATE, -1 * dom + 1);
        LogUtil.Log(TAG, DateToStringConverter.stringFromDate(calendar.getTime()), Log.DEBUG);
        return calendar.getTime();
    }*/
}
