package tk.andrielson.carrinhos.androidapp.fireroom.repository;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import tk.andrielson.carrinhos.androidapp.data.model.ItemVenda;
import tk.andrielson.carrinhos.androidapp.data.model.Produto;
import tk.andrielson.carrinhos.androidapp.data.model.Venda;
import tk.andrielson.carrinhos.androidapp.data.model.Vendedor;
import tk.andrielson.carrinhos.androidapp.data.repository.BackupRepository;
import tk.andrielson.carrinhos.androidapp.fireroom.model.ItemVendaImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.model.ProdutoImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.model.VendaImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.model.VendedorImpl;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;

public final class BackupRepoImpl implements BackupRepository {
    private static final String TAG = BackupRepoImpl.class.getSimpleName();
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final Executor executor = Executors.newFixedThreadPool(5);

    @Override
    public MediatorLiveData<SimpleArrayMap<String, JSONArray>> exportar() {
        MediatorLiveData<SimpleArrayMap<String, JSONArray>> mediatorLiveData = new MediatorLiveData<>();
        mediatorLiveData.setValue(null);
        mediatorLiveData.addSource(backupProdutos(), new Observador(mediatorLiveData, PRODUTOS));
        mediatorLiveData.addSource(backupVendedores(), new Observador(mediatorLiveData, VENDEDORES));
        mediatorLiveData.addSource(backupVendas(), new Observador(mediatorLiveData, VENDAS));
        return mediatorLiveData;
    }

    @Override
    public void importar() {

    }

    private MutableLiveData<JSONArray> backupProdutos() {
        MutableLiveData<JSONArray> liveData = new MutableLiveData<>();
        liveData.postValue(null);
        firestore.collection(ProdutoImpl.COLECAO).get().addOnCompleteListener(executor, task -> {
            if (!task.isSuccessful()) {
                LogUtil.Log(TAG, "Não foi possível recuperar os produtos!", Log.ERROR);
                liveData.postValue(null);
                return;
            }
            JSONArray jsonArray = new JSONArray();
            if (task.getResult() == null || task.getResult().isEmpty() || task.getResult().getDocuments().isEmpty()) {
                LogUtil.Log(TAG, "Não há produtos para recuperar!", Log.INFO);
                liveData.postValue(jsonArray);
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
            liveData.postValue(jsonArray);
        });
        return liveData;
    }

    private MutableLiveData<JSONArray> backupVendedores() {
        MutableLiveData<JSONArray> liveData = new MutableLiveData<>();
        liveData.postValue(null);
        firestore.collection(VendedorImpl.COLECAO).get().addOnCompleteListener(executor, task -> {
            if (!task.isSuccessful()) {
                LogUtil.Log(TAG, "Não foi possível recuperar os vendedores!", Log.ERROR);
                liveData.postValue(null);
                return;
            }
            JSONArray jsonArray = new JSONArray();
            if (task.getResult() == null || task.getResult().isEmpty() || task.getResult().getDocuments().isEmpty()) {
                LogUtil.Log(TAG, "Não há vendedores para recuperar!", Log.INFO);
                liveData.postValue(jsonArray);
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
            liveData.postValue(jsonArray);
        });
        return liveData;
    }

    private MutableLiveData<JSONArray> backupVendas() {
        MutableLiveData<JSONArray> liveData = new MutableLiveData<>();
        liveData.postValue(null);
        firestore.collection(VendaImpl.COLECAO).get().addOnCompleteListener(executor, taskVendas -> {
            if (!taskVendas.isSuccessful()) {
                LogUtil.Log(TAG, "Não foi possível recuperar os vendas!", Log.ERROR);
                liveData.postValue(null);
                return;
            }
            JSONArray jsonArrayVendas = new JSONArray();
            if (taskVendas.getResult() == null || taskVendas.getResult().isEmpty() || taskVendas.getResult().getDocuments().isEmpty()) {
                LogUtil.Log(TAG, "Não há vendas para recuperar!", Log.INFO);
                liveData.postValue(jsonArrayVendas);
                return;
            }
            for (DocumentSnapshot doc : taskVendas.getResult().getDocuments()) {
                CollectionReference itens = firestore.collection(String.format("/%s/%s/%s", VendaImpl.COLECAO, doc.getId(), ItemVendaImpl.COLECAO));
                itens.get().addOnCompleteListener(executor, taskItens -> {
                    if (!taskItens.isSuccessful()) {
                        LogUtil.Log(TAG, "Não foi possível recuperar os itens da venda!", Log.ERROR);
                        return;
                    }
                    if (taskItens.getResult() == null || taskItens.getResult().isEmpty() || taskItens.getResult().getDocuments().isEmpty()) {
                        LogUtil.Log(TAG, "Não há itens da venda para recuperar!", Log.INFO);
                        return;
                    }
                    try {
                        Venda venda = doc.toObject(VendaImpl.class);
                        JSONObject vendaObject = new JSONObject();
                        vendaObject.put("codigo", venda.getCodigo());
                        vendaObject.put("comissao", venda.getComissao());
                        vendaObject.put("data", venda.getData());
                        vendaObject.put("status", venda.getStatus());
                        vendaObject.put("valor_comissao", venda.getValorComissao());
                        vendaObject.put("valor_pago", venda.getValorPago());
                        vendaObject.put("valor_total", venda.getValorTotal());
                        vendaObject.put("vendedor", venda.getVendedor().getCodigo());
                        JSONArray jsonArrayItens = new JSONArray();
                        for (DocumentSnapshot docItem : taskItens.getResult().getDocuments()) {
                            ItemVenda itemVenda = docItem.toObject(ItemVendaImpl.class);
                            JSONObject itemObject = new JSONObject();
                            itemObject.put("produto", itemVenda.getProduto().getCodigo());
                            itemObject.put("qt_saiu", itemVenda.getQtSaiu());
                            itemObject.put("qt_voltou", itemVenda.getQtVoltou());
                            itemObject.put("qt_vendeu", itemVenda.getQtVendeu());
                            itemObject.put("valor", itemVenda.getValor());
                            itemObject.put("total", itemVenda.getTotal());
                            jsonArrayItens.put(itemObject);
                        }
                        vendaObject.put("itens", jsonArrayItens);
                        jsonArrayVendas.put(vendaObject);
                    } catch (JSONException e) {
                        LogUtil.Log(TAG, "Erro na conversão de Venda em JSON", Log.ERROR);
                        LogUtil.Log(TAG, e.getLocalizedMessage(), Log.ERROR);
                    }
                });
            }
            liveData.postValue(jsonArrayVendas);
        });
        return liveData;
    }

    private void backupItensVenda(MutableLiveData<JSONArray> liveData) {

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
            SimpleArrayMap<String, JSONArray> arrayMap = (mediatorLiveData.getValue() != null) ? mediatorLiveData.getValue() : new SimpleArrayMap<>(3);
            arrayMap.put(colecao, jsonArray);
            mediatorLiveData.setValue(arrayMap);
        }
    }
}
