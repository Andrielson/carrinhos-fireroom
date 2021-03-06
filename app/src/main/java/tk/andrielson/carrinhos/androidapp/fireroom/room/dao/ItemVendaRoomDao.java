package tk.andrielson.carrinhos.androidapp.fireroom.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

import tk.andrielson.carrinhos.androidapp.fireroom.model.ItemVendaImpl;

@Dao
public abstract class ItemVendaRoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(ItemVendaImpl... itens);

    @Update
    public abstract void update(ItemVendaImpl... itens);

    @Delete
    public abstract void delete(ItemVendaImpl... itens);

    @Query("DELETE FROM tb_item_venda WHERE itv_venda_codigo = :cod_venda")
    public abstract void delete(Long cod_venda);

    @Query("DELETE FROM tb_item_venda WHERE itv_venda_codigo IN (:vendas)")
    public abstract void delete(List<Long> vendas);

    @Transaction
    public void replace(ItemVendaImpl[] itens, Long cod_venda) {
        delete(cod_venda);
        insert(itens);
    }

    @Query("SELECT * FROM tb_item_venda WHERE itv_venda_codigo = :cod_venda")
    public abstract LiveData<ItemVendaImpl[]> getItensVenda(Long cod_venda);
}
