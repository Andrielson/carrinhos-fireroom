package tk.andrielson.carrinhos.androidapp.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Objects;

import tk.andrielson.carrinhos.androidapp.data.model.ItemVenda;
import tk.andrielson.carrinhos.androidapp.data.model.ItemVendaImpl;
import tk.andrielson.carrinhos.androidapp.databinding.FragmentCadastroVendaBinding;
import tk.andrielson.carrinhos.androidapp.databinding.FragmentItemvendaBinding;
import tk.andrielson.carrinhos.androidapp.ui.viewhandler.ItemVendaHandler;

public class ItemVendaRecyclerViewAdapter extends RecyclerView.Adapter<ItemVendaRecyclerViewAdapter.ViewHolder> {

    private ItemVenda[] itens;
    private final FragmentCadastroVendaBinding vendaBinding;

    public ItemVendaRecyclerViewAdapter(FragmentCadastroVendaBinding vendaBinding) {
        this.vendaBinding = vendaBinding;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        FragmentItemvendaBinding binding = FragmentItemvendaBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.bind(itens[position], this.vendaBinding);
    }

    @Override
    public int getItemCount() {
        return itens.length;
    }

    public void setItens(final ItemVenda[] novosItens) {
        if (this.itens == null) {
            this.itens = novosItens;
            notifyItemRangeInserted(0, novosItens.length);
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffCallback(novosItens, this.itens));
            this.itens = novosItens;
            result.dispatchUpdatesTo(this);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final FragmentItemvendaBinding binding;

        public ViewHolder(FragmentItemvendaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final ItemVenda itemVenda, FragmentCadastroVendaBinding vendaBinding) {
            binding.setItemVenda((ItemVendaImpl) itemVenda);
            binding.qtLevou.setTransformationMethod(null);
            binding.qtVoltou.setTransformationMethod(null);
            binding.setHandler(new ItemVendaHandler(binding));
            binding.setVenda(vendaBinding.getVenda());
        }
    }

    private static final class DiffCallback extends DiffUtil.Callback {
        private final ItemVenda[] listaNova;
        private final ItemVenda[] listaAntiga;

        DiffCallback(ItemVenda[] listaNova, ItemVenda[] listaAntiga) {
            this.listaNova = listaNova;
            this.listaAntiga = listaAntiga;
        }

        @Override
        public int getOldListSize() {
            return listaAntiga.length;
        }

        @Override
        public int getNewListSize() {
            return listaNova.length;
        }

        @Override
        public boolean areItemsTheSame(int o, int n) {
            return Objects.equals(listaAntiga[o].getProduto(), listaNova[n].getProduto());
        }

        @Override
        public boolean areContentsTheSame(int o, int n) {
            ItemVenda itv1 = listaAntiga[o];
            ItemVenda itv2 = listaNova[n];
            return itv1.hashCode() == itv2.hashCode();
        }
    }
}
