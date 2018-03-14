package tk.andrielson.carrinhos.androidapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.widget.Toast;

import tk.andrielson.carrinhos.androidapp.data.dao.ProdutoDao;
import tk.andrielson.carrinhos.androidapp.observable.ProdutoObservable;

import static tk.andrielson.carrinhos.androidapp.DI.newProdutoDao;

@SuppressWarnings("unchecked")
public class CadastroProdutoViewModel extends AndroidViewModel {

    private final ProdutoDao produtoDao = newProdutoDao();

    public CadastroProdutoViewModel(@NonNull Application application) {
        super(application);
    }

    public void salvarProduto(ProdutoObservable observable) {
        if (observable.ehNovo()) {
            produtoDao.insert(observable.getProdutoModel());
            Toast.makeText(this.getApplication(), "Produto adicionado!", Toast.LENGTH_SHORT).show();
        } else {
            produtoDao.update(observable.getProdutoModel());
            Toast.makeText(this.getApplication(), "Produto atualizado!", Toast.LENGTH_SHORT).show();
        }
    }

    public void excluirProduto(ProdutoObservable observable) {
        if (!observable.ehNovo()) {
            produtoDao.delete(observable.getProdutoModel());
            Toast.makeText(this.getApplication(), "Produto excluído!", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this.getApplication(), "Não é possível excluir um produto nulo/vazio!", Toast.LENGTH_SHORT).show();
    }
}
