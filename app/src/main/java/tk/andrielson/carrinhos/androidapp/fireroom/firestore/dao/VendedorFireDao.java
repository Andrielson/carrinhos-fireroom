package tk.andrielson.carrinhos.androidapp.fireroom.firestore.dao;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.WriteBatch;

import tk.andrielson.carrinhos.androidapp.fireroom.model.VendedorImpl;

/**
 * Implementação de VendedorDao para o banco Firestore.
 */
public final class VendedorFireDao extends FirestoreDao {
    private static final String COLECAO = VendedorImpl.COLECAO;
    private static final String TAG = VendedorFireDao.class.getSimpleName();

    /**
     * O construtor configura a coleção do Firestore que irá utilizar e implementa a query padrão.
     */
    public VendedorFireDao() {
        super(COLECAO);
    }

    /**
     * Insere um vendedor no banco de dados e retorna o código gerado.
     *
     * @param vendedor o vendedor a ser inserido
     */
    public void insert(@NonNull final VendedorImpl vendedor) {
        String ultimoID = getColecaoID(COLECAO);
        vendedor.setCodigo(Long.valueOf(ultimoID) + 1);
        final String id = getIdFromCodigo(vendedor.getCodigo());
        DocumentReference novoDocumento = collection.document(id);
        WriteBatch batch = setColecaoID(COLECAO, id);
        batch.set(novoDocumento, vendedor);
        batch.commit().addOnCompleteListener(new OnTaskCompleteListenerPadrao("Novo vendedor " + id + " adicionado com sucesso!", "Falha ao adicionar o vendedor " + id, TAG));
    }

    /**
     * Atualiza as informações de um vendedor já existente no banco de dados.
     *
     * @param vendedor o vendedor a ser atualizado
     */
    public void update(@NonNull final VendedorImpl vendedor) {
        final String id = getIdFromCodigo(vendedor.getCodigo());
        DocumentReference documento = collection.document(id);
        WriteBatch batch = db.batch();
        batch.set(documento, vendedor);
        batch.commit().addOnCompleteListener(new OnTaskCompleteListenerPadrao("Vendedor " + id + " atualizado com sucesso!", "Falha ao atualizar o vendedor " + id, TAG));
    }

    /**
     * Remove um vendedor do banco de dados
     *
     * @param vendedor o vendedor a ser removido
     */
    public void delete(@NonNull final VendedorImpl vendedor) {
        final String id = getIdFromCodigo(vendedor.getCodigo());
        DocumentReference documento = collection.document(id);
        WriteBatch batch = db.batch();
        if (vendedor.getExcluido())
            // Exclusão lógica
            batch.update(documento, VendedorImpl.EXCLUIDO, Boolean.TRUE);
        else
            // Exclusão física
            batch.delete(documento);
        batch.commit().addOnCompleteListener(new OnTaskCompleteListenerPadrao("Vendedor " + id + " removido com sucesso!", "Falha ao remover o vendedor " + id, TAG));
    }
}
