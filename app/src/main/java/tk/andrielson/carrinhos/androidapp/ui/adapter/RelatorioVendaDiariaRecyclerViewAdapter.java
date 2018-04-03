package tk.andrielson.carrinhos.androidapp.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import tk.andrielson.carrinhos.androidapp.databinding.FragmentRelatorioVendasPorDiaItemBinding;
import tk.andrielson.carrinhos.androidapp.observable.RelatorioVendaPorDia;

public final class RelatorioVendaDiariaRecyclerViewAdapter extends RecyclerView.Adapter<RelatorioVendaDiariaRecyclerViewAdapter.ViewHolder> {

    private List<RelatorioVendaPorDia> lista = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        FragmentRelatorioVendasPorDiaItemBinding binding = FragmentRelatorioVendasPorDiaItemBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(lista.get(position));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void setLista(final List<RelatorioVendaPorDia> novaLista) {
        if (this.lista == null) {
            this.lista = novaLista;
            notifyItemRangeInserted(0, novaLista.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffCallback(novaLista, this.lista));
            lista = novaLista;
            result.dispatchUpdatesTo(this);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final FragmentRelatorioVendasPorDiaItemBinding binding;

        public ViewHolder(FragmentRelatorioVendasPorDiaItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final RelatorioVendaPorDia item) {
            binding.setRelatorio(item);
            binding.executePendingBindings();
        }
    }

    private static final class DiffCallback extends DiffUtil.Callback {
        private final List<RelatorioVendaPorDia> listaNova;
        private final List<RelatorioVendaPorDia> listaAntiga;

        DiffCallback(List<RelatorioVendaPorDia> listaNova, List<RelatorioVendaPorDia> listaAntiga) {
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
            return Objects.equals(listaAntiga.get(oldItemPosition).data.get(), listaNova.get(newItemPosition).data.get());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            RelatorioVendaPorDia v1 = listaAntiga.get(oldItemPosition);
            RelatorioVendaPorDia v2 = listaNova.get(newItemPosition);
            return v1.hashCode() == v2.hashCode();
        }
    }
}
