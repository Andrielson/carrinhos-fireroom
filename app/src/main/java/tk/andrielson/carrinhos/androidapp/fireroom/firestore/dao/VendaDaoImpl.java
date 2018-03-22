package tk.andrielson.carrinhos.androidapp.fireroom.firestore.dao;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.LongSparseArray;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import tk.andrielson.carrinhos.androidapp.fireroom.firestore.FirestoreQueryLiveData;
import tk.andrielson.carrinhos.androidapp.data.dao.VendaDao;
import tk.andrielson.carrinhos.androidapp.fireroom.model.ItemVendaImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.model.ProdutoImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.model.VendaImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.model.VendedorImpl;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;

public final class VendaDaoImpl extends FirestoreDao implements VendaDao<VendaImpl, ItemVendaImpl> {
    private static final String COLECAO = VendaImpl.COLECAO;
    private static final String TAG = VendaDaoImpl.class.getSimpleName();

    public VendaDaoImpl() {
        super(COLECAO);
    }

    @Override
    public long insert(@NonNull VendaImpl venda) {
        String ultimoID = getColecaoID(COLECAO);
        Long novoCodigo = Long.valueOf(ultimoID) + 1;
        venda.setCodigo(novoCodigo);
        final String idVenda = getIdFromCodigo(novoCodigo);
        DocumentReference novoDocumento = collection.document(idVenda);
        WriteBatch batch = setColecaoID(COLECAO, idVenda);
        batch.set(novoDocumento, vendaToMap(venda));
        for (ItemVendaImpl itv : venda.getItens()) {
            String idItem = getIdFromCodigo(itv.getProduto().getCodigo());
            CollectionReference collectionItem = db.collection(String.format("/%s/%s/%s", VendaImpl.COLECAO, idVenda, ItemVendaImpl.COLECAO));
            DocumentReference novoItem = collectionItem.document(idItem);
            batch.set(novoItem, itemToMap(itv));
        }
        batch.commit().addOnSuccessListener(aVoid -> LogUtil.Log(TAG, "Nova venda " + idVenda + " adicionada com sucesso!", Log.INFO)).addOnFailureListener(e -> {
            LogUtil.Log(TAG, "Falha ao adicionar a venda " + idVenda, Log.ERROR);
            LogUtil.Log(TAG, e.getMessage(), Log.ERROR);
        });
        return novoCodigo;
    }

    @Override
    public int update(@NonNull VendaImpl venda) {
        final String idVenda = getIdFromCodigo(venda.getCodigo());
        DocumentReference documento = collection.document(idVenda);
        WriteBatch batch = db.batch();
        batch.set(documento, vendaToMap(venda));
        for (ItemVendaImpl itv : venda.getItens()) {
            String idItem = getIdFromCodigo(itv.getProduto().getCodigo());
            CollectionReference collectionItem = db.collection(String.format("/%s/%s/%s", VendaImpl.COLECAO, idVenda, ItemVendaImpl.COLECAO));
            DocumentReference novoItem = collectionItem.document(idItem);
            batch.set(novoItem, itemToMap(itv));
        }
        batch.commit().addOnSuccessListener(aVoid -> LogUtil.Log(TAG, "Venda " + idVenda + " atualizada com sucesso!", Log.INFO)).addOnFailureListener(e -> {
            LogUtil.Log(TAG, "Falha ao atualizar a venda " + idVenda, Log.ERROR);
            LogUtil.Log(TAG, e.getMessage(), Log.ERROR);
        });
        return 0;
    }

    @Override
    public int delete(@NonNull VendaImpl venda) {
        final String idVenda = getIdFromCodigo(venda.getCodigo());
        final WriteBatch batch = db.batch();
        Query queryItens = db.collection(String.format("/%s/%s/%s", VendaImpl.COLECAO, idVenda, ItemVendaImpl.COLECAO));
        queryItens.get().addOnSuccessListener(Executors.newSingleThreadExecutor(), documentSnapshots -> {
            if (!documentSnapshots.isEmpty())
                for (DocumentSnapshot doc : documentSnapshots.getDocuments())
                    batch.delete(doc.getReference());
            batch.delete(collection.document(idVenda));
            batch.commit()
                    .addOnSuccessListener(aVoid -> LogUtil.Log(TAG, "Venda " + idVenda + " removida com sucesso!", Log.INFO))
                    .addOnFailureListener(e -> {
                        LogUtil.Log(TAG, "Falha ao remover a venda " + idVenda, Log.ERROR);
                        LogUtil.Log(TAG, e.getMessage(), Log.ERROR);
                    });
        });
        return 0;
    }

