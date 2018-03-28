package tk.andrielson.carrinhos.androidapp.ui.activity;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import tk.andrielson.carrinhos.androidapp.R;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;
import tk.andrielson.carrinhos.androidapp.viewmodel.BackupViewModel;

public class BackupActivity extends AppCompatActivity {

    private static final String PERMISSAO_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final int CODIGO_WRITE_EXTERNAL_STORAGE = 789;
    private static final String TAG = BackupActivity.class.getSimpleName();

    private Button btnExportar;
    private Button btnImportar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        btnExportar = findViewById(R.id.btn_exportar);
        btnImportar = findViewById(R.id.btn_importar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, PERMISSAO_WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{PERMISSAO_WRITE_EXTERNAL_STORAGE}, CODIGO_WRITE_EXTERNAL_STORAGE);
        else
            btnExportar.setEnabled(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODIGO_WRITE_EXTERNAL_STORAGE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            btnExportar.setEnabled(true);
        else
            Toast.makeText(this, "É preciso dar permissão de escrita para realizar o backup", Toast.LENGTH_SHORT).show();
    }

    public void onClickExportar(View view) {
        BackupViewModel viewModel = ViewModelProviders.of(this).get(BackupViewModel.class);
        viewModel.exportar().observe(this, arrayMap -> {
            if (arrayMap == null) {
                LogUtil.Log(TAG, "ArrayMap é nulo!", Log.DEBUG);
                return;
            }
            if (arrayMap.isEmpty()) {
                LogUtil.Log(TAG, "ArrayMap está vazio!", Log.DEBUG);
                return;
            }
            if (arrayMap.containsKey("PRODUTOS") && arrayMap.get("PRODUTOS") == null) {
                LogUtil.Log(TAG, "Backup de PRODUTOS iniciado!", Log.DEBUG);
            }
            if (arrayMap.containsKey("PRODUTOS") && arrayMap.get("PRODUTOS") != null) {
                LogUtil.Log(TAG, "ArrayMap contém PRODUTOS!", Log.DEBUG);
                LogUtil.Log(TAG, arrayMap.get("PRODUTOS").toString(), Log.DEBUG);
            }
            if (arrayMap.containsKey("VENDEDORES") && arrayMap.get("VENDEDORES") == null) {
                LogUtil.Log(TAG, "Backup de VENDEDORES iniciado!", Log.DEBUG);
            }
            if (arrayMap.containsKey("VENDEDORES") && arrayMap.get("VENDEDORES") != null) {
                LogUtil.Log(TAG, "ArrayMap contém VENDEDORES!", Log.DEBUG);
                LogUtil.Log(TAG, arrayMap.get("VENDEDORES").toString(), Log.DEBUG);
            }
        });
        File diretorio;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            diretorio = new File(Environment.getExternalStorageDirectory(), "/APP_CARRINHOS/BACKUP"); // External Storage
        else
            diretorio = new File(this.getFilesDir(), "/BACKUP"); // Internal Storage
        if (!diretorio.exists()) {
            //noinspection ResultOfMethodCallIgnored
            diretorio.mkdirs();
        }
        LogUtil.Log(TAG, diretorio.getAbsolutePath(), Log.VERBOSE);
        Toast.makeText(this, diretorio.getAbsolutePath(), Toast.LENGTH_SHORT).show();
    }

    public void onClickImportar(View view) {

    }

    //      private static class ExportarBackupTask extends AsyncTask
//      private static class ImportarBackupTask extends AsyncTask
}
