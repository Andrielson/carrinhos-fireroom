package tk.andrielson.carrinhos.androidapp.fireroom.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import tk.andrielson.carrinhos.androidapp.fireroom.room.entities.ItemVendaRoom;

@Dao
public abstract class ItemVendaDaoRoom {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(ItemVendaRoom... itens);

    @Update
    public abstract void update(ItemVendaRoom... itens);

    @Delete
    public abstract void delete(ItemVendaRoom... itens);

    @Query("SELECT * FROM tb_item_venda WHERE cod_venda = :cod_venda")
    public abstract LiveData<ItemVendaRoom[]> getItensVenda(Long cod_venda);
}
