package tk.andrielson.carrinhos.androidapp.data.dao;

import android.arch.lifecycle.LiveData;

import java.util.List;

import tk.andrielson.carrinhos.androidapp.data.model.Vendedor;

/**
 * Created by Andrielson on 02/03/2018.
 */

public interface VendedorDao {
    long insert(Vendedor vendedor);

    int update(Vendedor vendedor);

    int delete(Vendedor vendedor);

    LiveData<List<Vendedor>> getAll();

    LiveData<Vendedor> getByCodigo(Long codigo);

    void deleteAll();
}
