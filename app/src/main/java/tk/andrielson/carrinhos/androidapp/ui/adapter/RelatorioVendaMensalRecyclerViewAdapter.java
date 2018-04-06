package tk.andrielson.carrinhos.androidapp.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import tk.andrielson.carrinhos.androidapp.databinding.FragmentRelatorioVendasMensalItemBinding;
import tk.andrielson.carrinhos.androidapp.observable.RelatorioVendaPorDia;
import tk.andrielson.carrinhos.androidapp.observable.RelatorioVendaPorMes;

public final class RelatorioVendaMensalRecyclerViewAdapter extends RecyclerView.Adapter<RelatorioVendaMensalRecyclerViewAdapter.ViewHolder> {

    private List<RelatorioVendaPorMes> lista = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        FragmentRelatorioVendasMensalItemBinding binding = FragmentRelatorioVendasMensalItemBinding.inflate(layoutInflater, parent, false);
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

    public void setLista(final List<RelatorioVendaPorMes> novaLista) {
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
        private final FragmentRelatorioVendasMensalItemBinding binding;

        public ViewHolder(FragmentRelatorioVendasMensalItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(final RelatorioVendaPorMes item) {
            binding.setRelatorio(item);
            binding.executePendingBindings();
        }
    }

    private static final class DiffCallback extends DiffUtil.Callback {
        private final List<RelatorioVendaPorMes> listaNova;
        private final List<RelatorioVendaPorMes> listaAntiga;

        DiffCallback(List<RelatorioVendaPorMes> listaNova, List<RelatorioVendaPorMes> listaAntiga) {
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
            RelatorioVendaPorMes v1 = listaAntiga.get(oldItemPosition);
            RelatorioVendaPorMes v2 = listaNova.get(newItemPosition);
            return v1.hashCode() == v2.hashCode();
        }
    }
}
