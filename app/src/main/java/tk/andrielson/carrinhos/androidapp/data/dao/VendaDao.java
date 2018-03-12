package tk.andrielson.carrinhos.androidapp.data.dao;

import android.arch.lifecycle.LiveData;

import java.util.List;

import tk.andrielson.carrinhos.androidapp.data.model.Venda;

/**
 * Created by anfesilva on 12/03/2018.
 */

public interface VendaDao<T extends Venda> {
    long insert(T venda);

    int update(T venda);

    int delete(T venda);

    LiveData<List<T>> getAll();

    LiveData<T> getByCodigo(Long codigo);
}
