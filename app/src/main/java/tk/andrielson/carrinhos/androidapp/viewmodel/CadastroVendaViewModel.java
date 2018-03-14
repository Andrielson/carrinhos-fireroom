package tk.andrielson.carrinhos.androidapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import tk.andrielson.carrinhos.androidapp.DI;
import tk.andrielson.carrinhos.androidapp.data.dao.ProdutoDao;
import tk.andrielson.carrinhos.androidapp.data.dao.VendedorDao;
import tk.andrielson.carrinhos.androidapp.data.model.Produto;
import tk.andrielson.carrinhos.androidapp.data.model.Vendedor;
import tk.andrielson.carrinhos.androidapp.observable.ItemVendaObservable;
import tk.andrielson.carrinhos.androidapp.observable.VendaObservable;
import tk.andrielson.carrinhos.androidapp.observable.VendedorObservable;

/**
 * Created by anfesilva on 13/03/2018.
 */
@SuppressWarnings("unchecked")
public class CadastroVendaViewModel extends ViewModel {
    private final LiveData<List<VendedorObservable>> vendedoresAtivos;
    private final LiveData<List<ItemVendaObservable>> itensVenda;
    private final VendaObservable vendaObservable = new VendaObservable(DI.newVenda());

    public CadastroVendaViewModel() {
        ProdutoDao produtoDao = DI.newProdutoDao();
        itensVenda = Transformations.map((LiveData<List<Produto>>) produtoDao.getAll(), input -> {
            List<ItemVendaObservable> lista = new ArrayList<>();
            if (input != null)
                for (Produto p : input)
                    if (p.getAtivo()) lista.add(new ItemVendaObservable(DI.newItemVenda(p)));
            return lista;
        });
        VendedorDao vendedorDao = DI.newVendedorDao();
        vendedoresAtivos = Transformations.map((LiveData<List<Vendedor>>) vendedorDao.getAll(), input -> {
            List<VendedorObservable> lista = new ArrayList<>();
            if (input != null)
                for (Vendedor v : input)
                    if (v.getAtivo()) lista.add(new VendedorObservable(v));
            return lista;
        });
    }

    public LiveData<List<ItemVendaObservable>>  getItensVenda() {
        return itensVenda;
    }

    public LiveData<List<VendedorObservable>> getVendedores() {
        return vendedoresAtivos;
    }


    private class TesteObserver implements Observer<List<ItemVendaObservable>> {
        @Override
        public void onChanged(@Nullable List<ItemVendaObservable> itemVendaObservables) {
            if (itemVendaObservables != null && !itemVendaObservables.isEmpty()) {
                vendaObservable.itens.set(itemVendaObservables);
                itensVenda.removeObserver(this);
            }
        }
    }
}
