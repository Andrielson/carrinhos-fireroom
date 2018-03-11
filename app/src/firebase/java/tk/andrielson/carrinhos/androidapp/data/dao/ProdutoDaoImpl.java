package tk.andrielson.carrinhos.androidapp.data.dao;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;

import tk.andrielson.carrinhos.androidapp.data.model.Produto;
import tk.andrielson.carrinhos.androidapp.data.model.ProdutoImpl;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;

/**
 * Implementação de ProdutoDao para o banco Firestore.
 */
public final class ProdutoDaoImpl extends FirestoreDao implements ProdutoDao {

    private static final String COLECAO = ProdutoImpl.COLLECTION;
    private static final String TAG = ProdutoDaoImpl.class.getSimpleName();

    /**
     * O construtor configura a coleção do Firestore que irá utilizar e implementa a query padrão.
     */
    public ProdutoDaoImpl() {
        super(COLECAO);
    }

    /**
     * Insere um produto no banco de dados e retorna o código gerado.
     *
     * @param produto o produto a ser inserido
     * @return o código gerado para o produto
     */
    @Override
    public long insert(Produto produto) {
        String ultimoID = getColecaoID(COLECAO);
        Long novoCodigo = Long.valueOf(ultimoID) + 1;
        produto.setCodigo(novoCodigo);
        final String id = getIdFromCodigo(novoCodigo);
        DocumentReference novoDocumento = collection.document(id);
        WriteBatch batch = setColecaoID(COLECAO, id);
        batch.set(novoDocumento, produto);
        batch.commit().addOnSuccessListener(aVoid -> LogUtil.Log(TAG, "Novo produto " + id + " adicionado com sucesso!", Log.INFO)).addOnFailureListener(e -> {
            LogUtil.Log(TAG, "Falha ao adicionar o produto " + id, Log.ERROR);
            LogUtil.Log(TAG, e.getMessage(), Log.ERROR);
        });
        return novoCodigo;
    }

    /**
     * Atualiza as informações de um produto já existente no banco de dados.
     *
     * @param produto o produto a ser atualizado
     * @return o número de produtos atualizados
     */
    @Override
    public int update(Produto produto) {
        final String id = getIdFromCodigo(produto.getCodigo());
        DocumentReference documento = collection.document(id);
        WriteBatch batch = db.batch();
        batch.set(documento, produto);
        batch.commit().addOnSuccessListener(aVoid -> LogUtil.Log(TAG, "Produto " + id + " atualizado com sucesso!", Log.INFO)).addOnFailureListener(e -> {
            LogUtil.Log(TAG, "Falha ao atualizar o produto " + id, Log.ERROR);
            LogUtil.Log(TAG, e.getMessage(), Log.ERROR);
        });
        return 0;
    }

    /**
     * Remove um produto do banco de dados
     *
     * @param produto o produto a ser removido
     * @return o número de produto removidos
     */
    @Override
    public int delete(Produto produto) {
        final String id = getIdFromCodigo(produto.getCodigo());
        DocumentReference documento = collection.document(id);
        WriteBatch batch = db.batch();
        //Depois de implementar as vendas, pesquisar se o produto possui relação com alguma venda
        //se tiver, marcar a flag excluído.
        //batch.update(documento, ProdutoImpl.EXCLUIDO, true);
        batch.delete(documento);
        batch.commit()
                .addOnSuccessListener(aVoid -> LogUtil.Log(TAG, "Produto " + id + " removido com sucesso!", Log.INFO))
                .addOnFailureListener(e -> {
                    LogUtil.Log(TAG, "Falha ao remover o produto " + id, Log.ERROR);
                    LogUtil.Log(TAG, e.getMessage(), Log.ERROR);
                });
        return 0;
    }

    /**
     * Procura um produto especificado pelo código informado e o retorna encapsulado
     * numa LiveData para sempre manter a informação atualizada.
     *
     * @param codigo o código do produto a ser procurado/retornado
     * @return o produto encapsulado em uma LiveData
     */
    @NonNull
    @Override
    public LiveData<Produto> getByCodigo(final Long codigo) {
        Query query = queryPadrao.whereEqualTo("codigo", codigo);
        FirestoreQueryLiveData liveData = new FirestoreQueryLiveData(query);
        return Transformations.map(liveData, new ProdutoDeserializer());
    }

    /**
     * Consulta todos os produtos do banco de dados e retorna uma lista
     * encapsulada numa LiveData observável, para manter a lista sempre atualizada.
     *
     * @return a lista de produtos encapsulada em uma LiveData
     */
    @NonNull
    @Override
    public LiveData<List<Produto>> getAll() {
        Query query = queryPadrao.orderBy(ProdutoImpl.NOME);
        FirestoreQueryLiveData liveData = new FirestoreQueryLiveData(query);
        return Transformations.map(liveData, new ListaProdutoDeserializer());
    }

    /**
     * Consulta todos os produtos do banco de dados, ordenando pelos parâmetros de ordenação,
     * encapsulada numa LiveData observável, para manter a lista sempre atualizada.
     *
     * @param ordenacao uma mapa no qual a chave indica por qual atributo deve ser ordenado e o valor
     *                  indica a direção (ascendente ou descendente) da ordenação.
     * @return a lista de produtos encapsulada em uma LiveData
     */
    @NonNull
    public LiveData<List<Produto>> getAll(SimpleArrayMap<String, String> ordenacao) {
        Query query = queryPadrao;
        for (int i = 0; i < ordenacao.size(); i++) {
            String orderBy = ordenacao.keyAt(i);
            String direcao = ordenacao.valueAt(i) != null ? ordenacao.valueAt(i) : "ASC";
            query = query.orderBy(orderBy, direcao.equals("ASC") ? Query.Direction.ASCENDING : Query.Direction.DESCENDING);
        }
        FirestoreQueryLiveData liveData = new FirestoreQueryLiveData(query);
        return Transformations.map(liveData, new ListaProdutoDeserializer());
    }

    private class ListaProdutoDeserializer implements Function<QuerySnapshot, List<Produto>> {

        @Override
        public List<Produto> apply(QuerySnapshot input) {
            List<Produto> lista = new ArrayList<>();
            for (DocumentSnapshot doc : input.getDocuments()) {
                lista.add(doc.toObject(ProdutoImpl.class));
            }
            return lista;
        }
    }

    private class ProdutoDeserializer implements Function<QuerySnapshot, Produto> {
        @Override
        public ProdutoImpl apply(QuerySnapshot input) {
            return input.getDocuments().isEmpty() ? null : input.getDocuments().get(0).toObject(ProdutoImpl.class);
        }
    }

}
