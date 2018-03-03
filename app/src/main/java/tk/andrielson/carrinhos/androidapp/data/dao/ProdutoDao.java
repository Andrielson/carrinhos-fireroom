package tk.andrielson.carrinhos.androidapp.data.dao;

import java.util.List;

import tk.andrielson.carrinhos.androidapp.data.model.Produto;

/**
 * Created by Andrielson on 02/03/2018.
 */

public interface ProdutoDao<T extends Produto> {
    long insert(T produto);

    int update(T produto);

    int delete(T produto);

    List<T> getAll();

    T getByCodigo(Long codigo);

    void deleteAll();
}
