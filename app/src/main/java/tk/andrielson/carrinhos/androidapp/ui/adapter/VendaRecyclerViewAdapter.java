package tk.andrielson.carrinhos.androidapp.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import tk.andrielson.carrinhos.androidapp.data.model.Venda;
import tk.andrielson.carrinhos.androidapp.data.model.VendaImpl;
import tk.andrielson.carrinhos.androidapp.databinding.FragmentVendaListaItemBinding;
import tk.andrielson.carrinhos.androidapp.ui.fragment.ListaVendaFragment;

/**
 * Created by Andrielson on 06/03/2018.
 */
//TODO: estudar possibilidade de se tornar uma classe abstrata para todas as entidades
public class VendaRecyclerViewAdapter extends RecyclerView.Adapter<VendaRecyclerViewAdapter.VendaViewHolder> {
    protected final ListaVendaFragment.OnListFragmentInteractionListener mListener;

    protected List<? extends Venda> listaVenda;

    public VendaRecyclerViewAdapter(ListaVendaFragment.OnListFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }

    @Override
    @NonNull
    public VendaViewHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(group.getContext());
        FragmentVendaListaItemBinding binding = FragmentVendaListaItemBinding.inflate(layoutInflater, group, false);
        return new VendaViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VendaViewHolder holder, int position) {
        holder.bind(listaVenda.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return (listaVenda != null) ? listaVenda.size() : 0;
    }

    public void setListaVenda(final List<? extends Venda> novaLista) {
        if (this.listaVenda == null) {
            this.listaVenda = novaLista;
            notifyItemRangeInserted(0, novaLista.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffCallback(novaLista, this.listaVenda));
            listaVenda = novaLista;
            result.dispatchUpdatesTo(this);
        }
    }

    private static final class DiffCallback extends DiffUtil.Callback {
        private final List<? extends Venda> listaNova;
        private final List<? extends Venda> listaAntiga;

        DiffCallback(List<? extends Venda> listaNova, List<? extends Venda> listaAntiga) {
            this.listaNova = listaNova;
            this.listaAntiga = listaAntiga;
        }

        @Override
        public int getOldListSize() {
            return listaAntiga.size();
        }

        @Override
        public int getNewListSize() {
            return listaNova.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            //TODO: implementar método equals nos objetos
            return Objects.equals(listaAntiga.get(oldItemPosition).getCodigo(), listaNova.get(newItemPosition).getCodigo());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            Venda p1 = listaAntiga.get(oldItemPosition);
            Venda p2 = listaNova.get(newItemPosition);
            return p1.hashCode() == p2.hashCode();
        }
    }
    
    public class VendaViewHolder extends RecyclerView.ViewHolder {

        private final String TAG = VendaViewHolder.class.getSimpleName();
        private final FragmentVendaListaItemBinding binding;

        public VendaViewHolder(FragmentVendaListaItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final Venda venda, final ListaVendaFragment.OnListFragmentInteractionListener listener) {
            binding.setVenda((VendaImpl) venda);
//            binding.setHandler(new ListaVendaItemHandler(listener, venda));
            binding.executePendingBindings();
        }

    }
}
