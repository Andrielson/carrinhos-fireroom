package tk.andrielson.carrinhos.androidapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import tk.andrielson.carrinhos.androidapp.data.model.Produto;
import tk.andrielson.carrinhos.androidapp.data.model.ProdutoImpl;
import tk.andrielson.carrinhos.androidapp.databinding.FragmentProdutoBinding;
import tk.andrielson.carrinhos.androidapp.ui.fragment.ListaProdutoFragment;
import tk.andrielson.carrinhos.androidapp.ui.viewhandler.ProdutoHandler;

/**
 * Created by Andrielson on 04/03/2018.
 */

public class ProdutoFirestoreRecyclerAdapter extends FirestoreRecyclerAdapter<ProdutoImpl, ProdutoFirestoreRecyclerAdapter.ProdutoViewHolder> {

    private static final String TAG = ProdutoFirestoreRecyclerAdapter.class.getSimpleName();

    private final ListaProdutoFragment.OnListFragmentInteractionListener mListener;

    public ProdutoFirestoreRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ProdutoImpl> options, ListaProdutoFragment.OnListFragmentInteractionListener listener) {
        super(options);
        mListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProdutoViewHolder holder, int position, @NonNull ProdutoImpl model) {
        holder.bind(model, this.mListener);
    }

    @Override
    @NonNull
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(group.getContext());
        FragmentProdutoBinding binding = FragmentProdutoBinding.inflate(layoutInflater, group, false);
        return new ProdutoViewHolder(binding);
    }

    public class ProdutoViewHolder extends RecyclerView.ViewHolder {

        private final String TAG = ProdutoViewHolder.class.getSimpleName();
        private final FragmentProdutoBinding binding;

        public ProdutoViewHolder(FragmentProdutoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final Produto produto, final ListaProdutoFragment.OnListFragmentInteractionListener listener) {
            binding.setProduto(produto);
            binding.setHandler(new ProdutoHandler(listener, produto));
            binding.executePendingBindings();
        }

    }
}
