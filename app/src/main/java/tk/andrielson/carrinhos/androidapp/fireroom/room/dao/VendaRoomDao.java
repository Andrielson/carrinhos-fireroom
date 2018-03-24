package tk.andrielson.carrinhos.androidapp.fireroom.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.support.annotation.NonNull;

import tk.andrielson.carrinhos.androidapp.fireroom.room.entities.VendaRoom;

@Dao
public abstract class VendaRoomDao {
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

    @Query("SELECT tb.venda_data, SUM(total) AS valor_total, SUM(comissao) AS valor_comissao, SUM(pago) AS valor_pago " +
            "FROM (" +
            "SELECT venda_data, SUM((b.qt_saiu - b.qt_voltou) * b.valor_item) AS total, " +
            " SUM((b.qt_saiu - b.qt_voltou) * b.valor_item) * a.venda_comissao/100 AS comissao, " +
            " SUM((b.qt_saiu - b.qt_voltou) * b.valor_item) * (1 - a.venda_comissao/100) AS pago " +
            "FROM tb_venda a " +
            "INNER JOIN tb_item_venda b USING(venda_codigo) " +
            "WHERE venda_data BETWEEN :inicio AND :fim " +
            "GROUP BY venda_data, venda_codigo) tb " +
            "GROUP BY tb.venda_data " +
            "ORDER BY tb.venda_data DESC")
    public abstract LiveData<VendaRoom.VendasPorDia[]> getVendasDiarias(@NonNull String inicio, @NonNull String fim);
}