    @Override
    public LiveData<List<VendaImpl>> getAll() {
        Query query = queryPadrao.orderBy(VendaImpl.DATA, Query.Direction.DESCENDING);
        FirestoreQueryLiveData liveData = new FirestoreQueryLiveData(query);
        LiveData<List<VendaImpl>> vendas = Transformations.map(liveData, input -> {
            if (input == null) return null;
            List<VendaImpl> lista = new ArrayList<>();
            for (DocumentSnapshot doc : input.getDocuments()) {
                try {
                    VendaImpl venda = new VendaImpl();
                    venda.setCodigo(doc.getLong(VendaImpl.CODIGO));
                    venda.setComissao(doc.getLong(VendaImpl.COMISSAO).intValue());
                    venda.setData(doc.getDate(VendaImpl.DATA));
                    venda.setTotal(doc.getLong(VendaImpl.TOTAL));
                    venda.setStatus(doc.getString(VendaImpl.STATUS));
                    VendedorImpl vendedor = new VendedorImpl();
                    vendedor.setCodigo(Long.valueOf(doc.getDocumentReference(VendaImpl.VENDEDOR).getId()));
                    venda.setVendedor(vendedor);
                    lista.add(venda);
                } catch (java.lang.RuntimeException e) {
                    LogUtil.Log(TAG, e.getLocalizedMessage(), Log.ERROR);
                    LogUtil.Log(TAG, VendaImpl.CODIGO + ": " + doc.getLong(VendaImpl.CODIGO), Log.ERROR);
                    LogUtil.Log(TAG, VendaImpl.COMISSAO + ": " + doc.getLong(VendaImpl.COMISSAO), Log.ERROR);
                    LogUtil.Log(TAG, VendaImpl.DATA + ": " + doc.getDate(VendaImpl.DATA), Log.ERROR);
                    LogUtil.Log(TAG, VendaImpl.TOTAL + ": " + doc.getLong(VendaImpl.TOTAL), Log.ERROR);
                    LogUtil.Log(TAG, VendaImpl.STATUS + ": " + doc.getString(VendaImpl.STATUS), Log.ERROR);
                }
            }
            return lista;
        });

        // Usa MediatorLiveData pra fazer um JOIN entre Vendas e Vendedores
        final MediatorLiveData<List<VendaImpl>> mediatorLiveData = new MediatorLiveData<>();
        mediatorLiveData.addSource(vendas, mediatorLiveData::setValue);
        VendedorDaoImpl vendedorDao = new VendedorDaoImpl();
        LiveData<LongSparseArray<VendedorImpl>> vendedores = vendedorDao.getForJoin();
        mediatorLiveData.addSource(vendedores, joinVendedores -> {
            List<VendaImpl> lista = mediatorLiveData.getValue();
            if (lista != null && joinVendedores != null) {
                for (VendaImpl vda : lista) {
                    Long cod = vda.getVendedor().getCodigo();
                    vda.setVendedor(joinVendedores.get(cod));
                }
                mediatorLiveData.setValue(lista);
            }
        });
        return mediatorLiveData;
    }

    @Override
    public LiveData<VendaImpl> getByCodigo(@NonNull Long codigo) {
        Query query = queryPadrao.whereEqualTo(VendaImpl.CODIGO, codigo);
        FirestoreQueryLiveData liveData = new FirestoreQueryLiveData(query);
        return Transformations.map(liveData, input -> {
            if (input.getDocuments().isEmpty())
                return null;
            DocumentSnapshot doc = input.getDocuments().get(0);
            VendaImpl venda = new VendaImpl();
            venda.setCodigo(doc.getLong(VendaImpl.CODIGO));
            venda.setComissao(Integer.valueOf(String.valueOf(doc.getLong(VendaImpl.COMISSAO))));
            venda.setData(doc.getDate(VendaImpl.DATA));
            venda.setTotal(doc.getLong(VendaImpl.TOTAL));
            String codigoVendedor = doc.getDocumentReference(VendaImpl.VENDEDOR).getId();
            VendedorImpl vendedor = new VendedorImpl();
            vendedor.setCodigo(Long.valueOf(codigoVendedor));
            vendedor.setNome(doc.getString(VendaImpl.VENDEDOR_NOME));
            venda.setVendedor(vendedor);
            return venda;
        });
    }

