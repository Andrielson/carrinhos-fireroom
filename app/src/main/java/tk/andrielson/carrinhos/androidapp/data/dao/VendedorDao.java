package tk.andrielson.carrinhos.androidapp.data.dao;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import tk.andrielson.carrinhos.androidapp.data.model.Vendedor;

/**
 * The interface Vendedor dao.
 */
public interface VendedorDao<T extends Vendedor> {
    /**
     * Insert long.
     *
     * @param vendedor the vendedor
     * @return the long
     */
    long insert(@NonNull T vendedor);

    /**
     * Update int.
     *
     * @param vendedor the vendedor
     * @return the int
     */
    int update(@NonNull T vendedor);

    /**
     * Delete int.
     *
     * @param vendedor the vendedor
     * @return the int
     */
    int delete(@NonNull T vendedor);

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
