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
    private final LiveDataEventListener listener;
    private final boolean mainThread;
    private ListenerRegistration registration;

    public FirestoreQueryLiveData(Query query) {
        this(query, false);
    }

    public FirestoreQueryLiveData(Query query, boolean mainThread) {
        this.query = query;
        this.task = null;
        this.mainThread = mainThread;
        listener = new LiveDataEventListener(mainThread);
    }

    public FirestoreQueryLiveData(Task<QuerySnapshot> task) {
        this(task, false);
    }

    public FirestoreQueryLiveData(Task<QuerySnapshot> task, boolean mainThread) {
        this.task = task;
        this.query = null;
        this.mainThread = mainThread;
        listener = new LiveDataEventListener(mainThread);
    }

    @Override
    protected void onActive() {
        if (query != null)
            registration = mainThread ? query.addSnapshotListener(listener) : query.addSnapshotListener(Executors.newSingleThreadExecutor(), listener);
        Task t;
        if (task != null)
            t = mainThread ? task.addOnCompleteListener(listener) : task.addOnCompleteListener(Executors.newSingleThreadExecutor(), listener);
    }

    @Override
    protected void onInactive() {
        if (registration != null) registration.remove();
    }

    private class LiveDataEventListener implements EventListener<QuerySnapshot>, OnCompleteListener<QuerySnapshot> {
        private final boolean mainThread;

        LiveDataEventListener(boolean mainThread) {
            this.mainThread = mainThread;
        }

        @Override
        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
            if (mainThread)
                setValue(documentSnapshots);
            else
                postValue(documentSnapshots);
        }

        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (mainThread)
                setValue(task.getResult());
            else
                postValue(task.getResult());
        }
    }
}
