package tk.andrielson.carrinhos.androidapp.data.dao;

import android.arch.lifecycle.LiveData;

import java.util.List;

import tk.andrielson.carrinhos.androidapp.data.model.Vendedor;

/**
 * The interface Vendedor dao.
 */
public interface VendedorDao {
    /**
     * Insert long.
     *
     * @param vendedor the vendedor
     * @return the long
     */
    long insert(Vendedor vendedor);

    /**
     * Update int.
     *
     * @param vendedor the vendedor
     * @return the int
     */
    int update(Vendedor vendedor);

    /**
     * Delete int.
     *
     * @param vendedor the vendedor
     * @return the int
     */
    int delete(Vendedor vendedor);

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
    LiveData<Vendedor> getByCodigo(Long codigo);

}
