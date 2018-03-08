package tk.andrielson.carrinhos.androidapp.data.dao;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.Executors;

/**
 * Created by anfesilva on 07/03/2018.
 */

public class FirestoreQueryLiveData extends LiveData<QuerySnapshot> {
    private static final String TAG = FirestoreQueryLiveData.class.getSimpleName();

    private final Query query;
    private final Task<QuerySnapshot> task;
    private final LiveDataEventListener listener = new LiveDataEventListener();
    private ListenerRegistration registration;

    public FirestoreQueryLiveData(Query query) {
        this.query = query;
        this.task = null;
    }

    public FirestoreQueryLiveData(Task<QuerySnapshot> task) {
        this.task = task;
        this.query = null;
    }

    @Override
    protected void onActive() {
        /*if (query != null)
            registration = query.addSnapshotListener(Executors.newSingleThreadExecutor(), listener);
        if (task != null) task.addOnCompleteListener(Executors.newSingleThreadExecutor(), listener);*/
        if (query != null)
            registration = query.addSnapshotListener(listener);
        if (task != null) task.addOnCompleteListener(listener);
    }

    @Override
    protected void onInactive() {
        if (registration != null) registration.remove();
    }

    //TODO: implementar flag para diferenciar se deve executar setValue ou postValue
    private class LiveDataEventListener implements EventListener<QuerySnapshot>, OnCompleteListener<QuerySnapshot> {
        @Override
        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
            setValue(documentSnapshots);
        }

        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            postValue(task.getResult());
        }
    }
}
