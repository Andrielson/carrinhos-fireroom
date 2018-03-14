package tk.andrielson.carrinhos.androidapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.widget.Toast;

import tk.andrielson.carrinhos.androidapp.data.dao.VendedorDao;
import tk.andrielson.carrinhos.androidapp.data.model.Vendedor;
import tk.andrielson.carrinhos.androidapp.observable.VendedorObservable;

import static tk.andrielson.carrinhos.androidapp.DI.newVendedorDao;

@SuppressWarnings("unchecked")
public class CadastroVendedorViewModel extends AndroidViewModel {
    private final VendedorDao vendedorDao = newVendedorDao();

    public CadastroVendedorViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<VendedorObservable> getVendedor(@NonNull Long codigo) {
        return Transformations.map((LiveData<Vendedor>) vendedorDao.getByCodigo(codigo), VendedorObservable::new);
    }

    public void salvarVendedor(VendedorObservable observable) {
        if (observable.codigo.get() == null || observable.codigo.get().isEmpty() || observable.codigo.get().equals("0")) {
            vendedorDao.insert(observable.getVendedorModel());
            Toast.makeText(this.getApplication(), "Vendedor adicionado!", Toast.LENGTH_SHORT).show();
        } else {
            vendedorDao.update(observable.getVendedorModel());
            Toast.makeText(this.getApplication(), "Vendedor atualizado!", Toast.LENGTH_SHORT).show();
        }
    }

    public void excluirVendedor(VendedorObservable observable) {
        if (observable.codigo.get() != null && !observable.codigo.get().isEmpty() && !observable.codigo.get().equals("0")) {
            vendedorDao.delete(observable.getVendedorModel());
            Toast.makeText(this.getApplication(), "Vendedor excluído!", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this.getApplication(), "Não é possível excluir um vendedor nulo/vazio!", Toast.LENGTH_SHORT).show();
    }

}
