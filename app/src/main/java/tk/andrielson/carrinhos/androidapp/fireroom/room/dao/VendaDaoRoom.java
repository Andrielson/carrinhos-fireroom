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

    @Query("SELECT a.*, b.* FROM tb_venda a INNER JOIN tb_vendedor b ON a.cod_vendedor = b.codigo")
    public abstract LiveData<VendaRoom.VendaTeste[]> getAllComVendedor();
}
