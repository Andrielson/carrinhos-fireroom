package tk.andrielson.carrinhos.androidapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import tk.andrielson.carrinhos.androidapp.DI;
import tk.andrielson.carrinhos.androidapp.data.dao.ProdutoDao;
import tk.andrielson.carrinhos.androidapp.data.dao.VendedorDao;
import tk.andrielson.carrinhos.androidapp.data.model.Produto;
import tk.andrielson.carrinhos.androidapp.data.model.Vendedor;
import tk.andrielson.carrinhos.androidapp.observable.ItemVendaObservable;
import tk.andrielson.carrinhos.androidapp.observable.VendedorObservable;

/**
 * Created by anfesilva on 13/03/2018.
 */
@SuppressWarnings("unchecked")
public class CadastroVendaViewModel extends ViewModel {
    private final LiveData<List<VendedorObservable>> vendedoresAtivos;
    private final LiveData<ItemVendaObservable[]> itensVenda;

    public CadastroVendaViewModel() {
        ProdutoDao produtoDao = DI.newProdutoDao();
        itensVenda = Transformations.map((LiveData<List<Produto>>) produtoDao.getAll(), input -> {
            List<ItemVendaObservable> lista = new ArrayList<>();
            if (input != null)
                for (Produto p : input)
                    if (p.getAtivo()) lista.add(new ItemVendaObservable(DI.newItemVenda(p)));
            return lista.toArray(new ItemVendaObservable[lista.size()]);
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

    public LiveData<List<VendedorObservable>> getVendedores() {
        return vendedoresAtivos;
    }

    public LiveData<ItemVendaObservable[]> getItensVenda() {
        return itensVenda;
    }
}
