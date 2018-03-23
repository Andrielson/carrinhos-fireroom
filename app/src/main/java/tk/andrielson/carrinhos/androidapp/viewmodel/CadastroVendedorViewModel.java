package tk.andrielson.carrinhos.androidapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.widget.Toast;

import tk.andrielson.carrinhos.androidapp.data.repository.VendedorRepository;
import tk.andrielson.carrinhos.androidapp.observable.VendedorObservable;

import static tk.andrielson.carrinhos.androidapp.DI.newVendedorRepository;

@SuppressWarnings("unchecked")
public class CadastroVendedorViewModel extends AndroidViewModel {

    private final VendedorRepository vendedorRepository = newVendedorRepository();

    public CadastroVendedorViewModel(@NonNull Application application) {
        super(application);
    }

    public void salvarVendedor(VendedorObservable observable) {
        if (observable.ehNovo()) {
            vendedorRepository.insert(observable.getVendedorModel());
            Toast.makeText(this.getApplication(), "Vendedor adicionado!", Toast.LENGTH_SHORT).show();
        } else {
            vendedorRepository.update(observable.getVendedorModel());
            Toast.makeText(this.getApplication(), "Vendedor atualizado!", Toast.LENGTH_SHORT).show();
        }
    }

    public void excluirVendedor(VendedorObservable observable) {
        if (!observable.ehNovo()) {
            vendedorRepository.delete(observable.getVendedorModel());
            Toast.makeText(this.getApplication(), "Vendedor excluído!", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this.getApplication(), "Não é possível excluir um vendedor nulo/vazio!", Toast.LENGTH_SHORT).show();
    }

}
