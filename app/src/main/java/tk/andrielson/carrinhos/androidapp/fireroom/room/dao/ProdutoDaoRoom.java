package tk.andrielson.carrinhos.androidapp.fireroom.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import tk.andrielson.carrinhos.androidapp.fireroom.room.entities.ProdutoRoom;

@Dao
public abstract class ProdutoDaoRoom {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(ProdutoRoom... produtos);

    @Update
    public abstract void update(ProdutoRoom... produtos);

    @Delete
    public abstract void delete(ProdutoRoom... produtos);

    @Query("SELECT * FROM tb_produto ORDER BY produto_nome")
    public abstract LiveData<ProdutoRoom[]> getAll();
}
