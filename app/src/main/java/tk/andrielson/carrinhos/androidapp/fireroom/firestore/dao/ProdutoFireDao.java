package tk.andrielson.carrinhos.androidapp.fireroom.firestore.dao;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.WriteBatch;

import tk.andrielson.carrinhos.androidapp.fireroom.model.ProdutoImpl;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;

/**
 * Implementação de ProdutoDao para o banco Firestore.
 */
public final class ProdutoFireDao extends FirestoreDao {

    private static final String COLECAO = ProdutoImpl.COLECAO;
    private static final String TAG = ProdutoFireDao.class.getSimpleName();

    /**
     * O construtor configura a coleção do Firestore que irá utilizar.
     */
    public ProdutoFireDao() {
        super(COLECAO);
    }

    /**
     * Insere um produto no banco de dados e retorna o código gerado.
     *
     * @param produto o produto a ser inserido
     */
    public void insert(@NonNull final ProdutoImpl produto) {
        String ultimoID = getColecaoID(COLECAO);
        produto.setCodigo(Long.valueOf(ultimoID) + 1);
        final String id = getIdFromCodigo(produto.getCodigo());
        DocumentReference novoDocumento = collection.document(id);
        WriteBatch batch = setColecaoID(COLECAO, id);
        batch.set(novoDocumento, produto);
        //TODO: trocar os dois listener por um só OnCompleteListener
        batch.commit().addOnSuccessListener(aVoid -> LogUtil.Log(TAG, "Novo produto " + id + " adicionado com sucesso!", Log.INFO)).addOnFailureListener(e -> {
            LogUtil.Log(TAG, "Falha ao adicionar o produto " + id, Log.ERROR);
            LogUtil.Log(TAG, e.getMessage(), Log.ERROR);
        });
    }

    /**
     * Atualiza as informações de um produto já existente no banco de dados.
     *
     * @param produto o produto a ser atualizado
     */
    public void update(@NonNull final ProdutoImpl produto) {
        final String id = getIdFromCodigo(produto.getCodigo());
        DocumentReference documento = collection.document(id);
        WriteBatch batch = db.batch();
        batch.set(documento, produto);
        //TODO: trocar os dois listener por um só OnCompleteListener
        batch.commit().addOnSuccessListener(aVoid -> LogUtil.Log(TAG, "Produto " + id + " atualizado com sucesso!", Log.INFO)).addOnFailureListener(e -> {
            LogUtil.Log(TAG, "Falha ao atualizar o produto " + id, Log.ERROR);
            LogUtil.Log(TAG, e.getMessage(), Log.ERROR);
        });
    }

    /**
     * Remove um produto do banco de dados
     *
     * @param produto o produto a ser removido
     */
    public void delete(@NonNull final ProdutoImpl produto) {
        final String id = getIdFromCodigo(produto.getCodigo());
        DocumentReference documento = collection.document(id);
        WriteBatch batch = db.batch();
        if (produto.getExcluido())
            // Exclusão lógica
            batch.update(documento, ProdutoImpl.EXCLUIDO, Boolean.TRUE);
        else
            // Exclusão física
            batch.delete(documento);
        //TODO: trocar os dois listener por um só OnCompleteListener
        batch.commit()
                .addOnSuccessListener(aVoid -> LogUtil.Log(TAG, "Produto " + id + " removido com sucesso!", Log.INFO))
                .addOnFailureListener(e -> {
                    LogUtil.Log(TAG, "Falha ao remover o produto " + id, Log.ERROR);
                    LogUtil.Log(TAG, e.getMessage(), Log.ERROR);
                });
    }

}
