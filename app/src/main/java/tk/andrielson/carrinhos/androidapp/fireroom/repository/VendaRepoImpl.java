package tk.andrielson.carrinhos.androidapp.fireroom.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import tk.andrielson.carrinhos.androidapp.data.model.ItemVenda;
import tk.andrielson.carrinhos.androidapp.data.model.Venda;
import tk.andrielson.carrinhos.androidapp.data.repository.VendaRepository;
import tk.andrielson.carrinhos.androidapp.fireroom.room.AppDatabase;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.VendaDaoRoom;
import tk.andrielson.carrinhos.androidapp.fireroom.room.entities.VendaRoom;

public final class VendaRepoImpl implements VendaRepository {

    private static final String TAG = ProdutoRepoImpl.class.getSimpleName();
    private final AppDatabase database = AppDatabase.getInstancia();
    private final VendaDaoRoom daoRoom = database.vendaDao();

    @Override
    public void insert(@NonNull Venda venda) {

    }

    @Override
    public void update(@NonNull Venda venda) {

    }

    @Override
    public void delete(@NonNull Venda venda) {

    }

    @Override
    public LiveData<List<Venda>> getAll() {
        final MediatorLiveData<List<Venda>> mediatorLiveData = new MediatorLiveData<>();
        mediatorLiveData.setValue(null);
        mediatorLiveData.addSource(daoRoom.getAllComVendedor(), vendaTestes -> {
            if (vendaTestes == null) return;
            List<Venda> lista = new ArrayList<>(vendaTestes.length);
            for (VendaRoom.VendaComVendedorTotal vt : vendaTestes)
                lista.add(VendaRoom.getModel(vt));
            mediatorLiveData.postValue(lista);
        });
        return mediatorLiveData;
    }

    @Override
    public LiveData<Venda> getByCodigo(@NonNull Long codigo) {
        final MediatorLiveData<Venda> mediatorLiveData = new MediatorLiveData<>();
        mediatorLiveData.setValue(null);
        return mediatorLiveData;
    }

    @Override
    public LiveData<List<ItemVenda>> getItens(@NonNull Long codigo) {
        final MediatorLiveData<List<ItemVenda>> mediatorLiveData = new MediatorLiveData<>();
        mediatorLiveData.setValue(null);
        return mediatorLiveData;
    }
}
