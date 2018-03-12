package tk.andrielson.carrinhos.androidapp.data.dao;

import android.annotation.SuppressLint;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.util.ArrayMap;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tk.andrielson.carrinhos.androidapp.data.model.AbsEntidadePadrao;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;

/**
 * The type Firestore dao.
 */
@SuppressLint("DefaultLocale")
public abstract class FirestoreDao {

    private static final InnerSingleton viewModel = InnerSingleton.getInstance();
    private static final String COLECAOIDS = "StringIDs";
    private static final String CAMPOID = "ultimo_id";
    protected final FirebaseFirestore db = FirebaseFirestore.getInstance();

    protected CollectionReference collection;
    protected final Query queryPadrao;

    protected FirestoreDao(String colecao) {
        collection = db.collection(colecao);
        queryPadrao = collection;
    }

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

    protected String getIdFromCodigo(@NonNull Long codigo) {
        return String.format("%018d", codigo);
    }

    private static class InnerSingleton {
        private static final String TAG = InnerSingleton.class.getSimpleName();
        private final LiveData<Map<String, String>> liveDataIDs;

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
                LogUtil.Log(TAG, "Lista de IDs das coleções atualizada!", Log.INFO);
            });
        }

        @Contract(pure = true)
        static InnerSingleton getInstance() {
            return StaticHolder.INSTANCE;
        }

        @Contract(pure = true)
        private Map<String, String> getIDs() {
            return liveDataIDs.getValue();
        }

        private static class StaticHolder {
            static final InnerSingleton INSTANCE = new InnerSingleton();
        }
    }

    protected class ListaDeserializer<T extends AbsEntidadePadrao> implements Function<QuerySnapshot, List<T>> {
        private final Class<T> tipo;

        public ListaDeserializer(Class<T> tipo) {
            this.tipo = tipo;
        }

        @Override
        public List<T> apply(QuerySnapshot input) {
            List<T> lista = new ArrayList<>();
            for (DocumentSnapshot doc : input.getDocuments()) {
                lista.add(doc.toObject(tipo));
            }
            return lista;
        }
    }

    protected class Deserializer<T extends AbsEntidadePadrao> implements Function<QuerySnapshot, T> {
        private final Class<T> tipo;

        public Deserializer(Class<T> tipo) {
            this.tipo = tipo;
        }

        @Override
        public T apply(QuerySnapshot input) {
            return input.getDocuments().isEmpty() ? null : input.getDocuments().get(0).toObject(tipo);
        }
    }
}
