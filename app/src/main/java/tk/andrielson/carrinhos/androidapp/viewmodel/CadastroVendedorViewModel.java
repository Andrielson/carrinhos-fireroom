package tk.andrielson.carrinhos.androidapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.annotation.Nullable;

import tk.andrielson.carrinhos.androidapp.DI;
import tk.andrielson.carrinhos.androidapp.data.dao.VendedorDao;
import tk.andrielson.carrinhos.androidapp.data.model.Vendedor;

/**
 * Created by Andrielson on 08/03/2018.
 */

public class CadastroVendedorViewModel extends ViewModel {
    private final MutableLiveData<Vendedor> vendedorLiveData;

    public CadastroVendedorViewModel(@Nullable Long codigo) {
        if (codigo == null) {
            vendedorLiveData = new MutableLiveData<>();
            vendedorLiveData.setValue(DI.newVendedor());
        } else {
            VendedorDao vendedorDao = DI.newVendedorDao();
            vendedorLiveData = (MutableLiveData<Vendedor>) vendedorDao.getByCodigo(codigo);
        }
    }

    public LiveData<Vendedor> getVendedor() {
        return vendedorLiveData;
    }

    public void setVendedor(Vendedor vendedor) {
        vendedorLiveData.setValue(vendedor);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        private final Long vendedorCodigo;

        public Factory(@Nullable Long codigo) {
            vendedorCodigo = codigo;
        }

        @Override
        @NonNull
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new CadastroVendedorViewModel(vendedorCodigo);
        }
    }
}
