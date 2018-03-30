package tk.andrielson.carrinhos.androidapp.ui.activity;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import tk.andrielson.carrinhos.androidapp.R;
import tk.andrielson.carrinhos.androidapp.data.repository.BackupRepository;
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
            boolean produtos = false;
            boolean vendedores = false;
            boolean vendas = false;
            if (arrayMap == null) {
                LogUtil.Log(TAG, "ArrayMap é nulo!", Log.DEBUG);
                return;
            }
            if (arrayMap.isEmpty()) {
                LogUtil.Log(TAG, "ArrayMap está vazio!", Log.DEBUG);
                return;
            }
            if (arrayMap.containsKey(BackupRepository.PRODUTOS) && arrayMap.get(BackupRepository.PRODUTOS) == null) {
                LogUtil.Log(TAG, "Backup de PRODUTOS iniciado!", Log.DEBUG);
            }
            if (arrayMap.containsKey(BackupRepository.PRODUTOS) && arrayMap.get(BackupRepository.PRODUTOS) != null) {
                LogUtil.Log(TAG, "ArrayMap contém PRODUTOS!", Log.DEBUG);
                LogUtil.Log(TAG, arrayMap.get(BackupRepository.PRODUTOS).toString(), Log.DEBUG);
                produtos = true;
            }
            if (arrayMap.containsKey(BackupRepository.VENDEDORES) && arrayMap.get(BackupRepository.VENDEDORES) == null) {
                LogUtil.Log(TAG, "Backup de VENDEDORES iniciado!", Log.DEBUG);
            }
            if (arrayMap.containsKey(BackupRepository.VENDEDORES) && arrayMap.get(BackupRepository.VENDEDORES) != null) {
                LogUtil.Log(TAG, "ArrayMap contém VENDEDORES!", Log.DEBUG);
                LogUtil.Log(TAG, arrayMap.get(BackupRepository.VENDEDORES).toString(), Log.DEBUG);
                vendedores = true;
            }
            if (arrayMap.containsKey(BackupRepository.VENDAS) && arrayMap.get(BackupRepository.VENDAS) == null) {
                LogUtil.Log(TAG, "Backup de VENDAS iniciado!", Log.DEBUG);
            }
            if (arrayMap.containsKey(BackupRepository.VENDAS) && arrayMap.get(BackupRepository.VENDAS) != null) {
                LogUtil.Log(TAG, "ArrayMap contém VENDAS!", Log.DEBUG);
                LogUtil.Log(TAG, arrayMap.get(BackupRepository.VENDAS).toString(), Log.DEBUG);
                vendas = true;
            }
            if (produtos && vendedores && vendas)
                viewModel.salvaBackup();
        });
    }

    public void onClickImportar(View view) {

    }
}
