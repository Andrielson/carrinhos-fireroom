package tk.andrielson.carrinhos.androidapp.data.dao;

import java.util.List;

import tk.andrielson.carrinhos.androidapp.data.model.Vendedor;

/**
 * Created by Andrielson on 02/03/2018.
 */

public interface VendedorDao {
    long insert(Vendedor vendedor);

    int update(Vendedor vendedor);

    int delete(Vendedor vendedor);

    List<Vendedor> getAll();

    Vendedor getByCodigo(Long codigo);

    void deleteAll();
}
