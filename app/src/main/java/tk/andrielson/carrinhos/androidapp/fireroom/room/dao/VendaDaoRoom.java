package tk.andrielson.carrinhos.androidapp.fireroom.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import tk.andrielson.carrinhos.androidapp.fireroom.room.entities.VendaRoom;

@Dao
public abstract class VendaDaoRoom {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(VendaRoom... vendas);

    @Update
    public abstract void update(VendaRoom... vendas);

    @Delete
    public abstract void delete(VendaRoom... vendas);

    @Query("SELECT * FROM tb_venda")
    public abstract LiveData<VendaRoom[]> getAll();

    @Query("SELECT a.*, b.*, SUM((c.qt_saiu - c.qt_voltou) * c.valor_item) AS venda_total " +
            "FROM tb_venda a " +
            "INNER JOIN tb_vendedor b USING (vendedor_codigo) " +
            "INNER JOIN tb_item_venda c USING (venda_codigo) " +
            "GROUP BY a.venda_codigo " +
            "ORDER BY a.venda_status ASC, a.venda_data DESC, b.vendedor_nome ASC")
    public abstract LiveData<VendaRoom.VendaComVendedorTotal[]> getAllComVendedor();
}
