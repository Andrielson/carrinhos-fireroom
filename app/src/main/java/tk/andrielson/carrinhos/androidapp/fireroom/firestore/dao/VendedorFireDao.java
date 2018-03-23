package tk.andrielson.carrinhos.androidapp.fireroom.firestore.dao;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.WriteBatch;

import tk.andrielson.carrinhos.androidapp.fireroom.firestore.collections.VendedorFire;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;

/**
 * Implementação de VendedorDao para o banco Firestore.
 */
public final class VendedorFireDao extends FirestoreDao {
    private static final String COLECAO = VendedorFire.COLECAO;
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
    public void insert(@NonNull final VendedorFire vendedor) {
        String ultimoID = getColecaoID(COLECAO);
        Long novoCodigo = Long.valueOf(ultimoID) + 1;
        vendedor.codigo = novoCodigo;
        final String id = getIdFromCodigo(novoCodigo);
        DocumentReference novoDocumento = collection.document(id);
        WriteBatch batch = setColecaoID(COLECAO, id);
        batch.set(novoDocumento, vendedor);
        //TODO: trocar os dois listener por um só OnCompleteListener
        batch.commit().addOnSuccessListener(aVoid -> LogUtil.Log(TAG, "Novo vendedor " + id + " adicionado com sucesso!", Log.INFO)).addOnFailureListener(e -> {
            LogUtil.Log(TAG, "Falha ao adicionar o vendedor " + id, Log.ERROR);
            LogUtil.Log(TAG, e.getMessage(), Log.ERROR);
        });
    }

    /**
     * Atualiza as informações de um vendedor já existente no banco de dados.
     *
     * @param vendedor o vendedor a ser atualizado
     */
    public void update(@NonNull final VendedorFire vendedor) {
        final String id = getIdFromCodigo(vendedor.codigo);
        DocumentReference documento = collection.document(id);
        WriteBatch batch = db.batch();
        batch.set(documento, vendedor);
        //TODO: trocar os dois listener por um só OnCompleteListener
        batch.commit().addOnSuccessListener(aVoid -> LogUtil.Log(TAG, "Vendedor " + id + " atualizado com sucesso!", Log.INFO)).addOnFailureListener(e -> {
            LogUtil.Log(TAG, "Falha ao atualizar o vendedor " + id, Log.ERROR);
            LogUtil.Log(TAG, e.getMessage(), Log.ERROR);
        });
    }

    /**
     * Remove um vendedor do banco de dados
     *
     * @param vendedor o vendedor a ser removido
     */
    public void delete(@NonNull final VendedorFire vendedor) {
        final String id = getIdFromCodigo(vendedor.codigo);
        DocumentReference documento = collection.document(id);
        WriteBatch batch = db.batch();
        if (vendedor.excluido)
            // Exclusão lógica
            batch.update(documento, VendedorFire.EXCLUIDO, Boolean.TRUE);
        else
            // Exclusão física
            batch.delete(documento);
        //TODO: trocar os dois listener por um só OnCompleteListener
        batch.commit()
                .addOnSuccessListener(aVoid -> LogUtil.Log(TAG, "Vendedor " + id + " removido com sucesso!", Log.INFO))
                .addOnFailureListener(e -> {
                    LogUtil.Log(TAG, "Falha ao remover o vendedor " + id, Log.ERROR);
                    LogUtil.Log(TAG, e.getMessage(), Log.ERROR);
                });
    }
}
