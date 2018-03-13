package tk.andrielson.carrinhos.androidapp.data.dao;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

import tk.andrielson.carrinhos.androidapp.data.model.ItemVendaImpl;
import tk.andrielson.carrinhos.androidapp.data.model.VendaImpl;
import tk.andrielson.carrinhos.androidapp.data.model.VendedorImpl;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;

/**
 * Created by anfesilva on 12/03/2018.
 */

public class VendaDaoImpl extends FirestoreDao implements VendaDao<VendaImpl> {
    private static final String COLECAO = VendaImpl.COLECAO;
    private static final String TAG = VendaDaoImpl.class.getSimpleName();

    public VendaDaoImpl() {
        super(COLECAO);
    }

    @Override
    public long insert(VendaImpl venda) {
        String ultimoID = getColecaoID(COLECAO);
        Long novoCodigo = Long.valueOf(ultimoID) + 1;
        venda.setCodigo(novoCodigo);
        final String id = getIdFromCodigo(novoCodigo);
        DocumentReference novoDocumento = collection.document(id);
        WriteBatch batch = setColecaoID(COLECAO, id);
        batch.set(novoDocumento, venda);
        batch.commit().addOnSuccessListener(aVoid -> LogUtil.Log(TAG, "Nova venda " + id + " adicionada com sucesso!", Log.INFO)).addOnFailureListener(e -> {
            LogUtil.Log(TAG, "Falha ao adicionar a venda " + id, Log.ERROR);
            LogUtil.Log(TAG, e.getMessage(), Log.ERROR);
        });
        return novoCodigo;
    }

    @Override
    public int update(VendaImpl venda) {
        final String id = getIdFromCodigo(venda.getCodigo());
        DocumentReference documento = collection.document(id);
        WriteBatch batch = db.batch();
        batch.set(documento, venda);
        batch.commit().addOnSuccessListener(aVoid -> LogUtil.Log(TAG, "Venda " + id + " atualizada com sucesso!", Log.INFO)).addOnFailureListener(e -> {
            LogUtil.Log(TAG, "Falha ao atualizar a venda " + id, Log.ERROR);
            LogUtil.Log(TAG, e.getMessage(), Log.ERROR);
        });
        return 0;
    }

    @Override
    public int delete(VendaImpl venda) {
        final String id = getIdFromCodigo(venda.getCodigo());
        DocumentReference documento = collection.document(id);
        WriteBatch batch = db.batch();
        batch.delete(documento);
        batch.commit()
                .addOnSuccessListener(aVoid -> LogUtil.Log(TAG, "Venda " + id + " removida com sucesso!", Log.INFO))
                .addOnFailureListener(e -> {
                    LogUtil.Log(TAG, "Falha ao remover a venda " + id, Log.ERROR);
                    LogUtil.Log(TAG, e.getMessage(), Log.ERROR);
                });
        return 0;
    }

    @Override
    public LiveData<List<VendaImpl>> getAll() {
        FirestoreQueryLiveData liveData = new FirestoreQueryLiveData(queryPadrao);
        return Transformations.map(liveData, input -> {
            List<VendaImpl> lista = new ArrayList<>();
            for (DocumentSnapshot doc : input.getDocuments()) {
                VendaImpl venda = new VendaImpl();
                venda.setCodigo(doc.getLong(VendaImpl.CODIGO));
                venda.setComissao(doc.getLong(VendaImpl.COMISSAO).intValue());
                venda.setData(doc.getDate(VendaImpl.DATA));
                venda.setTotal(doc.getLong(VendaImpl.TOTAL));
                venda.setStatus(doc.getString(VendaImpl.STATUS));
                String codigoVendedor = doc.getDocumentReference(VendaImpl.VENDEDOR).getId();
                VendedorImpl vendedor = new VendedorImpl();
                vendedor.setCodigo(Long.valueOf(codigoVendedor));
                vendedor.setNome(doc.getString(VendaImpl.VENDEDOR_NOME));
                venda.setVendedor(vendedor);
                lista.add(venda);
            }
            return lista;
        });
    }

    @Override
    public LiveData<VendaImpl> getByCodigo(Long codigo) {
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
            return venda;
        });
    }

    public LiveData<List<ItemVendaImpl>> getItens(Long codigo) {
        Query query = db.collection("/vendas/000000000000000001/itens");
        FirestoreQueryLiveData itensLiveData = new FirestoreQueryLiveData(query);
        return Transformations.map(itensLiveData, input -> {
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
                lista.add(item);
                LogUtil.Log(TAG, String.valueOf(item.getValor()), Log.DEBUG);
            }
            return lista;
        });
    }
}
