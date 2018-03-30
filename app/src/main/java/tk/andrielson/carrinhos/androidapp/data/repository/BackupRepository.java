package tk.andrielson.carrinhos.androidapp.data.repository;

import android.arch.lifecycle.MediatorLiveData;
import android.support.v4.util.SimpleArrayMap;

import org.json.JSONArray;

public interface BackupRepository {
    String PRODUTOS = "PRODUTOS";
    String VENDEDORES = "VENDEDORES";
    String VENDAS = "VENDAS";
    MediatorLiveData<SimpleArrayMap<String, JSONArray>> exportar();
    void importar();
}
