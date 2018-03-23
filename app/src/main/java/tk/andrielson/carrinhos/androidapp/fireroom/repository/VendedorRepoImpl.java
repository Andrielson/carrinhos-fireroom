package tk.andrielson.carrinhos.androidapp.fireroom.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import tk.andrielson.carrinhos.androidapp.data.model.Vendedor;
import tk.andrielson.carrinhos.androidapp.data.repository.VendedorRepository;
import tk.andrielson.carrinhos.androidapp.fireroom.firestore.collections.VendedorFire;
import tk.andrielson.carrinhos.androidapp.fireroom.firestore.dao.VendedorFireDao;
import tk.andrielson.carrinhos.androidapp.fireroom.model.VendedorImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.room.AppDatabase;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.VendedorRoomDao;
import tk.andrielson.carrinhos.androidapp.fireroom.room.entities.VendedorRoom;

public final class VendedorRepoImpl implements VendedorRepository {
    private static final String TAG = VendedorRepoImpl.class.getSimpleName();
    private final AppDatabase database = AppDatabase.getInstancia();
    private final VendedorRoomDao roomDao = database.vendedorDao();
    private final VendedorFireDao fireDao = new VendedorFireDao();

    @Override
    public void insert(@NonNull Vendedor vendedor) {
        fireDao.insert(new VendedorFire((VendedorImpl) vendedor));
    }

    @Override
    public void update(@NonNull Vendedor vendedor) {
        fireDao.update(new VendedorFire((VendedorImpl) vendedor));
    }

    @Override
    public void delete(@NonNull Vendedor vendedor) {
        Executors.newSingleThreadExecutor().execute(() -> {
            VendedorFire vendedorFire = new VendedorFire((VendedorImpl) vendedor);
            if (roomDao.vendedorPossuiVendas(vendedor.getCodigo()))
                vendedorFire.excluido = true;
            fireDao.delete(vendedorFire);
        });
    }

    @Override
    @NonNull
    public LiveData<List<Vendedor>> getAll() {
        return listFromRoomArray(roomDao.getAll());
    }

    @Override
    @NonNull
    public LiveData<Vendedor> getByCodigo(@NonNull Long codigo) {
        return new MediatorLiveData<>();
    }

    @NonNull
    private LiveData<List<Vendedor>> listFromRoomArray(@NonNull LiveData<VendedorRoom[]> liveData) {
        MediatorLiveData<List<Vendedor>> mediatorLiveData = new MediatorLiveData<>();
        mediatorLiveData.setValue(null);
        mediatorLiveData.addSource(liveData, vendedorRooms -> {
            if (vendedorRooms == null) return;
            List<Vendedor> lista = new ArrayList<>(vendedorRooms.length);
            for (VendedorRoom pr : vendedorRooms)
                lista.add(pr.getModel());
            mediatorLiveData.setValue(lista);
        });
        return mediatorLiveData;
    }
}
