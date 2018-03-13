package tk.andrielson.carrinhos.androidapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import tk.andrielson.carrinhos.androidapp.DI;
import tk.andrielson.carrinhos.androidapp.data.dao.VendaDao;
import tk.andrielson.carrinhos.androidapp.data.dao.VendaDaoImpl;
import tk.andrielson.carrinhos.androidapp.data.model.ItemVendaImpl;
import tk.andrielson.carrinhos.androidapp.data.model.Venda;

/**
 * Created by anfesilva on 07/03/2018.
 */

public class ListaVendaViewModel extends ViewModel {
    private final MediatorLiveData<List<Venda>> mediatorLiveDataListaVendas;

    public ListaVendaViewModel() {
        mediatorLiveDataListaVendas = new MediatorLiveData<>();
        // Set por padrão null, até carregar os dados do repositório
        mediatorLiveDataListaVendas.setValue(null);

        VendaDao vendaDao = DI.newVendaDao();
        //noinspection unchecked
        LiveData<List<Venda>> vendas = vendaDao.getAll();
        mediatorLiveDataListaVendas.addSource(vendas, mediatorLiveDataListaVendas::setValue);
        VendaDaoImpl d = new VendaDaoImpl();
        LiveData<List<ItemVendaImpl>> itens = d.getItens(null);
        itens.observeForever(itemVendas -> {

        });
    }

    public LiveData<List<Venda>> getVendas() {
        return mediatorLiveDataListaVendas;
    }
}
