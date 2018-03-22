package tk.andrielson.carrinhos.androidapp.fireroom.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import tk.andrielson.carrinhos.androidapp.data.model.VendedorImpl;

@Dao
public abstract class VendedorDaoRoom {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(VendedorImpl... vendedores);

    @Update
    public abstract void update(VendedorImpl... vendedores);

    @Delete
    public abstract void delete(VendedorImpl... vendedores);

    @Query("SELECT * FROM tb_vendedor")
    public abstract LiveData<List<VendedorImpl>> getAll();
}
