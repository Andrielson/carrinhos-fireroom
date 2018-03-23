package tk.andrielson.carrinhos.androidapp.fireroom.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import tk.andrielson.carrinhos.androidapp.data.model.ItemVenda;
import tk.andrielson.carrinhos.androidapp.data.model.Venda;
import tk.andrielson.carrinhos.androidapp.data.repository.VendaRepository;
import tk.andrielson.carrinhos.androidapp.fireroom.firestore.collections.ItemVendaFire;
import tk.andrielson.carrinhos.androidapp.fireroom.firestore.collections.VendaFire;
import tk.andrielson.carrinhos.androidapp.fireroom.firestore.dao.VendaFireDao;
import tk.andrielson.carrinhos.androidapp.fireroom.model.ItemVendaImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.model.VendaImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.room.AppDatabase;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.VendaRoomDao;
import tk.andrielson.carrinhos.androidapp.fireroom.room.entities.VendaRoom;

@SuppressWarnings("unchecked")
public final class VendaRepoImpl implements VendaRepository {

    private static final String TAG = ProdutoRepoImpl.class.getSimpleName();
    private final AppDatabase database = AppDatabase.getInstancia();
    private final VendaRoomDao roomDao = database.vendaDao();
    private final VendaFireDao fireDao = new VendaFireDao();

    @Override
    public void insert(@NonNull Venda venda) {
        int qt = venda.getItens().size();
        ItemVendaFire[] itensVendaFire = new ItemVendaFire[qt];
        for (int i = 0; i < qt; i++)
            itensVendaFire[i] = new ItemVendaFire((ItemVendaImpl) venda.getItens().get(i));
        fireDao.insert(new VendaFire((VendaImpl) venda), itensVendaFire);
    }

    @Override
    public void update(@NonNull Venda venda) {
        int qt = venda.getItens().size();
        ItemVendaFire[] itensVendaFire = new ItemVendaFire[qt];
        for (int i = 0; i < qt; i++)
            itensVendaFire[i] = new ItemVendaFire((ItemVendaImpl) venda.getItens().get(i));
        fireDao.update(new VendaFire((VendaImpl) venda), itensVendaFire);
    }

    @Override
    public void delete(@NonNull Venda venda) {
        fireDao.delete(new VendaFire((VendaImpl) venda));
    }

    @Override
    public LiveData<List<Venda>> getAll() {
        final MediatorLiveData<List<Venda>> mediatorLiveData = new MediatorLiveData<>();
        mediatorLiveData.setValue(null);
        mediatorLiveData.addSource(roomDao.getAllComVendedor(), vendas -> {
            if (vendas == null) return;
            List<Venda> lista = new ArrayList<>(vendas.length);
            for (VendaRoom.VendaComVendedorTotal vt : vendas)
                lista.add(VendaRoom.getModel(vt));
            mediatorLiveData.setValue(lista);
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
