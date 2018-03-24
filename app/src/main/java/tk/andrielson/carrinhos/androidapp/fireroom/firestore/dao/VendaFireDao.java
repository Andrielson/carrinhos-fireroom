package tk.andrielson.carrinhos.androidapp.fireroom.firestore.dao;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.concurrent.Executors;

import tk.andrielson.carrinhos.androidapp.fireroom.model.ItemVendaImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.model.VendaImpl;

public final class VendaFireDao extends FirestoreDao {
    private static final String COLECAO = VendaImpl.COLECAO;
    private static final String TAG = VendaFireDao.class.getSimpleName();

    public VendaFireDao() {
        super(COLECAO);
    }

    public void insert(@NonNull final VendaImpl venda, @NonNull final ItemVendaImpl[] itens) {
        String ultimoID = getColecaoID(COLECAO);
        venda.setCodigo(Long.valueOf(ultimoID) + 1);
        final String idVenda = getIdFromCodigo(venda.getCodigo());
        DocumentReference novoDocumento = collection.document(idVenda);
        WriteBatch batch = setColecaoID(COLECAO, idVenda);
        batch.set(novoDocumento, venda);
        //TODO: o batch possui um limite máximo de 500 operações. Se a venda tiver mais de 495 itens, pode dar ruim.
        CollectionReference itensRef = db.collection(String.format("/%s/%s/%s", COLECAO, idVenda, ItemVendaImpl.COLECAO));
        for (ItemVendaImpl itv : itens)
            batch.set(itensRef.document(itv.getRefProduto().getId()), itv);
        batch.commit().addOnCompleteListener(new OnTaskCompleteListenerPadrao("Nova venda " + venda.getCodigo() + " adicionada com sucesso!", "Falha ao adicionar a venda " + venda.getCodigo(), TAG));
    }

    public void update(@NonNull final VendaImpl venda, @NonNull final ItemVendaImpl[] itens) {
        CollectionReference itensRef = db.collection(String.format("/%s/%s/%s", COLECAO, FirestoreDao.getIdFromCodigo(venda.getCodigo()), ItemVendaImpl.COLECAO));
        itensRef.get().addOnSuccessListener(Executors.newSingleThreadExecutor(), snapshots -> {
            if (snapshots != null) {
                WriteBatch batch = db.batch();
                // Apaga todos os itens
                for (DocumentSnapshot doc : snapshots.getDocuments())
                    batch.delete(doc.getReference());
                // Salva a venda
                batch.set(collection.document(getIdFromCodigo(venda.getCodigo())), venda);
                // Insere os novos itens
                for (ItemVendaImpl itv : itens)
                    batch.set(itensRef.document(itv.getRefProduto().getId()), itv);
                // Commita o batch
                batch.commit().addOnCompleteListener(new OnTaskCompleteListenerPadrao("Venda " + venda.getCodigo() + " atualizada com sucesso!", "Falha ao atualizar a venda " + venda.getCodigo(), TAG));
            }
        });
    }

    public void delete(@NonNull final VendaImpl venda) {
        CollectionReference itensRef = db.collection(String.format("/%s/%s/%s", COLECAO, FirestoreDao.getIdFromCodigo(venda.getCodigo()), ItemVendaImpl.COLECAO));
        itensRef.get().addOnSuccessListener(Executors.newSingleThreadExecutor(), snapshots -> {
            if (snapshots != null) {
                WriteBatch batch = db.batch();
                // Remove todos os itens
                for (DocumentSnapshot doc : snapshots.getDocuments())
                    batch.delete(doc.getReference());
                // Remove a venda
                batch.delete(collection.document(getIdFromCodigo(venda.getCodigo())));
                // Commita o batch
                batch.commit().addOnCompleteListener(new OnTaskCompleteListenerPadrao("Venda " + venda.getCodigo() + " removida com sucesso!", "Falha ao remover a venda " + venda.getCodigo(), TAG));
            }
        });
    }
}
