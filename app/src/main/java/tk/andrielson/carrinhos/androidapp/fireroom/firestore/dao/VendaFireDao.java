package tk.andrielson.carrinhos.androidapp.fireroom.firestore.dao;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.concurrent.Executors;

import tk.andrielson.carrinhos.androidapp.fireroom.firestore.collections.ItemVendaFire;
import tk.andrielson.carrinhos.androidapp.fireroom.firestore.collections.VendaFire;

public final class VendaFireDao extends FirestoreDao {
    private static final String COLECAO = VendaFire.COLECAO;
    private static final String TAG = VendaFireDao.class.getSimpleName();

    public VendaFireDao() {
        super(COLECAO);
    }

    public void insert(@NonNull final VendaFire venda, @NonNull final ItemVendaFire[] itens) {
        String ultimoID = getColecaoID(COLECAO);
        Long novoCodigo = Long.valueOf(ultimoID) + 1;
        venda.codigo = novoCodigo;
        final String idVenda = getIdFromCodigo(novoCodigo);
        DocumentReference novoDocumento = collection.document(idVenda);
        WriteBatch batch = setColecaoID(COLECAO, idVenda);
        batch.set(novoDocumento, venda);
        //TODO: o batch possui um limite máximo de 500 operações. Se a venda tiver mais de 495 itens, pode dar ruim.
        CollectionReference itensRef = db.collection(String.format("/%s/%s/%s", COLECAO, idVenda, ItemVendaFire.COLECAO));
        for (ItemVendaFire itv : itens)
            batch.set(itensRef.document(itv.produto.getId()), itv);
        batch.commit().addOnCompleteListener(new OnTaskCompleteListenerPadrao("Nova venda " + idVenda + " adicionada com sucesso!", "Falha ao adicionar a venda " + idVenda, TAG));
    }

    public void update(@NonNull final VendaFire venda, @NonNull final ItemVendaFire[] itens) {
        CollectionReference itensRef = db.collection(String.format("/%s/%s/%s", COLECAO, FirestoreDao.getIdFromCodigo(venda.codigo), ItemVendaFire.COLECAO));
        itensRef.get().addOnSuccessListener(Executors.newSingleThreadExecutor(), snapshots -> {
            if (snapshots != null) {
                WriteBatch batch = db.batch();
                // Apaga todos os itens
                for (DocumentSnapshot doc : snapshots.getDocuments())
                    batch.delete(doc.getReference());
                // Salva a venda
                batch.set(collection.document(getIdFromCodigo(venda.codigo)), venda);
                // Insere os novos itens
                for (ItemVendaFire itv : itens)
                    batch.set(itensRef.document(itv.produto.getId()), itv);
                // Commita o batch
                batch.commit().addOnCompleteListener(new OnTaskCompleteListenerPadrao("Venda " + venda.codigo + " atualizada com sucesso!", "Falha ao atualizar a venda " + venda.codigo, TAG));
            }
        });
    }

    public void delete(@NonNull final VendaFire venda) {
        CollectionReference itensRef = db.collection(String.format("/%s/%s/%s", COLECAO, FirestoreDao.getIdFromCodigo(venda.codigo), ItemVendaFire.COLECAO));
        itensRef.get().addOnSuccessListener(Executors.newSingleThreadExecutor(), snapshots -> {
            if (snapshots != null) {
                WriteBatch batch = db.batch();
                // Remove todos os itens
                for (DocumentSnapshot doc : snapshots.getDocuments())
                    batch.delete(doc.getReference());
                // Remove a venda
                batch.delete(collection.document(getIdFromCodigo(venda.codigo)));
                // Commita o batch
                batch.commit().addOnCompleteListener(new OnTaskCompleteListenerPadrao("Venda " + venda.codigo + " removida com sucesso!", "Falha ao remover a venda " + venda.codigo, TAG));
            }
        });
    }
}
