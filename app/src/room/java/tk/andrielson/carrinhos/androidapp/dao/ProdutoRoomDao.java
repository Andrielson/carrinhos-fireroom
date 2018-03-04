package tk.andrielson.carrinhos.androidapp.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import tk.andrielson.carrinhos.androidapp.data.dao.ProdutoDao;
import tk.andrielson.carrinhos.androidapp.data.model.ProdutoRoom;

/**
 * Created by Andrielson on 02/03/2018.
 */

@Dao
public abstract class ProdutoRoomDao implements ProdutoDao<ProdutoRoom> {
    @Override
    @Insert
    public abstract long insert(ProdutoRoom produto);

    @Override
    @Update
    public abstract int update(ProdutoRoom produto);

    @Override
    @Delete
    public abstract int delete(ProdutoRoom produto);

    @Override
    @Query("SELECT * FROM tb_produto")
    public abstract List<ProdutoRoom> getAll();

    @Override
    @Query("SELECT * FROM tb_produto WHERE codigo = :codigo")
    public abstract ProdutoRoom getByCodigo(Long codigo);

    @Override
    @Query("DELETE FROM tb_produto")
    public abstract void deleteAll();
}
