package tk.andrielson.carrinhos.androidapp.adapter;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.Locale;

import tk.andrielson.carrinhos.androidapp.R;
import tk.andrielson.carrinhos.androidapp.data.model.ProdutoImpl;
import tk.andrielson.carrinhos.androidapp.ui.fragment.ProdutoFragment;
import tk.andrielson.carrinhos.androidapp.viewholder.ProdutoViewHolder;

/**
 * Created by Andrielson on 04/03/2018.
 */

public class ProdutoFirestoreRecyclerAdapter extends FirestoreRecyclerAdapter<ProdutoImpl, ProdutoViewHolder> {

    private static final String TAG = ProdutoFirestoreRecyclerAdapter.class.getSimpleName();

    private final ProdutoFragment.OnListFragmentInteractionListener mListener;

    public ProdutoFirestoreRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ProdutoImpl> options, ProdutoFragment.OnListFragmentInteractionListener listener) {
        super(options);
        mListener = listener;
        Log.d(TAG, "ProdutoFirestoreRecyclerAdapter");
    }

    @Override
    public void onBindViewHolder(@NonNull final ProdutoViewHolder holder, int position, @NonNull ProdutoImpl model) {
        holder.mItem = model;
        holder.mNomeView.setText(model.getNome());
        holder.mValorView.setText(String.format(Locale.getDefault(), "R$ %.2f", model.getPreco()));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
        Log.d(TAG, "onBindViewHolder");
    }

    @Override
    @NonNull
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {
        View view = LayoutInflater.from(group.getContext())
                .inflate(R.layout.fragment_produto, group, false);
        Log.d(TAG, "onCreateViewHolder");
        return new ProdutoViewHolder(view);
    }
}
