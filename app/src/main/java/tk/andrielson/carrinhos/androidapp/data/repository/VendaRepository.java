package tk.andrielson.carrinhos.androidapp.data.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import tk.andrielson.carrinhos.androidapp.data.model.ItemVenda;
import tk.andrielson.carrinhos.androidapp.data.model.Venda;

public interface VendaRepository<V extends Venda, I extends ItemVenda> {
    void insert(@NonNull Venda venda);

    void update(@NonNull Venda venda);

    void delete(@NonNull Venda venda);

    LiveData<List<V>> getAll();

    LiveData<V> getByCodigo(@NonNull Long codigo);

    LiveData<List<I>> getItens(@NonNull Long codigo);
}
