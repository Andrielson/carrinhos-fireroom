package tk.andrielson.carrinhos.androidapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import tk.andrielson.carrinhos.androidapp.DI;
import tk.andrielson.carrinhos.androidapp.data.dao.VendaDao;
import tk.andrielson.carrinhos.androidapp.data.model.Venda;
import tk.andrielson.carrinhos.androidapp.observable.VendaObservable;

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
    }

    public LiveData<List<VendaObservable>> getVendas() {
        return Transformations.map(mediatorLiveDataListaVendas, input -> {
            List<VendaObservable> lista = new ArrayList<>();
            if (input != null)
                for (Venda v : input)
                    lista.add(new VendaObservable(v));
            return lista;
        });
    }
}
