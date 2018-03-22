package tk.andrielson.carrinhos.androidapp.fireroom.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import tk.andrielson.carrinhos.androidapp.data.model.ProdutoImpl;

/**
 * Created by anfesilva on 22/03/2018.
 */
@Dao
public abstract class ProdutoDaoRoom {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(ProdutoImpl... produtos);

    @Update
    public abstract void update(ProdutoImpl... produtos);

    @Delete
    public abstract void delete(ProdutoImpl... produtos);

    @Query("SELECT * FROM tb_produto")
    public abstract LiveData<List<ProdutoImpl>> getAll();
}
