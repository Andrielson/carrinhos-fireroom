package tk.andrielson.carrinhos.androidapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import tk.andrielson.carrinhos.androidapp.DI;
import tk.andrielson.carrinhos.androidapp.data.repository.BackupRepository;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;

public final class BackupViewModel extends AndroidViewModel {

    private static final String TAG = BackupViewModel.class.getSimpleName();
    private final BackupRepository backupRepo;
    private LiveData<SimpleArrayMap<String, JSONArray>> liveDataExportar = null;

    public BackupViewModel(@NonNull Application application) {
        super(application);
        backupRepo = DI.newBackupRepository();
    }

    public LiveData<SimpleArrayMap<String, JSONArray>> exportar() {
        liveDataExportar = backupRepo.exportar();
        return liveDataExportar;
    }

    public void salvaBackup() {
        File diretorio;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            diretorio = new File(Environment.getExternalStorageDirectory(), "/APP_CARRINHOS/BACKUP"); // External Storage
        else
            diretorio = new File(this.getApplication().getFilesDir(), "/BACKUP"); // Internal Storage
        if (!diretorio.exists())
            //noinspection ResultOfMethodCallIgnored
            diretorio.mkdirs();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault());
        Date hoje = Calendar.getInstance().getTime();
        String nomeArquivo = "Backup_" + dateFormat.format(hoje) + ".json";
        String URIArquivo = diretorio.getAbsolutePath() + "/" + nomeArquivo;
        LogUtil.Log(TAG, URIArquivo, Log.VERBOSE);
        Toast.makeText(this.getApplication(), URIArquivo, Toast.LENGTH_LONG).show();

        if (liveDataExportar == null) return;
        SimpleArrayMap<String, JSONArray> arrayMap = liveDataExportar.getValue();
        if (arrayMap == null) return;
        JSONObject backupObject = new JSONObject();
        try {
            if (arrayMap.containsKey(BackupRepository.PRODUTOS))
                backupObject.put(BackupRepository.PRODUTOS, arrayMap.get(BackupRepository.PRODUTOS));
            if (arrayMap.containsKey(BackupRepository.VENDEDORES))
                backupObject.put(BackupRepository.VENDEDORES, arrayMap.get(BackupRepository.VENDEDORES));
            if (arrayMap.containsKey(BackupRepository.VENDAS))
                backupObject.put(BackupRepository.VENDAS, arrayMap.get(BackupRepository.VENDAS));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try (FileOutputStream outputStream = new FileOutputStream(URIArquivo)) {
            outputStream.write(backupObject.toString().getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
