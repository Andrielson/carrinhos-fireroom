package tk.andrielson.carrinhos.androidapp.fireroom.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import tk.andrielson.carrinhos.androidapp.fireroom.model.ProdutoImpl;

@Dao
public abstract class ProdutoRoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(ProdutoImpl... produtos);

    @Update
    public abstract void update(ProdutoImpl... produtos);

    @Delete
    public abstract void delete(ProdutoImpl... produtos);

    @Query("SELECT * FROM tb_produto ORDER BY produto_nome")
    public abstract LiveData<List<ProdutoImpl>> getAllOrderByNome();

    @Query("SELECT * FROM tb_produto ORDER BY produto_codigo")
    public abstract LiveData<List<ProdutoImpl>> getAllOrderByCodigo();

    @Query("SELECT EXISTS (SELECT * FROM tb_item_venda WHERE itv_produto_codigo = :codigo)")
    public abstract boolean produtoPossuiVendas(Long codigo);
}
