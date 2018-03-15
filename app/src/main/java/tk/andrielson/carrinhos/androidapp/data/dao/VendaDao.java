package tk.andrielson.carrinhos.androidapp.data.dao;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import tk.andrielson.carrinhos.androidapp.data.model.ItemVenda;
import tk.andrielson.carrinhos.androidapp.data.model.Venda;

/**
 * Created by anfesilva on 12/03/2018.
 */

public interface VendaDao<T extends Venda, I extends ItemVenda> {
    long insert(@NonNull T venda);

    int update(@NonNull T venda);

    int delete(@NonNull T venda);

    LiveData<List<T>> getAll();

    LiveData<T> getByCodigo(@NonNull Long codigo);

    LiveData<List<I>> getItens(@NonNull Long codigo);

}
