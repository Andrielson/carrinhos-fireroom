package tk.andrielson.carrinhos.androidapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.annotation.Nullable;

import tk.andrielson.carrinhos.androidapp.DI;
import tk.andrielson.carrinhos.androidapp.data.dao.ProdutoDao;
import tk.andrielson.carrinhos.androidapp.data.model.Produto;

/**
 * Created by Andrielson on 08/03/2018.
 */

public class CadastroProdutoViewModel extends ViewModel {
    private final MutableLiveData<Produto> produtoLiveData;

    public CadastroProdutoViewModel(@Nullable Long codigo) {
        if (codigo == null) {
            produtoLiveData = new MutableLiveData<>();
        } else {
            ProdutoDao produtoDao = DI.newProdutoDao();
            produtoLiveData = (MutableLiveData<Produto>) produtoDao.getByCodigo(codigo);
        }
    }

    public LiveData<Produto> getProduto() {
        return produtoLiveData;
    }

    public void setProduto(Produto produto) {
        produtoLiveData.setValue(produto);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final Long produtoCodigo;

        public Factory(@Nullable Long codigo) {
            produtoCodigo = codigo;
        }

        @Override
        @NonNull
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new CadastroProdutoViewModel(produtoCodigo);
        }
    }
}
