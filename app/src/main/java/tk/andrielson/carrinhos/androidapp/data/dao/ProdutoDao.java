package tk.andrielson.carrinhos.androidapp.data.dao;

import android.arch.lifecycle.LiveData;

import java.util.List;

import tk.andrielson.carrinhos.androidapp.data.model.Produto;

/**
 * Created by Andrielson on 02/03/2018.
 */

public interface ProdutoDao {
    long insert(Produto produto);

    int update(Produto produto);

    int delete(Produto produto);

    LiveData<List<Produto>> getAll();

    LiveData<Produto> getByCodigo(Long codigo);

    void deleteAll();
}
