package tk.andrielson.carrinhos.androidapp.adapter;

import android.support.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import tk.andrielson.carrinhos.androidapp.data.model.ProdutoImpl;
import tk.andrielson.carrinhos.androidapp.ui.adapter.ProdutoRecyclerViewAdapter;
import tk.andrielson.carrinhos.androidapp.ui.fragment.ProdutoFragment;
import tk.andrielson.carrinhos.androidapp.ui.viewholder.ProdutoViewHolder;

/**
 * Created by Andrielson on 06/03/2018.
 */

public class TesteAdapter extends ProdutoRecyclerViewAdapter {

    private final ProdutoFirestoreRecyclerAdapter adapter;

    public TesteAdapter(ProdutoFragment.OnListFragmentInteractionListener mListener) {
        super(mListener);
        CollectionReference produtosReference = FirebaseFirestore.getInstance()
                .collection("produtos");
        Query query = produtosReference.limit(50);
        // Configure recycler adapter options:
        //  * query is the Query object defined above.
        //  * ProdutoImpl.class instructs the adapter to convert each DocumentSnapshot to a ProdutoImpl object
        FirestoreRecyclerOptions<ProdutoImpl> options = new FirestoreRecyclerOptions.Builder<ProdutoImpl>()
                .setQuery(query, ProdutoImpl.class)
//                .setLifecycleOwner(lifecycleOwner)
                .build();
        adapter = new ProdutoFirestoreRecyclerAdapter(options, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoViewHolder holder, int position) {
        adapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return adapter.getItemCount();
    }
}
