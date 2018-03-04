package tk.andrielson.carrinhos.androidapp;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import tk.andrielson.carrinhos.androidapp.adapter.ProdutoFirestoreRecyclerAdapter;
import tk.andrielson.carrinhos.androidapp.data.model.Produto;
import tk.andrielson.carrinhos.androidapp.data.model.ProdutoImpl;
import tk.andrielson.carrinhos.androidapp.ui.fragment.ProdutoFragment;

/**
 * Created by Andrielson on 02/03/2018.
 */

public final class DI {
    private DI() {
        //construtor privado para impedir o instanciamento da classe.
    }

    @NonNull
    public static Produto newProduto() {
        return new ProdutoImpl();
    }

    public static RecyclerView.Adapter newProdutoRecyclerViewAdapter(ProdutoFragment.OnListFragmentInteractionListener listener, LifecycleOwner lifecycleOwner) {
        CollectionReference produtosReference = FirebaseFirestore.getInstance()
                .collection("produtos");
        Query query = produtosReference.limit(50);
        // Configure recycler adapter options:
        //  * query is the Query object defined above.
        //  * Chat.class instructs the adapter to convert each DocumentSnapshot to a Chat object
        FirestoreRecyclerOptions<ProdutoImpl> options = new FirestoreRecyclerOptions.Builder<ProdutoImpl>()
                .setQuery(query, ProdutoImpl.class)
                .setLifecycleOwner(lifecycleOwner)
                .build();
        Log.d("DI.RecyclerViewAdapter", "Passou por aqui");
        return new ProdutoFirestoreRecyclerAdapter(options, listener);
    }
}
