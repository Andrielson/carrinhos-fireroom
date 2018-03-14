package tk.andrielson.carrinhos.androidapp.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import tk.andrielson.carrinhos.androidapp.data.model.Vendedor;
import tk.andrielson.carrinhos.androidapp.data.observable.VendedorObservable;
import tk.andrielson.carrinhos.androidapp.databinding.FragmentVendedorListaItemBinding;
import tk.andrielson.carrinhos.androidapp.ui.fragment.ListaVendedorFragment;
import tk.andrielson.carrinhos.androidapp.ui.viewhandler.ListaVendedorItemHandler;

/**
 * Created by Andrielson on 06/03/2018.
 */
//TODO: estudar possibilidade de se tornar uma classe abstrata para todas as entidades
public class VendedorRecyclerViewAdapter extends RecyclerView.Adapter<VendedorRecyclerViewAdapter.VendedorViewHolder> {
    protected final ListaVendedorFragment.OnListFragmentInteractionListener mListener;

    protected List<VendedorObservable> listaVendedor;

    public VendedorRecyclerViewAdapter(ListaVendedorFragment.OnListFragmentInteractionListener mListener) {
        this.mListener = mListener;
    }

    @Override
    @NonNull
    public VendedorViewHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(group.getContext());
        FragmentVendedorListaItemBinding binding = FragmentVendedorListaItemBinding.inflate(layoutInflater, group, false);
        return new VendedorViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VendedorViewHolder holder, int position) {
        holder.bind(listaVendedor.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return (listaVendedor != null) ? listaVendedor.size() : 0;
    }

    public void setListaVendedor(final List<VendedorObservable> novaLista) {
        if (this.listaVendedor == null) {
            this.listaVendedor = novaLista;
            notifyItemRangeInserted(0, novaLista.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffCallback(novaLista, this.listaVendedor));
            listaVendedor = novaLista;
            result.dispatchUpdatesTo(this);
        }
    }

    private static final class DiffCallback extends DiffUtil.Callback {
        private final List<VendedorObservable> listaNova;
        private final List<VendedorObservable> listaAntiga;

        DiffCallback(List<VendedorObservable> listaNova, List<VendedorObservable> listaAntiga) {
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
            return Objects.equals(listaAntiga.get(oldItemPosition).codigo.get(), listaNova.get(newItemPosition).codigo.get());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            VendedorObservable p1 = listaAntiga.get(oldItemPosition);
            VendedorObservable p2 = listaNova.get(newItemPosition);
            return p1.hashCode() == p2.hashCode();
        }
    }
    
    public class VendedorViewHolder extends RecyclerView.ViewHolder {

        private final String TAG = VendedorViewHolder.class.getSimpleName();
        private final FragmentVendedorListaItemBinding binding;

        public VendedorViewHolder(FragmentVendedorListaItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final VendedorObservable observable, final ListaVendedorFragment.OnListFragmentInteractionListener listener) {
            binding.setVendedor(observable);
            binding.setHandler(new ListaVendedorItemHandler(listener, observable.getVendedorModel()));
            binding.executePendingBindings();
        }

    }
}
