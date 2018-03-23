package tk.andrielson.carrinhos.androidapp.fireroom.firestore.dao;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.util.ArrayMap;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import org.jetbrains.annotations.Contract;

import java.util.Map;

import tk.andrielson.carrinhos.androidapp.fireroom.firestore.FirestoreQueryLiveData;
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

    protected FirestoreDao(String colecao) {
        collection = db.collection(colecao);
    }

    @NonNull
    protected String getColecaoID(@NonNull String colecao) {
        Map<String, String> map = viewModel.getIDs();
        return (map != null && map.containsKey(colecao)) ? map.get(colecao) : "0";
    }

    protected WriteBatch setColecaoID(@NonNull String colecao, @NonNull String novoID) {
        WriteBatch batch = db.batch();
        // FIXME: quando não há o campo da coleção, gera erro. Verificar se compensa criar
        return batch.set(db.collection(COLECAOIDS).document(colecao), mapID(novoID));
//        return batch.update(db.collection(COLECAOIDS).document(colecao), CAMPOID, novoID);
    }

    private Map<String, String> mapID(@NonNull String novoID) {
        Map<String, String> map = new ArrayMap<>(1);
        map.put(CAMPOID, novoID);
        return map;
    }

    public static String getIdFromCodigo(@NonNull Long codigo) {
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
            liveDataIDs.observeForever(map -> LogUtil.Log(TAG, "Lista de IDs das coleções atualizada!", Log.INFO));
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

    protected class OnTaskCompleteListenerPadrao implements OnCompleteListener<Void> {
        private final String msgSucesso;
        private final String msgFalha;
        private final String TAG;

        OnTaskCompleteListenerPadrao(String msgSucesso, String msgFalha, String TAG) {
            this.msgSucesso = msgSucesso;
            this.msgFalha = msgFalha;
            this.TAG = TAG;
        }

        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful())
                LogUtil.Log(TAG, this.msgSucesso, Log.INFO);
            else {
                LogUtil.Log(TAG, this.msgFalha, Log.ERROR);
                if (task.getException() != null)
                    LogUtil.Log(TAG, task.getException().getMessage(), Log.ERROR);
            }
        }
    }
}
