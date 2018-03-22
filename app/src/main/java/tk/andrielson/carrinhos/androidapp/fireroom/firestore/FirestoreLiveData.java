package tk.andrielson.carrinhos.androidapp.fireroom.firestore;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import tk.andrielson.carrinhos.androidapp.data.model.ProdutoImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.room.AppDatabase;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.ProdutoDaoRoom;

public final class FirestoreLiveData extends LiveData<QuerySnapshot> {
    private static final String TAG = FirestoreLiveData.class.getSimpleName();

    private final Query query;
    private ListenerRegistration registration;

    public FirestoreLiveData(Query query) {
        this.query = query;
    }

    @Override
    protected void onActive() {
        if (query != null) {
            registration = query.addSnapshotListener(Executors.newSingleThreadExecutor(), new LiveDataEventListener());
        }
    }

    @Override
    protected void onInactive() {
        if (registration != null) registration.remove();
    }

    private class LiveDataEventListener implements EventListener<QuerySnapshot>, OnCompleteListener<QuerySnapshot> {
        private final ProdutoDaoRoom daoRoom = AppDatabase.getInstancia().produtoDao();

        @Override
        public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {
            if (snapshots != null)
                sincronizaRoom(snapshots.getDocumentChanges());
        }

        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful() && task.getResult() != null)
                sincronizaRoom(task.getResult().getDocumentChanges());
        }

        private void sincronizaRoom(@NonNull List<DocumentChange> documentChangeList) {
            List<ProdutoImpl> adicionados = null;
            List<ProdutoImpl> modificados = null;
            List<ProdutoImpl> excluidos = null;
            for (DocumentChange dc : documentChangeList) {
                //TODO: criar interface mapeadora para as vendas e itens da venda
                ProdutoImpl produto = dc.getDocument().toObject(ProdutoImpl.class);
                switch (dc.getType()) {
                    case ADDED:
                        if (adicionados == null) adicionados = new ArrayList<>();
                        adicionados.add(produto);
                        break;
                    case MODIFIED:
                        if (modificados == null) modificados = new ArrayList<>();
                        modificados.add(produto);
                        break;
                    case REMOVED:
                        if (excluidos == null) excluidos = new ArrayList<>();
                        excluidos.add(produto);
                        break;
                }
            }
            ProdutoImpl[] modelo = new ProdutoImpl[0];
            if (adicionados != null && !adicionados.isEmpty())
                daoRoom.insert(adicionados.toArray(modelo));
            if (modificados != null && !modificados.isEmpty())
                daoRoom.update(modificados.toArray(modelo));
            if (excluidos != null && !excluidos.isEmpty())
                daoRoom.delete(excluidos.toArray(modelo));
        }
    }
}
