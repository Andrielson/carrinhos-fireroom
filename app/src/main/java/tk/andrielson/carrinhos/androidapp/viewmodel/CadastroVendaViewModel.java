package tk.andrielson.carrinhos.androidapp.viewmodel;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import tk.andrielson.carrinhos.androidapp.DI;
import tk.andrielson.carrinhos.androidapp.data.dao.ProdutoDao;
import tk.andrielson.carrinhos.androidapp.data.dao.VendedorDao;
import tk.andrielson.carrinhos.androidapp.data.model.ItemVenda;
import tk.andrielson.carrinhos.androidapp.data.model.Produto;
import tk.andrielson.carrinhos.androidapp.data.model.Vendedor;

/**
 * Created by anfesilva on 13/03/2018.
 */
@SuppressWarnings("unchecked")
public class CadastroVendaViewModel extends ViewModel {
    private final MediatorLiveData<List<Produto>> produtos;
    private final MediatorLiveData<List<Vendedor>> vendedores;

    public CadastroVendaViewModel() {
        ProdutoDao produtoDao = DI.newProdutoDao();
        produtos = new MediatorLiveData<>();
        produtos.setValue(null);
        produtos.addSource(produtoDao.getAll(), produtos::setValue);
        VendedorDao vendedorDao = DI.newVendedorDao();
        vendedores = new MediatorLiveData<>();
        vendedores.setValue(null);
        vendedores.addSource(vendedorDao.getAll(), vendedores::setValue);
    }

    public LiveData<List<Produto>> getProdutos() {
        return Transformations.map(produtos, input -> {
            for (Produto p : input) {
                if (!p.getAtivo())
                    input.remove(p);
            }
            return input;
        });
    }

    public LiveData<List<Vendedor>> getVendedores() {
        return Transformations.map(vendedores, input -> {
            for (Vendedor v : input)
                if (!v.getAtivo())
                    input.remove(v);
            return input;
        });
    }

    public LiveData<ItemVenda[]> getItens() {
        return Transformations.map(produtos, input -> {
            List<ItemVenda> lista = new ArrayList<>();
            for (Produto p : input) {
                if (p.getAtivo()) {
                    lista.add(DI.newItemVenda(p));
                }
            }
            return lista.toArray(new ItemVenda[0]);
        });
    }
}
