package tk.andrielson.carrinhos.androidapp.data.dao;

import android.arch.lifecycle.LiveData;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Created by anfesilva on 07/03/2018.
 */

public class FirestoreQueryLiveData extends LiveData<QuerySnapshot> {
    private static final String TAG = FirestoreQueryLiveData.class.getSimpleName();

    private final Query query;
    private final TesteEventListener listener;

    public FirestoreQueryLiveData(Query query) {
        this.query = query;
        this.listener = new TesteEventListener();
    }

    @Override
    protected void onActive() {
        query.addSnapshotListener(listener);
    }

    private class TesteEventListener implements EventListener<QuerySnapshot> {
        @Override
        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
            setValue(documentSnapshots);
        }
    }
}
