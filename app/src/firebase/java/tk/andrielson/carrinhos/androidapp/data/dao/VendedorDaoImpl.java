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

import tk.andrielson.carrinhos.androidapp.data.model.Vendedor;
import tk.andrielson.carrinhos.androidapp.data.model.VendedorImpl;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;

/**
 * Implementação de VendedorDao para o banco Firestore.
 */
public class VendedorDaoImpl extends FirestoreDao implements VendedorDao {
    private static final String COLECAO = VendedorImpl.COLLECTION;
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
    @Override
    public long insert(Vendedor vendedor) {
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
    @Override
    public int update(Vendedor vendedor) {
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
    @Override
    public int delete(Vendedor vendedor) {
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
    @Override
    public LiveData<List<Vendedor>> getAll() {
        Query query = queryPadrao.orderBy(VendedorImpl.NOME);
        FirestoreQueryLiveData liveData = new FirestoreQueryLiveData(query);
        return Transformations.map(liveData, new ListaVendedorDeserializer());
    }

    /**
     * Procura um vendedor especificado pelo código informado e o retorna encapsulado
     * numa LiveData para sempre manter a informação atualizada.
     *
     * @param codigo o código do vendedor a ser procurado/retornado
     * @return o vendedor encapsulado em uma LiveData
     */
    @Override
    public LiveData<Vendedor> getByCodigo(Long codigo) {
        Query query = queryPadrao.whereEqualTo("codigo", codigo);
        FirestoreQueryLiveData liveData = new FirestoreQueryLiveData(query);
        return Transformations.map(liveData, new VendedorDeserializer());
    }

    private class ListaVendedorDeserializer implements Function<QuerySnapshot, List<Vendedor>> {

        @Override
        public List<Vendedor> apply(QuerySnapshot input) {
            List<Vendedor> lista = new ArrayList<>();
            for (DocumentSnapshot doc : input.getDocuments()) {
                lista.add(doc.toObject(VendedorImpl.class));
            }
            return lista;
        }
    }

    private class VendedorDeserializer implements Function<QuerySnapshot, Vendedor> {
        @Override
        public VendedorImpl apply(QuerySnapshot input) {
            return input.getDocuments().isEmpty() ? null : input.getDocuments().get(0).toObject(VendedorImpl.class);
        }
    }
}
