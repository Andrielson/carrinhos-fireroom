package tk.andrielson.carrinhos.androidapp.fireroom.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.SimpleArrayMap;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import tk.andrielson.carrinhos.androidapp.data.repository.RelatorioRepository;
import tk.andrielson.carrinhos.androidapp.fireroom.room.AppDatabase;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.ProdutoRoomDao;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.VendaRoomDao;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.VendedorRoomDao;
import tk.andrielson.carrinhos.androidapp.fireroom.room.entities.VendaRoom;
import tk.andrielson.carrinhos.androidapp.observable.RelatorioVendaPorDia;

public final class RelatorioRepoImpl implements RelatorioRepository {

    private static final String TAG = ProdutoRepoImpl.class.getSimpleName();
    private final AppDatabase database = AppDatabase.getInstancia();
    private final ProdutoRoomDao produtoDao = database.produtoDao();
    private final VendedorRoomDao vendedorDao = database.vendedorDao();
    private final VendaRoomDao vendaDao = database.vendaDao();

    @Override
    public LiveData<List<RelatorioVendaPorDia>> vendasDiarias(@NonNull Date inicio, @NonNull Date fim) {
        MediatorLiveData<List<RelatorioVendaPorDia>> mediatorLiveData = new MediatorLiveData<>();
        mediatorLiveData.setValue(null);
        mediatorLiveData.addSource(vendaDao.getVendasDiarias("", ""), new Observer<VendaRoom.VendasPorDia[]>() {
            @Override
            public void onChanged(@Nullable VendaRoom.VendasPorDia[] vendasPorDias) {
                if (vendasPorDias == null) return;
                DateFormat formato = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                List<RelatorioVendaPorDia> lista = new ArrayList<>(vendasPorDias.length);
                for (VendaRoom.VendasPorDia vpd : vendasPorDias) {
                    Date data;
                    try {
                        data = formato.parse(vpd.data);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        data = Calendar.getInstance().getTime();
                    }
                    SimpleArrayMap<String, Long> dados = new SimpleArrayMap<>(3);
                    dados.put("total", vpd.valorTotal);
                    dados.put("pago", vpd.valorPago);
                    dados.put("comissao", vpd.valorComissao);
                    lista.add(new RelatorioVendaPorDia(data, dados));
                }
                mediatorLiveData.setValue(lista);
            }
        });
        return mediatorLiveData;
    }
}
