package tk.andrielson.carrinhos.androidapp.fireroom.firestore.dao;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.WriteBatch;

import tk.andrielson.carrinhos.androidapp.fireroom.firestore.collections.ProdutoFirestore;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;

/**
 * Implementação de ProdutoDao para o banco Firestore.
 */
public final class ProdutoDaoImpl extends FirestoreDao {

    private static final String COLECAO = ProdutoFirestore.COLECAO;
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
    public long insert(ProdutoFirestore produto) {
        String ultimoID = getColecaoID(COLECAO);
        Long novoCodigo = Long.valueOf(ultimoID) + 1;
        produto.codigo = novoCodigo;
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
    public int update(ProdutoFirestore produto) {
        final String id = getIdFromCodigo(produto.codigo);
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
    public int delete(ProdutoFirestore produto) {
        final String id = getIdFromCodigo(produto.codigo);
        DocumentReference documento = collection.document(id);
        WriteBatch batch = db.batch();
        //Depois de implementar as vendas, pesquisar se o produto possui relação com alguma venda
        //se tiver, marcar a flag excluído.
        //batch.update(documento, ProdutoFirestore.EXCLUIDO, true);
        batch.delete(documento);
        batch.commit()
                .addOnSuccessListener(aVoid -> LogUtil.Log(TAG, "Produto " + id + " removido com sucesso!", Log.INFO))
                .addOnFailureListener(e -> {
                    LogUtil.Log(TAG, "Falha ao remover o produto " + id, Log.ERROR);
                    LogUtil.Log(TAG, e.getMessage(), Log.ERROR);
                });
        return 0;
    }

}
