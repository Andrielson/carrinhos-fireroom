package tk.andrielson.carrinhos.androidapp.data.dao;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import tk.andrielson.carrinhos.androidapp.data.model.ProdutoImpl;

/**
 * Created by anfesilva on 07/03/2018.
 */

public final class ProdutoDaoImpl extends FirestoreDao implements ProdutoDao<ProdutoImpl> {

    private static final String COLECAO = ProdutoImpl.COLLECTION;
    private static final String TAG = ProdutoDaoImpl.class.getSimpleName();

    public ProdutoDaoImpl() {
        super();
        collectionReference = db.collection(COLECAO);
    }

    @Override
    public long insert(ProdutoImpl produto) {
        return 0;
    }

    @Override
    public int update(ProdutoImpl produto) {
        return 0;
    }

    @Override
    public int delete(ProdutoImpl produto) {
        return 0;
    }

    @Override
    public List<ProdutoImpl> getAll() {
        final List<ProdutoImpl> lista = new ArrayList<>();
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        lista.add(document.toObject(ProdutoImpl.class));
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        return lista;
    }

    @Override
    public ProdutoImpl getByCodigo(final Long codigo) {
        final ProdutoImpl produto = new ProdutoImpl();
        Query query = collectionReference.whereEqualTo("codigo", codigo);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null || value == null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                Map<String, Object> map = value.getDocuments().get(0).getData();
                produto.setCodigo((Long) map.get(ProdutoImpl.CODIGO));
                produto.setNome((String) map.get(ProdutoImpl.NOME));
                produto.setSigla((String) map.get(ProdutoImpl.SIGLA));
                produto.setPreco((Double) map.get(ProdutoImpl.PRECO));
                produto.setAtivo((Boolean) map.get(ProdutoImpl.ATIVO));
            }
        });
        return produto;
    }

    @Override
    public void deleteAll() {

    }

    public LiveData<List<ProdutoImpl>> listaProdutos() {
        Query query = collectionReference.whereEqualTo("ativo", true);
        FirestoreQueryLiveData liveData = new FirestoreQueryLiveData(query);
        return Transformations.map(liveData, new Deserializer());
    }

    private class Deserializer implements Function<QuerySnapshot, List<ProdutoImpl>> {
        @Override
        public List<ProdutoImpl> apply(QuerySnapshot input) {
            List<ProdutoImpl> lista = new ArrayList<>();
            for (DocumentSnapshot doc : input.getDocuments()) {
                lista.add(doc.toObject(ProdutoImpl.class));
            }
            return lista;
        }
    }
}
