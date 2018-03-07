package tk.andrielson.carrinhos.androidapp.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import tk.andrielson.carrinhos.androidapp.data.model.Produto;
import tk.andrielson.carrinhos.androidapp.databinding.FragmentProdutoBinding;
import tk.andrielson.carrinhos.androidapp.ui.fragment.ListaProdutoFragment;
import tk.andrielson.carrinhos.androidapp.ui.viewholder.ProdutoViewHolder;

/**
 * Created by Andrielson on 06/03/2018.
 */

public class ProdutoRecyclerViewAdapter extends RecyclerView.Adapter<ProdutoViewHolder> {
    protected final ListaProdutoFragment.OnListFragmentInteractionListener mListener;

    protected List<? extends Produto> listaProduto;

    public ProdutoRecyclerViewAdapter(ListaProdutoFragment.OnListFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }

    @Override
    @NonNull
    public ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(group.getContext());
        FragmentProdutoBinding binding = FragmentProdutoBinding.inflate(layoutInflater, group, false);
        return new ProdutoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoViewHolder holder, int position) {
        holder.bind(listaProduto.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return (listaProduto != null) ? listaProduto.size() : 0;
    }

    public void setListaProduto(final List<? extends Produto> novaLista) {
        if (this.listaProduto == null) {
            this.listaProduto = novaLista;
            notifyItemRangeInserted(0, novaLista.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return listaProduto.size();
                }

                @Override
                public int getNewListSize() {
                    return novaLista.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    //TODO: implementar método equals nos objetos
                    return Objects.equals(listaProduto.get(oldItemPosition).getCodigo(), novaLista.get(newItemPosition).getCodigo());
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Produto p1 = listaProduto.get(oldItemPosition);
                    Produto p2 = novaLista.get(newItemPosition);
                    //TODO: implementar método hashCode nos objetos
                    //return listaProduto.get(oldItemPosition).equals(novaLista.get(newItemPosition));
                    return areItemsTheSame(oldItemPosition, newItemPosition)
                            && Objects.equals(p1.getNome(), p2.getNome())
                            && Objects.equals(p1.getPreco(), p2.getPreco());
                }
            });
            listaProduto = novaLista;
            result.dispatchUpdatesTo(this);
        }
    }
}
