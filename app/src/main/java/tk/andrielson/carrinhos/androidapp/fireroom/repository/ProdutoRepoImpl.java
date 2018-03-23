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
import tk.andrielson.carrinhos.androidapp.fireroom.room.AppDatabase;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.ProdutoDaoRoom;
import tk.andrielson.carrinhos.androidapp.fireroom.room.entities.ProdutoRoom;

public final class ProdutoRepoImpl implements ProdutoRepository {
    private static final String TAG = ProdutoRepoImpl.class.getSimpleName();
    private final AppDatabase database = AppDatabase.getInstancia();
    private final ProdutoDaoRoom daoRoom = database.produtoDao();

    @Override
    public void insert(Produto produto) {

    }

    @Override
    public void update(Produto produto) {

    }

    @Override
    public void delete(Produto produto) {

    }

    @NonNull
    @Override
    public LiveData<List<Produto>> getAll() {
        final MediatorLiveData<List<Produto>> mediatorLiveData = new MediatorLiveData<>();
        mediatorLiveData.setValue(null);
        Executors.newSingleThreadExecutor().execute(() -> mediatorLiveData.addSource(daoRoom.getAll(), produtoRooms -> {
            if (produtoRooms == null) return;
            List<Produto> lista = new ArrayList<>(produtoRooms.length);
            for (ProdutoRoom pr : produtoRooms)
                lista.add(pr.getModel());
            mediatorLiveData.postValue(lista);
        }));
        return mediatorLiveData;
    }

    @NonNull
    @Override
    public LiveData<Produto> getByCodigo(Long codigo) {
        return new MediatorLiveData<>();
    }

    @NonNull
    @Override
    public LiveData<List<Produto>> getAll(SimpleArrayMap<String, String> ordenacao) {
        return new MediatorLiveData<>();
    }
}
