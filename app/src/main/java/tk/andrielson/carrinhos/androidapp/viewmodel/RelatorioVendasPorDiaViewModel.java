package tk.andrielson.carrinhos.androidapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;

import org.jetbrains.annotations.Contract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import tk.andrielson.carrinhos.androidapp.data.dao.RelatoriosDaoImpl;
import tk.andrielson.carrinhos.androidapp.observable.RelatorioVendaPorDia;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;

public final class RelatorioVendasPorDiaViewModel extends ViewModel {
    private static final String TAG = RelatorioVendasPorDiaViewModel.class.getSimpleName();
    private final MediatorLiveData<List<RelatorioVendaPorDia>> mediatorLiveData;

    public RelatorioVendasPorDiaViewModel() {
        mediatorLiveData = new MediatorLiveData<>();
        mediatorLiveData.setValue(null);

        RelatoriosDaoImpl relatoriosDao = new RelatoriosDaoImpl();
        LiveData<SimpleArrayMap<Date, SimpleArrayMap<String, Long>>> liveData = relatoriosDao.vendasPorDia();
        mediatorLiveData.addSource(liveData, arrayMap -> {
            if (arrayMap == null) return;
            int tamanho = arrayMap.size();
            List<RelatorioVendaPorDia> lista = new ArrayList<>(tamanho);
            for (int i = 0; i < tamanho; i++)
                lista.add(new RelatorioVendaPorDia(arrayMap.keyAt(i), arrayMap.valueAt(i)));
            Collections.sort(lista, (o1, o2) -> {
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
                try {
                    Date data1 = formato.parse(o1.data.get());
                    Date data2 = formato.parse(o2.data.get());
                    return data2.compareTo(data1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            });
            mediatorLiveData.setValue(lista);
        });
    }

    @Contract(pure = true)
    @NonNull
    public LiveData<List<RelatorioVendaPorDia>> getRelatorio() {
        return mediatorLiveData;
        /*RelatoriosDaoImpl relatoriosDao = new RelatoriosDaoImpl();
        LiveData<SimpleArrayMap<Date, SimpleArrayMap<String, Long>>> liveData = relatoriosDao.vendasPorDia();
        return Transformations.map(liveData, input -> {
            if (input == null) return null;
            int tamanho = input.size();
            LogUtil.Log(TAG, "Número de dias: " + input.size(), Log.DEBUG);
            List<RelatorioVendaPorDia> lista = new ArrayList<>(tamanho);
            for (int i = 0; i < tamanho; i++)
                lista.add(new RelatorioVendaPorDia(input.keyAt(i), input.valueAt(i)));
            return lista;
        });*/
    }
}
