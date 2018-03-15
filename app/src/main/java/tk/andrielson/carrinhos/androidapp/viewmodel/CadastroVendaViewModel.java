package tk.andrielson.carrinhos.androidapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tk.andrielson.carrinhos.androidapp.DI;
import tk.andrielson.carrinhos.androidapp.data.dao.ProdutoDao;
import tk.andrielson.carrinhos.androidapp.data.dao.VendaDao;
import tk.andrielson.carrinhos.androidapp.data.model.ItemVenda;
import tk.andrielson.carrinhos.androidapp.data.model.Produto;
import tk.andrielson.carrinhos.androidapp.data.model.Venda;
import tk.andrielson.carrinhos.androidapp.observable.ItemVendaObservable;
import tk.andrielson.carrinhos.androidapp.observable.VendaObservable;

import static tk.andrielson.carrinhos.androidapp.DI.newProdutoDao;
import static tk.andrielson.carrinhos.androidapp.DI.newVenda;
import static tk.andrielson.carrinhos.androidapp.DI.newVendaDao;


@SuppressWarnings("unchecked")
public class CadastroVendaViewModel extends AndroidViewModel {
    private final MediatorLiveData<List<ItemVendaObservable>> itensVenda;
    private final VendaObservable vendaObservable = new VendaObservable(newVenda());
    private final VendaDao vendaDao = newVendaDao();

    public CadastroVendaViewModel(@NonNull Application application) {
        super(application);
        ProdutoDao produtoDao = newProdutoDao();
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

    public LiveData<List<ItemVendaObservable>> getItensVenda(@NonNull final String strCodigo) {
        Long codigo = Long.valueOf(strCodigo);
        LiveData<List<ItemVenda>> liveData = vendaDao.getItens(codigo);
        itensVenda.addSource(liveData, itensDaVenda -> {
            List<ItemVendaObservable> produtosAtivos = itensVenda.getValue();
            if (produtosAtivos != null && itensDaVenda != null) {
                for (ItemVenda itv : itensDaVenda)
                    for (ItemVendaObservable ito : produtosAtivos) {
                        // Se encontrar esse item entre os observáveis, substitui os valores
                        if (itv.getProduto().getCodigo().equals(Long.valueOf(ito.produto.get().codigo.get())))
                            //remove
                            //FIXME: ConcurrentModificationException
                            produtosAtivos.remove(ito);
                        //adiciona
                        //FIXME: ConcurrentModificationException
                        produtosAtivos.add(new ItemVendaObservable(itv));
                    }
                itensVenda.setValue(produtosAtivos);
            }
        });
        return itensVenda;
    }

    public void salvarVenda(VendaObservable observable) {
        Venda venda = observable.getVendaModel();

        //Remove os itens que não foram vendidos
        //FIXME: ConcurrentModificationException
        for (ItemVenda itv : (List<ItemVenda>) venda.getItens())
            if (itv.getQtVendeu().equals(0))
                venda.getItens().remove(itv);

        if (observable.ehNovo()) {
            vendaDao.insert(venda);
            Toast.makeText(this.getApplication(), "Venda adicionada!", Toast.LENGTH_SHORT).show();
        } else {
            vendaDao.update(venda);
            Toast.makeText(this.getApplication(), "Venda atualizada!", Toast.LENGTH_SHORT).show();
        }
    }

    public void excluirVenda(VendaObservable observable) {
        if (!observable.ehNovo()) {
            vendaDao.delete(observable.getVendaModel());
            Toast.makeText(this.getApplication(), "Venda excluída!", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this.getApplication(), "Não é possível excluir uma venda nula/vazia!", Toast.LENGTH_SHORT).show();
    }
}
