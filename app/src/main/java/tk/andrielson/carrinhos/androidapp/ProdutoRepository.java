package tk.andrielson.carrinhos.androidapp;

import android.arch.lifecycle.MediatorLiveData;

import java.util.List;

import tk.andrielson.carrinhos.androidapp.data.dao.ProdutoDao;
import tk.andrielson.carrinhos.androidapp.data.model.Produto;

/**
 * Created by Andrielson on 07/03/2018.
 */

public class ProdutoRepository {
    private static ProdutoRepository sInstance;
    private MediatorLiveData<List<Produto>> mediatorLiveDataListaProdutos;

    private ProdutoRepository() {
        mediatorLiveDataListaProdutos = new MediatorLiveData<>();

        ProdutoDao produtoDao = DI.newProdutoDao();

        mediatorLiveDataListaProdutos.addSource(produtoDao.getAll(), (List<Produto> produtos) -> mediatorLiveDataListaProdutos.postValue(produtos));
    }
}
