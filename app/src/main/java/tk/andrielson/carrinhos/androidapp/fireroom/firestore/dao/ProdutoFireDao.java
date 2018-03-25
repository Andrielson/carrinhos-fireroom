package tk.andrielson.carrinhos.androidapp.fireroom.firestore.dao;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.WriteBatch;

import tk.andrielson.carrinhos.androidapp.fireroom.model.ProdutoImpl;

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
        batch.commit().addOnCompleteListener(new OnTaskCompleteListenerPadrao("Novo produto " + id + " adicionado com sucesso!", "Falha ao adicionar o produto " + id, TAG));
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
        batch.commit().addOnCompleteListener(new OnTaskCompleteListenerPadrao("Produto " + id + " atualizado com sucesso!", "Falha ao atualizar o produto " + id, TAG));
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
        batch.commit().addOnCompleteListener(new OnTaskCompleteListenerPadrao("Produto " + id + " removido com sucesso!", "Falha ao remover o produto " + id, TAG));
    }

}
