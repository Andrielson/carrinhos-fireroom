package tk.andrielson.carrinhos.androidapp.data.dao;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Firestore dao.
 */
public abstract class FirestoreDao {
    /**
     * The Db.
     */
    protected FirebaseFirestore db;
    /**
     * The Collection.
     */
    protected CollectionReference collection;

    private Teste viewModel = Teste.getInstance();

    /**
     * Instantiates a new Firestore dao.
     */
    protected FirestoreDao() {
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    protected String getColecaoID(@NonNull String colecao) {
        return viewModel.getIDs().get(colecao);
    }

    @NonNull
    public static LiveData<Map<String, String>> loadUltimosIDs() {
        CollectionReference collectionReference = FirebaseFirestore.getInstance().collection("StringIDs");
        FirestoreQueryLiveData liveData = new FirestoreQueryLiveData(collectionReference);
        return Transformations.map(liveData, new Deserializer());
    }

    private static class Deserializer implements Function<QuerySnapshot, Map<String, String>> {
        @Override
        public Map<String, String> apply(QuerySnapshot input) {
            Map<String, String> map = new HashMap<>(4, 1);
            for (DocumentSnapshot doc : input.getDocuments()) {
                map.put(doc.getId(), doc.getString("ultimo_id"));
            }
            return map;
        }
    }

}
