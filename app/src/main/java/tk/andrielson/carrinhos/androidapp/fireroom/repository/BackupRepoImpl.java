package tk.andrielson.carrinhos.androidapp.fireroom.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;

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
import tk.andrielson.carrinhos.androidapp.fireroom.room.AppDatabase;
import tk.andrielson.carrinhos.androidapp.fireroom.room.converters.DateToStringConverter;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.ProdutoRoomDao;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.VendaRoomDao;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.VendedorRoomDao;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;

public final class BackupRepoImpl implements BackupRepository {
    private static final String TAG = BackupRepoImpl.class.getSimpleName();
    private final Executor executor = Executors.newFixedThreadPool(3);

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

    private LiveData<JSONArray> backupProdutos() {
        ProdutoRoomDao produtoRoomDao = AppDatabase.getInstancia().produtoDao();
        MediatorLiveData<JSONArray> liveData = new MediatorLiveData<>();
        liveData.setValue(null);
        executor.execute(() -> liveData.addSource(produtoRoomDao.getAllOrderByCodigo(), listaProdutos -> {
            if (listaProdutos == null) {
                LogUtil.Log(TAG, "Não foi possível recuperar os produtos!", Log.ERROR);
                liveData.postValue(null);
                return;
            }
            JSONArray jsonArray = new JSONArray();
            if (listaProdutos.isEmpty()) {
                LogUtil.Log(TAG, "Não há produtos para recuperar!", Log.INFO);
                liveData.postValue(jsonArray);
                return;
            }
            try {
                for (Produto produto : listaProdutos) {
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
        }));
        return liveData;
    }

    private LiveData<JSONArray> backupVendedores() {
        VendedorRoomDao vendedorRoomDao = AppDatabase.getInstancia().vendedorDao();
        MediatorLiveData<JSONArray> liveData = new MediatorLiveData<>();
        liveData.setValue(null);
        executor.execute(() -> liveData.addSource(vendedorRoomDao.getAll(), listaVendedores -> {
            if (listaVendedores == null) {
                LogUtil.Log(TAG, "Não foi possível recuperar os vendedores!", Log.ERROR);
                liveData.postValue(null);
                return;
            }
            JSONArray jsonArray = new JSONArray();
            if (listaVendedores.isEmpty()) {
                LogUtil.Log(TAG, "Não há vendedores para recuperar!", Log.INFO);
                liveData.postValue(jsonArray);
                return;
            }
            try {
                for (Vendedor vendedor : listaVendedores) {
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
        }));
        return liveData;
    }

    private LiveData<JSONArray> backupVendas() {
        VendaRoomDao vendaRoomDao = AppDatabase.getInstancia().vendaDao();
        MediatorLiveData<JSONArray> liveData = new MediatorLiveData<>();
        liveData.setValue(null);
        executor.execute(() -> liveData.addSource(vendaRoomDao.getVendasBackup(), vendaBackups -> {
            if (vendaBackups == null) {
                LogUtil.Log(TAG, "Não foi possível recuperar as vendas!", Log.ERROR);
                liveData.postValue(null);
                return;
            }
            JSONArray jsonArrayVendas = new JSONArray();
            if (vendaBackups.length == 0) {
                LogUtil.Log(TAG, "Não há vendas para recuperar!", Log.INFO);
                liveData.postValue(jsonArrayVendas);
                return;
            }
            try {
                for (VendaRoomDao.VendaBackup vdabkp : vendaBackups) {
                    Venda venda = vdabkp.getModel();
                    JSONObject vendaObject = new JSONObject();
                    vendaObject.put("codigo", venda.getCodigo());
                    vendaObject.put("comissao", venda.getComissao());
                    vendaObject.put("data", DateToStringConverter.stringFromDate(venda.getData()));
                    vendaObject.put("status", venda.getStatus());
                    vendaObject.put("valor_comissao", venda.getValorComissao());
                    vendaObject.put("valor_pago", venda.getValorPago());
                    vendaObject.put("valor_total", venda.getValorTotal());
                    vendaObject.put("vendedor", venda.getVendedor().getCodigo());
                    JSONArray jsonArrayItens = new JSONArray();
                    for (ItemVenda itemVenda : venda.getItens()) {
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
                }
            } catch (JSONException e) {
                LogUtil.Log(TAG, "Erro na conversão de Venda em JSON", Log.ERROR);
                LogUtil.Log(TAG, e.getLocalizedMessage(), Log.ERROR);
            }
            liveData.postValue(jsonArrayVendas);
        }));
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
            SimpleArrayMap<String, JSONArray> arrayMap = (mediatorLiveData.getValue() != null) ? mediatorLiveData.getValue() : new SimpleArrayMap<>(3);
            arrayMap.put(colecao, jsonArray);
            mediatorLiveData.setValue(arrayMap);
        }
    }
}
