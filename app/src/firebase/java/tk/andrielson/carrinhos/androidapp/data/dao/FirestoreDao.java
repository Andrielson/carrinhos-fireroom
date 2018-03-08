package tk.andrielson.carrinhos.androidapp.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.util.ArrayMap;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import org.jetbrains.annotations.Contract;

import java.util.Map;

/**
 * The type Firestore dao.
 */
public abstract class FirestoreDao {

    private static final InnerSingleton viewModel = InnerSingleton.getInstance();
    private static final String COLECAOIDS = "StringIDs";
    private static final String CAMPOID = "ultimo_id";
    protected final FirebaseFirestore db = FirebaseFirestore.getInstance();

    protected CollectionReference collection;

    @NonNull
    protected String getColecaoID(@NonNull String colecao) {
        Map<String, String> map = viewModel.getIDs();
        return (map != null && map.containsKey(colecao)) ? map.get(colecao) : "0";
    }

    protected WriteBatch setColecaoID(@NonNull String colecao, String novoID) {
        WriteBatch batch = db.batch();
        DocumentReference doc = db.collection(COLECAOIDS).document(colecao);
        return batch.update(doc, CAMPOID, novoID);
    }

    private static class InnerSingleton {
        private static final String TAG = InnerSingleton.class.getSimpleName();

        private static class StaticHolder {
            static final InnerSingleton INSTANCE = new InnerSingleton();
        }

        @Contract(pure = true)
        static InnerSingleton getInstance() {
            return StaticHolder.INSTANCE;
        }

        private final LiveData<Map<String, String>> liveDataIDs;

        private Map<String, String> stringIDs;

        private InnerSingleton() {
            FirestoreQueryLiveData queryLiveData = new FirestoreQueryLiveData(FirebaseFirestore.getInstance().collection(COLECAOIDS), true);
            liveDataIDs = Transformations.map(queryLiveData, input -> {
                Map<String, String> map = new ArrayMap<>(1);
                for (DocumentSnapshot doc : input.getDocuments()) {
                    map.put(doc.getId(), doc.getString(CAMPOID));
                }
                return map;
            });
            liveDataIDs.observeForever(map -> {
                stringIDs = map;
                Log.v(TAG, "LiveDataIDs mudou!");
            });
            Log.d(TAG, "Singleton TESTE criado, aparentemente, com sucesso!");
        }

        @Contract(pure = true)
        private Map<String, String> getIDs() {
            return liveDataIDs.getValue();
        }
    }
}
