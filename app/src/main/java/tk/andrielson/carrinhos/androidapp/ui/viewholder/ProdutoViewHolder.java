package tk.andrielson.carrinhos.androidapp.ui.viewholder;

import android.support.v7.widget.RecyclerView;

import tk.andrielson.carrinhos.androidapp.data.model.Produto;
import tk.andrielson.carrinhos.androidapp.databinding.FragmentProdutoBinding;
import tk.andrielson.carrinhos.androidapp.ui.fragment.ListaProdutoFragment;
import tk.andrielson.carrinhos.androidapp.ui.viewhandler.ProdutoHandler;

/**
 * Created by Andrielson on 04/03/2018.
 */

public class ProdutoViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = ProdutoViewHolder.class.getSimpleName();
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
