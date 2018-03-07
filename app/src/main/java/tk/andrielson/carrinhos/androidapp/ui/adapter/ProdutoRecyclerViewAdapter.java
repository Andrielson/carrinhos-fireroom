package tk.andrielson.carrinhos.androidapp.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import tk.andrielson.carrinhos.androidapp.databinding.FragmentProdutoBinding;
import tk.andrielson.carrinhos.androidapp.ui.fragment.ProdutoFragment;
import tk.andrielson.carrinhos.androidapp.ui.viewholder.ProdutoViewHolder;

/**
 * Created by Andrielson on 06/03/2018.
 */

public abstract class ProdutoRecyclerViewAdapter extends RecyclerView.Adapter<ProdutoViewHolder> {
    private final ProdutoFragment.OnListFragmentInteractionListener mListener;

    public ProdutoRecyclerViewAdapter(ProdutoFragment.OnListFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }

    @Override
    @NonNull
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(group.getContext());
        FragmentProdutoBinding binding = FragmentProdutoBinding.inflate(layoutInflater, group, false);
        return new ProdutoViewHolder(binding);
    }
}
