package tk.andrielson.carrinhos.androidapp.data.dao;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Map;

/**
 * Created by anfesilva on 08/03/2018.
 */

public class Teste implements LifecycleOwner {
    private static final String TAG = "TESTE SINGLETON";
    private static final Teste ourInstance = new Teste();

    static Teste getInstance() {
        return ourInstance;
    }

    private final MediatorLiveData<Map<String, String>> IDs;

    private Teste() {
        IDs = new MediatorLiveData<>();
        IDs.setValue(null);
        IDs.addSource(FirestoreDao.loadUltimosIDs(), IDs::postValue);
        Log.d(TAG, "Singleton TESTE criado, aparentemente, com sucesso!");
    }

    public Map<String, String> getIDs() {
        return IDs.getValue();
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return null;
    }
}
