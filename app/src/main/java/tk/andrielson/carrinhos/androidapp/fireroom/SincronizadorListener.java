package tk.andrielson.carrinhos.androidapp.fireroom;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import tk.andrielson.carrinhos.androidapp.data.model.AbsEntidadePadrao;

@SuppressWarnings("unchecked")
public final class SincronizadorListener<E extends AbsEntidadePadrao> implements EventListener<QuerySnapshot>, OnCompleteListener<QuerySnapshot> {
    private SincronizadorDaoRoom daoRoom;
    private Class<E> tipoEntidade;

    public SincronizadorListener(SincronizadorDaoRoom daoRoom, Class<E> tipoEntidade) {
        this.daoRoom = daoRoom;
        this.tipoEntidade = tipoEntidade;
    }

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
        List<E> adicionados = null;
        List<E> modificados = null;
        List<E> excluidos = null;
        for (DocumentChange dc : documentChangeList) {
            //TODO: criar interface mapeadora para as vendas e itens da venda
            E objeto = dc.getDocument().toObject(tipoEntidade);
            switch (dc.getType()) {
                case ADDED:
                    if (adicionados == null) adicionados = new ArrayList<>();
                    adicionados.add(objeto);
                    break;
                case MODIFIED:
                    if (modificados == null) modificados = new ArrayList<>();
                    modificados.add(objeto);
                    break;
                case REMOVED:
                    if (excluidos == null) excluidos = new ArrayList<>();
                    excluidos.add(objeto);
                    break;
            }
        }
        E[] modelo = (E[]) new Object[0];
        if (adicionados != null && !adicionados.isEmpty())
            daoRoom.insert(adicionados.toArray(modelo));
        if (modificados != null && !modificados.isEmpty())
            daoRoom.update(modificados.toArray(modelo));
        if (excluidos != null && !excluidos.isEmpty())
            daoRoom.delete(excluidos.toArray(modelo));
    }
}
