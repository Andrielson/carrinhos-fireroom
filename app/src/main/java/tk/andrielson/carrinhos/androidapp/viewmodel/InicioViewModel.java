package tk.andrielson.carrinhos.androidapp.viewmodel;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tk.andrielson.carrinhos.androidapp.DI;
import tk.andrielson.carrinhos.androidapp.data.repository.RelatorioRepository;
import tk.andrielson.carrinhos.androidapp.observable.InicioTotais;
import tk.andrielson.carrinhos.androidapp.observable.RelatorioVendaPorDia;

public final class InicioViewModel extends ViewModel {
    private static final String TAG = InicioViewModel.class.getSimpleName();
    private final RelatorioRepository relatorioRepo = DI.newRelatorioRepository();

    @NonNull
    public LiveData<InicioTotais> getTotalHoje() {
        Date hoje = Calendar.getInstance().getTime();
        return relatorioRepo.totaisVendas(hoje, hoje);
    }

    @NonNull
    public LiveData<InicioTotais> getTotalSemana() {
        Calendar calendar = Calendar.getInstance();
        Date hoje = calendar.getTime();
        int dow = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE, -1 * dow + 1);
        return relatorioRepo.totaisVendas(calendar.getTime(), hoje);
    }

    @NonNull
    public LiveData<InicioTotais> getTotalMes() {
        Calendar calendar = Calendar.getInstance();
        Date hoje = calendar.getTime();
        int dom = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DATE, -1 * dom + 1);
        return relatorioRepo.totaisVendas(calendar.getTime(), hoje);
    }

    private class SomaTotais implements Function<List<RelatorioVendaPorDia>, InicioTotais> {
        @Override
        public InicioTotais apply(List<RelatorioVendaPorDia> input) {
            InicioTotais totais;
            if (input == null)
                totais = new InicioTotais(0L, 0L, 0L);
            else {
                long vendido, pago, comissao;
                vendido = pago = comissao = 0;
                for (RelatorioVendaPorDia vendaPorDia : input) {
                    vendido += Long.valueOf(vendaPorDia.valorTotal.get());
                    pago += Long.valueOf(vendaPorDia.valorPago.get());
                    comissao += Long.valueOf(vendaPorDia.valorComissao.get());
                }
                totais = new InicioTotais(vendido, pago, comissao);
            }
            return totais;
        }
    }
}
