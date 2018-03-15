package tk.andrielson.carrinhos.androidapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import tk.andrielson.carrinhos.androidapp.DI;
import tk.andrielson.carrinhos.androidapp.data.dao.ProdutoDao;
import tk.andrielson.carrinhos.androidapp.data.dao.VendaDao;
import tk.andrielson.carrinhos.androidapp.data.model.ItemVenda;
import tk.andrielson.carrinhos.androidapp.data.model.Produto;
import tk.andrielson.carrinhos.androidapp.observable.ItemVendaObservable;
import tk.andrielson.carrinhos.androidapp.observable.VendaObservable;


@SuppressWarnings("unchecked")
public class CadastroVendaViewModel extends ViewModel {
    private final MediatorLiveData<List<ItemVendaObservable>> itensVenda;
    private final VendaObservable vendaObservable = new VendaObservable(DI.newVenda());

    public CadastroVendaViewModel() {
        ProdutoDao produtoDao = DI.newProdutoDao();
        LiveData<List<ItemVendaObservable>> itensProdutosAtivos = Transformations.map((LiveData<List<Produto>>) produtoDao.getAll(), input -> {
            List<ItemVendaObservable> lista = new ArrayList<>();
            if (input != null)
                for (Produto p : input)
                    if (p.getAtivo()) lista.add(new ItemVendaObservable(DI.newItemVenda(p)));
            return lista;
        });
        itensVenda = new MediatorLiveData<>();
        itensVenda.addSource(itensProdutosAtivos, itensVenda::setValue);
    }

    public LiveData<List<ItemVendaObservable>> getItensVenda() {
        return itensVenda;
    }

    public LiveData<List<ItemVendaObservable>> getItensVenda(@NonNull final Long codigo) {
        VendaDao vendaDao = DI.newVendaDao();
        LiveData<List<ItemVenda>> liveData = vendaDao.getItens(codigo);
        itensVenda.addSource(liveData, new Observer<List<ItemVenda>>() {
            @Override
            public void onChanged(@Nullable List<ItemVenda> itensDaVenda) {
                List<ItemVendaObservable> produtosAtivos = itensVenda.getValue();
                if (produtosAtivos != null && itensDaVenda != null) {
                    for (ItemVenda itv : itensDaVenda)
                        for (ItemVendaObservable ito : produtosAtivos) {
                            if (itv.getProduto().getCodigo().equals(Long.valueOf(ito.produto.get().codigo.get())))
                                //remove
                                produtosAtivos.remove(ito);
                            //adiciona
                            produtosAtivos.add(new ItemVendaObservable(itv));
                        }
                    itensVenda.setValue(produtosAtivos);
                }
            }
        });
        return itensVenda;
    }

}
