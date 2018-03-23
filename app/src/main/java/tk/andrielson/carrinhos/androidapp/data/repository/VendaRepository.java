package tk.andrielson.carrinhos.androidapp.data.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import tk.andrielson.carrinhos.androidapp.data.model.ItemVenda;
import tk.andrielson.carrinhos.androidapp.data.model.Venda;

public interface VendaRepository {
    void insert(@NonNull Venda venda);

    void update(@NonNull Venda venda);

    void delete(@NonNull Venda venda);

    LiveData<List<Venda>> getAll();

    LiveData<Venda> getByCodigo(@NonNull Long codigo);

    LiveData<List<ItemVenda>> getItens(@NonNull Long codigo);
}
