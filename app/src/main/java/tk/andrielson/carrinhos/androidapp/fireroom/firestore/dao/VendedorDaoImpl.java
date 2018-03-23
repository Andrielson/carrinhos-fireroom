package tk.andrielson.carrinhos.androidapp.fireroom.firestore.dao;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.WriteBatch;

import tk.andrielson.carrinhos.androidapp.fireroom.firestore.collections.VendedorFirestore;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;

/**
 * Implementação de VendedorDao para o banco Firestore.
 */
public final class VendedorDaoImpl extends FirestoreDao {
    private static final String COLECAO = VendedorFirestore.COLECAO;
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
    public long insert(@NonNull VendedorFirestore vendedor) {
        String ultimoID = getColecaoID(COLECAO);
        Long novoCodigo = Long.valueOf(ultimoID) + 1;
        vendedor.codigo = novoCodigo;
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
    public int update(@NonNull VendedorFirestore vendedor) {
        final String id = getIdFromCodigo(vendedor.codigo);
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
    public int delete(@NonNull VendedorFirestore vendedor) {
        final String id = getIdFromCodigo(vendedor.codigo);
        DocumentReference documento = collection.document(id);
        WriteBatch batch = db.batch();
        //Depois de implementar as vendas, pesquisar se o vendedor possui relação com alguma venda
        //se tiver, marcar a flag excluído.
        //batch.update(documento, VendedorFirestore.EXCLUIDO, true);
        batch.delete(documento);
        batch.commit()
                .addOnSuccessListener(aVoid -> LogUtil.Log(TAG, "Vendedor " + id + " removido com sucesso!", Log.INFO))
                .addOnFailureListener(e -> {
                    LogUtil.Log(TAG, "Falha ao remover o vendedor " + id, Log.ERROR);
                    LogUtil.Log(TAG, e.getMessage(), Log.ERROR);
                });
        return 0;
    }
}
