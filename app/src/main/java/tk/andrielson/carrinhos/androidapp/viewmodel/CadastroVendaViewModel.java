package tk.andrielson.carrinhos.androidapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
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
    private static final String TAG = CadastroVendaViewModel.class.getSimpleName();
    private final MediatorLiveData<List<ItemVendaObservable>> itensVenda;
    private final VendaObservable vendaObservable = new VendaObservable(newVenda());
    private final VendaDao vendaDao = newVendaDao();

    public CadastroVendaViewModel(@NonNull Application application) {
        super(application);
        ProdutoDao produtoDao = newProdutoDao();
        SimpleArrayMap<String, String> ordenacao = new SimpleArrayMap<>(1);
        ordenacao.put("codigo", "ASC");
        LiveData<List<ItemVendaObservable>> itensProdutosAtivos = Transformations.map((LiveData<List<Produto>>) produtoDao.getAll(ordenacao), input -> {
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
                int i, j;
                for (i = itensDaVenda.size() - 1; i >= 0; i--) {
                    ItemVenda itv = itensDaVenda.get(i);
                    for (j = produtosAtivos.size() - 1; j >= 0; j--) {
                        ItemVendaObservable ito = produtosAtivos.get(j);
                        // Se encontrar esse item entre os observáveis, substitui os valores
                        if (itv.getProduto().getCodigo().equals(Long.valueOf(ito.produto.get().codigo.get()))) {
                            //remove
                            produtosAtivos.remove(j);
                            break;
                        }
                    }
                    //adiciona
                    produtosAtivos.add(new ItemVendaObservable(itv));
                }
                Collections.sort(produtosAtivos, (o1, o2) -> {
                    String strCod1 = o1.produto.get().codigo.get();
                    String strCod2 = o2.produto.get().codigo.get();
                    long cod1 = strCod1 != null && !strCod1.isEmpty() ? Long.valueOf(strCod1) : 0L;
                    long cod2 = strCod2 != null && !strCod2.isEmpty() ? Long.valueOf(strCod2) : 0L;
                    return Long.compare(cod1, cod2);
                });
                itensVenda.setValue(produtosAtivos);
            }
        });
        return itensVenda;
    }

    public void salvarVenda(VendaObservable observable) {
        Venda venda = observable.getVendaModel();

        //Remove os itens que não foram levados
        for (int i = venda.getItens().size() - 1; i >= 0; i--) {
            ItemVenda itv = (ItemVenda) venda.getItens().get(i);
            if (itv.getQtSaiu().equals(0))
                venda.getItens().remove(itv);
        }

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
