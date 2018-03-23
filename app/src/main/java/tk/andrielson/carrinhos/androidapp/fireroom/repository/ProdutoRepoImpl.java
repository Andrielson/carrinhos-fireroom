package tk.andrielson.carrinhos.androidapp.fireroom.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import tk.andrielson.carrinhos.androidapp.data.model.Produto;
import tk.andrielson.carrinhos.androidapp.data.repository.ProdutoRepository;
import tk.andrielson.carrinhos.androidapp.fireroom.firestore.collections.ProdutoFire;
import tk.andrielson.carrinhos.androidapp.fireroom.firestore.dao.ProdutoFireDao;
import tk.andrielson.carrinhos.androidapp.fireroom.model.ProdutoImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.room.AppDatabase;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.ProdutoRoomDao;
import tk.andrielson.carrinhos.androidapp.fireroom.room.entities.ProdutoRoom;

public final class ProdutoRepoImpl implements ProdutoRepository {
    private static final String TAG = ProdutoRepoImpl.class.getSimpleName();
    private final AppDatabase database = AppDatabase.getInstancia();
    private final ProdutoRoomDao roomDao = database.produtoDao();
    private final ProdutoFireDao fireDao = new ProdutoFireDao();

    @Override
    public void insert(Produto produto) {
        fireDao.insert(new ProdutoFire((ProdutoImpl) produto));
    }

    @Override
    public void update(Produto produto) {
        fireDao.update(new ProdutoFire((ProdutoImpl) produto));
    }

    @Override
    public void delete(Produto produto) {
        Executors.newSingleThreadExecutor().execute(() -> {
            ProdutoFire produtoFire = new ProdutoFire((ProdutoImpl) produto);
            if (roomDao.produtoPossuiVendas(produto.getCodigo()))
                produtoFire.excluido = true;
            fireDao.delete(produtoFire);
        });
    }

    @NonNull
    @Override
    public LiveData<List<Produto>> getAll() {
        return listFromRoomArray(roomDao.getAllOrderByNome());
    }

    @NonNull
    @Override
    public LiveData<Produto> getByCodigo(Long codigo) {
        return new MediatorLiveData<>();
    }

    @NonNull
    @Override
    public LiveData<List<Produto>> getAll(SimpleArrayMap<String, String> ordenacao) {
        return listFromRoomArray(roomDao.getAllOrderByCodigo());
    }

    @NonNull
    private LiveData<List<Produto>> listFromRoomArray(LiveData<ProdutoRoom[]> liveData) {
        final MediatorLiveData<List<Produto>> mediatorLiveData = new MediatorLiveData<>();
        mediatorLiveData.setValue(null);
        mediatorLiveData.addSource(liveData, produtoRooms -> {
            if (produtoRooms == null) return;
            List<Produto> lista = new ArrayList<>(produtoRooms.length);
            for (ProdutoRoom pr : produtoRooms)
                lista.add(pr.getModel());
            mediatorLiveData.setValue(lista);
        });
        return mediatorLiveData;
    }
}
