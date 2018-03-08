package tk.andrielson.carrinhos.androidapp.adapter;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.ChangeEventListener;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import tk.andrielson.carrinhos.androidapp.data.model.ProdutoImpl;
import tk.andrielson.carrinhos.androidapp.ui.adapter.ProdutoRecyclerViewAdapter;
import tk.andrielson.carrinhos.androidapp.ui.fragment.ListaProdutoFragment;

/**
 * Created by Andrielson on 06/03/2018.
 */

public class TesteAdapter extends ProdutoRecyclerViewAdapter implements ChangeEventListener, LifecycleObserver {

    private final FirestoreRecyclerAdapter<ProdutoImpl, ProdutoViewHolder> firestoreRecyclerAdapter;

    public TesteAdapter(final ListaProdutoFragment.OnListFragmentInteractionListener mListener, LifecycleOwner lifecycleOwner) {
        super(mListener);
        CollectionReference produtosReference = FirebaseFirestore.getInstance()
                .collection("produtos");
        Query query = produtosReference.limit(50);
        // Configure recycler firestoreRecyclerAdapter options:
        //  * query is the Query object defined above.
        //  * ProdutoImpl.class instructs the firestoreRecyclerAdapter to convert each DocumentSnapshot to a ProdutoImpl object
        FirestoreRecyclerOptions<ProdutoImpl> options = new FirestoreRecyclerOptions.Builder<ProdutoImpl>()
                .setQuery(query, ProdutoImpl.class)
                .setLifecycleOwner(lifecycleOwner)
                .build();
        firestoreRecyclerAdapter = new FirestoreRecyclerAdapter<ProdutoImpl, ProdutoViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProdutoViewHolder holder, int position, @NonNull ProdutoImpl model) {
                holder.bind(model, mListener);
            }

            @NonNull
            @Override
            public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return TesteAdapter.this.createViewHolder(parent, viewType);
            }
        };
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoViewHolder holder, int position) {
        firestoreRecyclerAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return firestoreRecyclerAdapter.getItemCount();
    }


    /**
     * Start listening for database changes and populate the adapter.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void startListening() {
        firestoreRecyclerAdapter.startListening();
    }

    /**
     * Stop listening for database changes and clear all items in the adapter.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void stopListening() {
        firestoreRecyclerAdapter.stopListening();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void cleanup(LifecycleOwner source) {
        source.getLifecycle().removeObserver(this);
    }

    @Override
    public void onChildChanged(@NonNull ChangeEventType type, @NonNull DocumentSnapshot snapshot, int newIndex, int oldIndex) {
        firestoreRecyclerAdapter.onChildChanged(type, snapshot, newIndex, oldIndex);
    }

    @Override
    public void onDataChanged() {
        firestoreRecyclerAdapter.onDataChanged();
    }

    @Override
    public void onError(@NonNull FirebaseFirestoreException e) {
        firestoreRecyclerAdapter.onError(e);
    }
}
