package tk.andrielson.carrinhos.androidapp.data.repository;


import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import tk.andrielson.carrinhos.androidapp.data.model.Vendedor;
import tk.andrielson.carrinhos.androidapp.fireroom.model.VendedorImpl;

public interface VendedorRepository<T extends Vendedor> {
    /**
     * Insert long.
     *
     * @param vendedor the vendedor
     */
    void insert(@NonNull Vendedor vendedor);

    /**
     * Update int.
     *
     * @param vendedor the vendedor
     */
    void update(@NonNull Vendedor vendedor);

    /**
     * Delete int.
     *
     * @param vendedor the vendedor
     */
    void delete(@NonNull Vendedor vendedor);

    /**
     * Gets all.
     *
     * @return the all
     */
    LiveData<List<T>> getAll();

    /**
     * Gets by codigo.
     *
     * @param codigo the codigo
     * @return the by codigo
     */
    LiveData<T> getByCodigo(@NonNull final Long codigo);
}