    public LiveData<List<ItemVendaImpl>> getItens(@NonNull Long codigo) {
        Query query = db.collection(String.format("/%s/%s/%s", VendaImpl.COLECAO, getIdFromCodigo(codigo), ItemVendaImpl.COLECAO));
        FirestoreQueryLiveData itensLiveData = new FirestoreQueryLiveData(query);
        LiveData<List<ItemVendaImpl>> itens = Transformations.map(itensLiveData, input -> {
            List<ItemVendaImpl> lista = new ArrayList<>();
            for (DocumentSnapshot doc : input.getDocuments()) {
                ItemVendaImpl item = new ItemVendaImpl();
                item.setValor(doc.getLong(ItemVendaImpl.VALOR));
                Long qtd = doc.getLong(ItemVendaImpl.QT_SAIU);
                item.setQtSaiu(qtd != null ? qtd.intValue() : 0);
                qtd = doc.getLong(ItemVendaImpl.QT_VOLTOU);
                item.setQtVoltou(qtd != null ? qtd.intValue() : 0);
                qtd = doc.getLong(ItemVendaImpl.QT_VENDEU);
                item.setQtVendeu(qtd != null ? qtd.intValue() : 0);
                String codigoProduto = doc.getDocumentReference(ItemVendaImpl.PRODUTO).getId();
                ProdutoImpl produto = new ProdutoImpl();
                produto.setCodigo(Long.valueOf(codigoProduto));
                //
                //TODO: salvar e recuperar o nome e a sigla do produto no item da venda
                //produto.setSigla(SIGLA-DO-ITEM);
                //produto.setNome(NOME-DO-ITEM);
                //
                item.setProduto(produto);
                lista.add(item);
            }
            return lista;
        });

        // Usa MediatorLiveData pra fazer um JOIN entre Itens e Produtos
        final MediatorLiveData<List<ItemVendaImpl>> mediatorLiveData = new MediatorLiveData<>();
        mediatorLiveData.addSource(itens, mediatorLiveData::setValue);
        ProdutoDaoImpl produtoDao = new ProdutoDaoImpl();
        LiveData<LongSparseArray<ProdutoImpl>> produtosLiveData = produtoDao.getForJoin();
        mediatorLiveData.addSource(produtosLiveData, produtos -> {
            List<ItemVendaImpl> lista = mediatorLiveData.getValue();
            if (lista != null && produtos != null) {
                for (ItemVendaImpl itv : lista) {
                    Long cod = itv.getProduto().getCodigo();
                    itv.setProduto(produtos.get(cod));
                }
                mediatorLiveData.setValue(lista);
            }
        });
        return mediatorLiveData;
    }

    private Map<String, Object> vendaToMap(@NonNull VendaImpl venda) {
        Map<String, Object> map = new HashMap<>(7, 1);
        map.put(VendaImpl.CODIGO, venda.getCodigo());
        map.put(VendaImpl.COMISSAO, venda.getComissao());
        map.put(VendaImpl.DATA, venda.getData());
        map.put(VendaImpl.STATUS, venda.getStatus());
        map.put(VendaImpl.TOTAL, venda.getTotal());
        map.put(VendaImpl.VENDEDOR, db.collection(VendedorImpl.COLECAO).document(getIdFromCodigo(venda.getVendedor().getCodigo())));
        return map;
    }

    private Map<String, Object> itemToMap(@NonNull ItemVendaImpl item) {
        Map<String, Object> map = new HashMap<>(5, 1);
        map.put(ItemVendaImpl.QT_SAIU, item.getQtSaiu());
        map.put(ItemVendaImpl.QT_VOLTOU, item.getQtVoltou());
        map.put(ItemVendaImpl.QT_VENDEU, item.getQtVendeu());
        map.put(ItemVendaImpl.VALOR, item.getValor());
        map.put(ItemVendaImpl.PRODUTO, db.collection(ProdutoImpl.COLECAO).document(getIdFromCodigo(item.getProduto().getCodigo())));
        return map;
    }
}
