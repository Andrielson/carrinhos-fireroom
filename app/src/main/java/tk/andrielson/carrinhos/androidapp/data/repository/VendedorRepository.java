package tk.andrielson.carrinhos.androidapp.data.repository;


import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import tk.andrielson.carrinhos.androidapp.data.model.Vendedor;

public interface VendedorRepository {
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
    LiveData<List<Vendedor>> getAll();

    /**
     * Gets by codigo.
     *
     * @param codigo the codigo
     * @return the by codigo
     */
    LiveData<Vendedor> getByCodigo(@NonNull final Long codigo);
}
