package tk.andrielson.carrinhos.androidapp.fireroom.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tk.andrielson.carrinhos.androidapp.data.repository.RelatorioRepository;
import tk.andrielson.carrinhos.androidapp.fireroom.room.AppDatabase;
import tk.andrielson.carrinhos.androidapp.fireroom.room.converters.DateToStringConverter;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.ProdutoRoomDao;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.VendaRoomDao;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.VendedorRoomDao;
import tk.andrielson.carrinhos.androidapp.observable.InicioTotais;
import tk.andrielson.carrinhos.androidapp.observable.RelatorioVendaPorDia;
import tk.andrielson.carrinhos.androidapp.utils.Util;

public final class RelatorioRepoImpl implements RelatorioRepository {

    private static final String TAG = RelatorioRepoImpl.class.getSimpleName();
    private final AppDatabase database = AppDatabase.getInstancia();
    private final ProdutoRoomDao produtoDao = database.produtoDao();
    private final VendedorRoomDao vendedorDao = database.vendedorDao();
    private final VendaRoomDao vendaDao = database.vendaDao();

    @Override
    public LiveData<List<RelatorioVendaPorDia>> vendasDiarias(@NonNull Date inicio, @NonNull Date fim) {
        MediatorLiveData<List<RelatorioVendaPorDia>> mediatorLiveData = new MediatorLiveData<>();
        mediatorLiveData.setValue(null);
        mediatorLiveData.addSource(vendaDao.getVendasDiarias(inicio, fim), vendasPorDias -> {
            if (vendasPorDias == null) return;
            List<RelatorioVendaPorDia> lista = new ArrayList<>(vendasPorDias.length);
            for (VendaRoomDao.VendasPorDia vpd : vendasPorDias) {
                Date data = DateToStringConverter.dateFromString(vpd.data);
                SimpleArrayMap<String, Long> dados = new SimpleArrayMap<>(3);
                dados.put("total", vpd.valorTotal);
                dados.put("pago", vpd.valorPago);
                dados.put("comissao", vpd.valorComissao);
                lista.add(new RelatorioVendaPorDia(data != null ? data : Calendar.getInstance().getTime(), dados));
            }
            mediatorLiveData.setValue(lista);
        });
        return mediatorLiveData;
    }

    @Override
    public LiveData<InicioTotais> totaisVendas(@NonNull Date inicio, @NonNull Date fim) {
        MediatorLiveData<InicioTotais> mediatorLiveData = new MediatorLiveData<>();
        mediatorLiveData.setValue(null);
        mediatorLiveData.addSource(vendaDao.getTotaisVendas(inicio, fim), totaisVendas -> {
            if (totaisVendas == null) return;
            InicioTotais inicioTotais = new InicioTotais(totaisVendas.valorTotal, totaisVendas.valorPago, totaisVendas.valorComissao);
            mediatorLiveData.setValue(inicioTotais);
        });
        return mediatorLiveData;
    }

    public LiveData<SimpleArrayMap<String, String>> getTopVendedor(@NonNull Date inicio, @NonNull Date fim) {
        return Transformations.map(vendaDao.getTotaisVendasComVendedor(inicio, fim), input -> {
            SimpleArrayMap<String, String> arrayMap = null;
            if (input != null) {
                arrayMap = new SimpleArrayMap<>(4);
                arrayMap.put("vendedor", input.vendedor.getNome());
                arrayMap.put("vendido", Util.longToRS(input.valorTotal));
                arrayMap.put("pago", Util.longToRS(input.valorPago));
                arrayMap.put("comissao", Util.longToRS(input.valorComissao));
            }
            return arrayMap;
        });
    }
}
