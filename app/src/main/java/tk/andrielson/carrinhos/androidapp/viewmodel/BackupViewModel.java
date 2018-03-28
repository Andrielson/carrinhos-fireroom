package tk.andrielson.carrinhos.androidapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;

import org.json.JSONArray;

import tk.andrielson.carrinhos.androidapp.DI;
import tk.andrielson.carrinhos.androidapp.data.repository.BackupRepository;

public final class BackupViewModel extends AndroidViewModel {

    private final BackupRepository backupRepo;

    public BackupViewModel(@NonNull Application application) {
        super(application);
        backupRepo = DI.newBackupRepository();
    }

    public LiveData<SimpleArrayMap<String, JSONArray>> exportar() {
        return backupRepo.exportar();
    }
}
