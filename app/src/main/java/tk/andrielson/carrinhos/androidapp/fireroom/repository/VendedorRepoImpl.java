package tk.andrielson.carrinhos.androidapp.fireroom.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import tk.andrielson.carrinhos.androidapp.data.model.Vendedor;
import tk.andrielson.carrinhos.androidapp.data.repository.VendedorRepository;
import tk.andrielson.carrinhos.androidapp.fireroom.room.AppDatabase;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.VendedorDaoRoom;
import tk.andrielson.carrinhos.androidapp.fireroom.room.entities.VendedorRoom;

public final class VendedorRepoImpl implements VendedorRepository {
    private static final String TAG = VendedorRepoImpl.class.getSimpleName();
    private final AppDatabase database = AppDatabase.getInstancia();
    private final VendedorDaoRoom daoRoom = database.vendedorDao();

    @Override
    public void insert(@NonNull Vendedor vendedor) {

    }

    @Override
    public void update(@NonNull Vendedor vendedor) {

    }

    @Override
    public void delete(@NonNull Vendedor vendedor) {

    }

    @Override
    public LiveData<List<Vendedor>> getAll() {
        final MediatorLiveData<List<Vendedor>> mediatorLiveData = new MediatorLiveData<>();
        mediatorLiveData.setValue(null);
        Executors.newSingleThreadExecutor().execute(() -> mediatorLiveData.addSource(daoRoom.getAll(), vendedorRooms -> {
            if (vendedorRooms == null) return;
            List<Vendedor> lista = new ArrayList<>(vendedorRooms.length);
            for (VendedorRoom pr : vendedorRooms)
                lista.add(pr.getModel());
            mediatorLiveData.postValue(lista);
        }));
        return mediatorLiveData;
    }

    @Override
    public LiveData<Vendedor> getByCodigo(@NonNull Long codigo) {
        return new MediatorLiveData<>();
    }
}
