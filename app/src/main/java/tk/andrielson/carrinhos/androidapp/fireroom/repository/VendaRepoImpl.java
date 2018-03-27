package tk.andrielson.carrinhos.androidapp.fireroom.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import tk.andrielson.carrinhos.androidapp.data.model.Venda;
import tk.andrielson.carrinhos.androidapp.data.repository.VendaRepository;
import tk.andrielson.carrinhos.androidapp.fireroom.firestore.dao.VendaFireDao;
import tk.andrielson.carrinhos.androidapp.fireroom.model.ItemVendaImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.model.VendaImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.room.AppDatabase;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.VendaRoomDao;
import tk.andrielson.carrinhos.androidapp.fireroom.room.entities.VendaRoom;

@SuppressWarnings("unchecked")
public final class VendaRepoImpl implements VendaRepository<VendaImpl, ItemVendaImpl> {

    private static final String TAG = VendaRepoImpl.class.getSimpleName();
    private final AppDatabase database = AppDatabase.getInstancia();
    private final VendaRoomDao roomDao = database.vendaDao();
    private final VendaFireDao fireDao = new VendaFireDao();

    @Override
    public void insert(@NonNull Venda venda) {
        fireDao.insert((VendaImpl) venda, (ItemVendaImpl[]) venda.getItens());
    }

    @Override
    public void update(@NonNull Venda venda) {
        fireDao.update((VendaImpl) venda, (ItemVendaImpl[]) venda.getItens());
    }

    @Override
    public void delete(@NonNull Venda venda) {
        fireDao.delete((VendaImpl) venda);
    }

    @Override
    public LiveData<List<VendaImpl>> getAll() {
        final MediatorLiveData<List<VendaImpl>> mediatorLiveData = new MediatorLiveData<>();
        mediatorLiveData.setValue(null);
        //TODO: mover essa lógica de conversão pra classe VendaComVendedorTotal
        mediatorLiveData.addSource(roomDao.getAllComVendedor(), vendas -> {
            if (vendas == null) return;
            List<VendaImpl> lista = new ArrayList<>(vendas.length);
            for (VendaRoomDao.VendaComVendedorTotal vt : vendas)
                lista.add(vt.getModel());
            mediatorLiveData.setValue(lista);
        });
        return mediatorLiveData;
    }

    @Override
    public LiveData<VendaImpl> getByCodigo(@NonNull Long codigo) {
        final MediatorLiveData<VendaImpl> mediatorLiveData = new MediatorLiveData<>();
        mediatorLiveData.setValue(null);
        return mediatorLiveData;
    }

    @Override
    public LiveData<List<ItemVendaImpl>> getItens(@NonNull Long codigo) {
        final MediatorLiveData<List<ItemVendaImpl>> mediatorLiveData = new MediatorLiveData<>();
        mediatorLiveData.setValue(null);
        //TODO: mover essa lógica de conversão pra classe ItemComProduto
        mediatorLiveData.addSource(roomDao.getItensComProduto(codigo), itens -> {
            if (itens == null) return;
            List<ItemVendaImpl> lista = new ArrayList<>(itens.length);
            for (VendaRoomDao.ItemComProduto itcp : itens)
                lista.add(itcp.getModel());
            mediatorLiveData.setValue(lista);
        });
        return mediatorLiveData;
    }
}
