package tk.andrielson.carrinhos.androidapp.data.dao;

import android.support.annotation.CallSuper;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Created by anfesilva on 07/03/2018.
 */

public abstract class FirestoreDao {
    protected FirebaseFirestore db;
    protected CollectionReference collectionReference;

    protected FirestoreDao() {
        db = FirebaseFirestore.getInstance();
    }
}
