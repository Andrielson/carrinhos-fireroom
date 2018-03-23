package tk.andrielson.carrinhos.androidapp.fireroom.firestore.dao;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.LongSparseArray;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;

import java.util.List;

import tk.andrielson.carrinhos.androidapp.fireroom.firestore.FirestoreQueryLiveData;
import tk.andrielson.carrinhos.androidapp.data.dao.VendedorDao;
import tk.andrielson.carrinhos.androidapp.fireroom.model.VendedorImpl;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;

/**
 * Implementação de VendedorDao para o banco Firestore.
 */
public final class VendedorDaoImpl extends FirestoreDao {
    private static final String COLECAO = VendedorImpl.COLECAO;
    private static final String TAG = VendedorDaoImpl.class.getSimpleName();

    /**
     * O construtor configura a coleção do Firestore que irá utilizar e implementa a query padrão.
     */
    public VendedorDaoImpl() {
        super(COLECAO);
    }

    /**
     * Insere um vendedor no banco de dados e retorna o código gerado.
     *
     * @param vendedor o vendedor a ser inserido
     * @return o código gerado para o vendedor
     */
    public long insert(@NonNull VendedorImpl vendedor) {
        String ultimoID = getColecaoID(COLECAO);
        Long novoCodigo = Long.valueOf(ultimoID) + 1;
        vendedor.setCodigo(novoCodigo);
        final String id = getIdFromCodigo(novoCodigo);
        DocumentReference novoDocumento = collection.document(id);
        WriteBatch batch = setColecaoID(COLECAO, id);
        batch.set(novoDocumento, vendedor);
        batch.commit().addOnSuccessListener(aVoid -> LogUtil.Log(TAG, "Novo vendedor " + id + " adicionado com sucesso!", Log.INFO)).addOnFailureListener(e -> {
            LogUtil.Log(TAG, "Falha ao adicionar o vendedor " + id, Log.ERROR);
            LogUtil.Log(TAG, e.getMessage(), Log.ERROR);
        });
        return novoCodigo;
    }

    /**
     * Atualiza as informações de um vendedor já existente no banco de dados.
     *
     * @param vendedor o vendedor a ser atualizado
     * @return o número de vendedors atualizados
     */
    public int update(@NonNull VendedorImpl vendedor) {
        final String id = getIdFromCodigo(vendedor.getCodigo());
        DocumentReference documento = collection.document(id);
        WriteBatch batch = db.batch();
        batch.set(documento, vendedor);
        batch.commit().addOnSuccessListener(aVoid -> LogUtil.Log(TAG, "Vendedor " + id + " atualizado com sucesso!", Log.INFO)).addOnFailureListener(e -> {
            LogUtil.Log(TAG, "Falha ao atualizar o vendedor " + id, Log.ERROR);
            LogUtil.Log(TAG, e.getMessage(), Log.ERROR);
        });
        return 0;
    }

    /**
     * Remove um vendedor do banco de dados
     *
     * @param vendedor o vendedor a ser removido
     * @return o número de vendedor removidos
     */
    public int delete(@NonNull VendedorImpl vendedor) {
        final String id = getIdFromCodigo(vendedor.getCodigo());
        DocumentReference documento = collection.document(id);
        WriteBatch batch = db.batch();
        //Depois de implementar as vendas, pesquisar se o vendedor possui relação com alguma venda
        //se tiver, marcar a flag excluído.
        //batch.update(documento, VendedorImpl.EXCLUIDO, true);
        batch.delete(documento);
        batch.commit()
                .addOnSuccessListener(aVoid -> LogUtil.Log(TAG, "Vendedor " + id + " removido com sucesso!", Log.INFO))
                .addOnFailureListener(e -> {
                    LogUtil.Log(TAG, "Falha ao remover o vendedor " + id, Log.ERROR);
                    LogUtil.Log(TAG, e.getMessage(), Log.ERROR);
                });
        return 0;
    }

    /**
     * Consulta todos os vendedors do banco de dados e retorna uma lista
     * encapsulada numa LiveData observável, para manter a lista sempre atualizada.
     *
     * @return a lista de vendedors encapsulada em uma LiveData
     */
    public LiveData<List<VendedorImpl>> getAll() {
        Query query = queryPadrao.orderBy(VendedorImpl.NOME);
        FirestoreQueryLiveData liveData = new FirestoreQueryLiveData(query);
        return Transformations.map(liveData, new ListaDeserializer<>(VendedorImpl.class));
    }

    /**
     * Procura um vendedor especificado pelo código informado e o retorna encapsulado
     * numa LiveData para sempre manter a informação atualizada.
     *
     * @param codigo o código do vendedor a ser procurado/retornado
     * @return o vendedor encapsulado em uma LiveData
     */
    public LiveData<VendedorImpl> getByCodigo(@NonNull final Long codigo) {
        Query query = queryPadrao.whereEqualTo("codigo", codigo);
        FirestoreQueryLiveData liveData = new FirestoreQueryLiveData(query);
        return Transformations.map(liveData, new Deserializer<>(VendedorImpl.class));
    }

    @NonNull
    public LiveData<LongSparseArray<VendedorImpl>> getForJoin() {
        FirestoreQueryLiveData liveData = new FirestoreQueryLiveData(db.collection(COLECAO));
        return Transformations.map(liveData, input -> {
            LongSparseArray<VendedorImpl> vendedorArray = new LongSparseArray<>();
            for (DocumentSnapshot doc : input.getDocuments()) {
                VendedorImpl produto = doc.toObject(VendedorImpl.class);
                vendedorArray.put(produto.getCodigo(), produto);
            }
            return vendedorArray;
        });
    }
}
