package tk.andrielson.carrinhos.androidapp.fireroom.repository;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import tk.andrielson.carrinhos.androidapp.data.model.Produto;
import tk.andrielson.carrinhos.androidapp.data.model.Vendedor;
import tk.andrielson.carrinhos.androidapp.data.repository.BackupRepository;
import tk.andrielson.carrinhos.androidapp.fireroom.model.ProdutoImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.model.VendedorImpl;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;

public final class BackupRepoImpl implements BackupRepository {
    private static final String TAG = BackupRepoImpl.class.getSimpleName();
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final Executor executor = Executors.newFixedThreadPool(5);

    @Override
    public void exportar() {
        MediatorLiveData<SimpleArrayMap<String, JSONArray>> mediatorLiveData = new MediatorLiveData<>();
        mediatorLiveData.setValue(null);
        mediatorLiveData.addSource(backupProdutos(), new Observador(mediatorLiveData, "PRODUTOS"));
        mediatorLiveData.addSource(backupVendedores(), new Observador(mediatorLiveData, "VENDEDORES"));
    }

    @Override
    public void importar() {

    }

    private MutableLiveData<JSONArray> backupProdutos() {
        MutableLiveData<JSONArray> liveData = new MutableLiveData<>();
        liveData.setValue(null);
        firestore.collection(ProdutoImpl.COLECAO).get().addOnCompleteListener(executor, task -> {
            if (!task.isSuccessful()) {
                LogUtil.Log(TAG, "Não foi possível recuperar os produtos!", Log.ERROR);
                liveData.setValue(null);
                return;
            }
            JSONArray jsonArray = new JSONArray();
            if (task.getResult() == null || task.getResult().isEmpty() || task.getResult().getDocuments().isEmpty()) {
                LogUtil.Log(TAG, "Não há produtos para recuperar!", Log.INFO);
                liveData.setValue(jsonArray);
                return;
            }
            try {
                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                    Produto produto = doc.toObject(ProdutoImpl.class);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("codigo", produto.getCodigo());
                    jsonObject.put("nome", produto.getNome());
                    jsonObject.put("sigla", produto.getSigla());
                    jsonObject.put("preco", produto.getPreco());
                    jsonObject.put("ativo", produto.getAtivo());
                    jsonArray.put(jsonObject);
                }
            } catch (JSONException e) {
                LogUtil.Log(TAG, "Erro na conversão de Produto em JSON", Log.ERROR);
                LogUtil.Log(TAG, e.getLocalizedMessage(), Log.ERROR);
            }
            liveData.setValue(jsonArray);
        });
        return liveData;
    }

    private MutableLiveData<JSONArray> backupVendedores() {
        MutableLiveData<JSONArray> liveData = new MutableLiveData<>();
        liveData.setValue(null);
        firestore.collection(ProdutoImpl.COLECAO).get().addOnCompleteListener(executor, task -> {
            if (!task.isSuccessful()) {
                LogUtil.Log(TAG, "Não foi possível recuperar os vendedores!", Log.ERROR);
                liveData.setValue(null);
                return;
            }
            JSONArray jsonArray = new JSONArray();
            if (task.getResult() == null || task.getResult().isEmpty() || task.getResult().getDocuments().isEmpty()) {
                LogUtil.Log(TAG, "Não há vendedores para recuperar!", Log.INFO);
                liveData.setValue(jsonArray);
                return;
            }
            try {
                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                    Vendedor vendedor = doc.toObject(VendedorImpl.class);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("codigo", vendedor.getCodigo());
                    jsonObject.put("nome", vendedor.getNome());
                    jsonObject.put("comissao", vendedor.getComissao());
                    jsonObject.put("ativo", vendedor.getAtivo());
                    jsonArray.put(jsonObject);
                }
            } catch (JSONException e) {
                LogUtil.Log(TAG, "Erro na conversão de Vendedor em JSON", Log.ERROR);
                LogUtil.Log(TAG, e.getLocalizedMessage(), Log.ERROR);
            }
            liveData.setValue(jsonArray);
        });
        return liveData;
    }


    private static class Observador implements Observer<JSONArray> {
        private final MediatorLiveData<SimpleArrayMap<String, JSONArray>> mediatorLiveData;
        private final String colecao;

        Observador(MediatorLiveData<SimpleArrayMap<String, JSONArray>> mediatorLiveData, String colecao) {
            this.mediatorLiveData = mediatorLiveData;
            this.colecao = colecao;
        }

        @Override
        public void onChanged(@Nullable JSONArray jsonArray) {
            SimpleArrayMap<String, JSONArray> arrayMap = (mediatorLiveData.getValue() != null) ? mediatorLiveData.getValue() : new SimpleArrayMap<>(4);
            arrayMap.put(colecao, jsonArray);
            mediatorLiveData.setValue(arrayMap);
        }
    }
}
