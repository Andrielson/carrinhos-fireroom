package tk.andrielson.carrinhos.androidapp.data.dao;

import android.arch.lifecycle.LiveData;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.Executors;

/**
 * Created by anfesilva on 07/03/2018.
 */

public class FirestoreQueryLiveData extends LiveData<QuerySnapshot> {
    private static final String TAG = FirestoreQueryLiveData.class.getSimpleName();

    private final Query query;
    private final LiveDataEventListener listener;

    public FirestoreQueryLiveData(Query query) {
        this.query = query;
        this.listener = new LiveDataEventListener();
    }

    @Override
    protected void onActive() {
        query.addSnapshotListener(Executors.newSingleThreadExecutor(), listener);
    }

    private class LiveDataEventListener implements EventListener<QuerySnapshot> {
        @Override
        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
            postValue(documentSnapshots);
        }
    }
}
