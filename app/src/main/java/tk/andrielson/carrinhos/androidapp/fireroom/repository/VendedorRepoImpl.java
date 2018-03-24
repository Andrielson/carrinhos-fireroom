package tk.andrielson.carrinhos.androidapp.fireroom.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import tk.andrielson.carrinhos.androidapp.data.model.Vendedor;
import tk.andrielson.carrinhos.androidapp.data.repository.VendedorRepository;
import tk.andrielson.carrinhos.androidapp.fireroom.firestore.dao.VendedorFireDao;
import tk.andrielson.carrinhos.androidapp.fireroom.model.VendedorImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.room.AppDatabase;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.VendedorRoomDao;
import tk.andrielson.carrinhos.androidapp.fireroom.room.entities.VendedorRoom;

public final class VendedorRepoImpl implements VendedorRepository<VendedorImpl> {
    private static final String TAG = VendedorRepoImpl.class.getSimpleName();
    private final AppDatabase database = AppDatabase.getInstancia();
    private final VendedorRoomDao roomDao = database.vendedorDao();
    private final VendedorFireDao fireDao = new VendedorFireDao();

    @Override
    public void insert(@NonNull Vendedor vendedor) {
        fireDao.insert((VendedorImpl) vendedor);
    }

    @Override
    public void update(@NonNull Vendedor vendedor) {
        fireDao.update((VendedorImpl) vendedor);
    }

    @Override
    public void delete(@NonNull Vendedor vendedor) {
        Executors.newSingleThreadExecutor().execute(() -> {
            if (roomDao.vendedorPossuiVendas(vendedor.getCodigo()))
                ((VendedorImpl) vendedor).setExcluido(true);
            fireDao.delete((VendedorImpl) vendedor);
        });
    }

    @Override
    @NonNull
    public LiveData<List<VendedorImpl>> getAll() {
        return roomDao.getAll();
    }

    @Override
    @NonNull
    public LiveData<VendedorImpl> getByCodigo(@NonNull Long codigo) {
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
