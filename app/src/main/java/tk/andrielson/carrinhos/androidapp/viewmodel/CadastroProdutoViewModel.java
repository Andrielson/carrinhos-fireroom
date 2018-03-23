package tk.andrielson.carrinhos.androidapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.widget.Toast;

import tk.andrielson.carrinhos.androidapp.data.repository.ProdutoRepository;
import tk.andrielson.carrinhos.androidapp.observable.ProdutoObservable;

import static tk.andrielson.carrinhos.androidapp.DI.newProdutoRepository;

public class CadastroProdutoViewModel extends AndroidViewModel {

    private final ProdutoRepository produtoRepository = newProdutoRepository();

    public CadastroProdutoViewModel(@NonNull Application application) {
        super(application);
    }

    public void salvarProduto(ProdutoObservable observable) {
        if (observable.ehNovo()) {
            produtoRepository.insert(observable.getProdutoModel());
            Toast.makeText(this.getApplication(), "Produto adicionado!", Toast.LENGTH_SHORT).show();
        } else {
            produtoRepository.update(observable.getProdutoModel());
            Toast.makeText(this.getApplication(), "Produto atualizado!", Toast.LENGTH_SHORT).show();
        }
    }

    public void excluirProduto(ProdutoObservable observable) {
        if (!observable.ehNovo()) {
            produtoRepository.delete(observable.getProdutoModel());
            Toast.makeText(this.getApplication(), "Produto excluído!", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this.getApplication(), "Não é possível excluir um produto nulo/vazio!", Toast.LENGTH_SHORT).show();
    }
}
