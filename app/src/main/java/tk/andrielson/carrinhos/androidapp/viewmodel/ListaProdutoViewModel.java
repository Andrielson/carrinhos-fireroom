package tk.andrielson.carrinhos.androidapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import tk.andrielson.carrinhos.androidapp.DI;
import tk.andrielson.carrinhos.androidapp.data.dao.ProdutoDao;
import tk.andrielson.carrinhos.androidapp.data.model.Produto;

/**
 * Created by anfesilva on 07/03/2018.
 */

public class ListaProdutoViewModel extends ViewModel {
    private final MediatorLiveData<List<Produto>> mediatorLiveDataListaProdutos;

    public ListaProdutoViewModel() {
        mediatorLiveDataListaProdutos = new MediatorLiveData<>();
        // Set por padrão null, até carregar os dados do repositório
        mediatorLiveDataListaProdutos.setValue(null);

        ProdutoDao produtoDao = DI.newProdutoDao();
        //noinspection unchecked
        LiveData<List<Produto>> produtos = produtoDao.getAll();
        mediatorLiveDataListaProdutos.addSource(produtos, mediatorLiveDataListaProdutos::setValue);
    }

    public LiveData<List<Produto>> getProdutos() {
        return mediatorLiveDataListaProdutos;
    }
}
