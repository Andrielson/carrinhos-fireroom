package tk.andrielson.carrinhos.androidapp.fireroom.room.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Relation;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;

import tk.andrielson.carrinhos.androidapp.data.model.ItemVenda;
import tk.andrielson.carrinhos.androidapp.fireroom.model.ItemVendaImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.model.ProdutoImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.model.VendaImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.model.VendedorImpl;

@Dao
public abstract class VendaRoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(VendaImpl... vendas);

    @Update
    public abstract void update(VendaImpl... vendas);

    @Delete
    public abstract void delete(VendaImpl... vendas);

    @Query("SELECT * FROM tb_venda")
    public abstract LiveData<List<VendaImpl>> getAll();

    @Query("SELECT * " +
            "FROM tb_venda " +
            "JOIN tb_vendedor ON (venda_vendedor_codigo = vendedor_codigo) " +
            "GROUP BY venda_codigo " +
            "ORDER BY venda_status ASC, venda_data DESC, vendedor_nome ASC")
    public abstract LiveData<VendaComVendedorTotal[]> getAllComVendedor();

    @Query("SELECT venda_data, SUM(venda_valor_total) AS valor_total, SUM(venda_valor_comissao) AS valor_comissao, SUM(venda_valor_pago) AS valor_pago " +
            "FROM tb_venda WHERE venda_data BETWEEN :inicio AND :fim " +
            "GROUP BY venda_data " +
            "ORDER BY venda_data DESC")
    public abstract LiveData<VendasPorData[]> getVendasDiarias(Date inicio, Date fim);

    @Query("SELECT substr(venda_data,1,7) AS venda_data, SUM(venda_valor_total) AS valor_total, SUM(venda_valor_comissao) AS valor_comissao, SUM(venda_valor_pago) AS valor_pago " +
            "FROM tb_venda WHERE venda_data BETWEEN :inicio AND :fim " +
            "GROUP BY substr(venda_data,1,7) " +
            "ORDER BY substr(venda_data,1,7) DESC")
    public abstract LiveData<VendasPorData[]> getVendasMensais(Date inicio, Date fim);

    @Query("SELECT * FROM tb_item_venda " +
            "JOIN tb_produto ON (itv_produto_codigo = produto_codigo) " +
            "WHERE itv_venda_codigo = :codigo " +
            "ORDER BY produto_codigo ASC")
    public abstract LiveData<ItemComProduto[]> getItensComProduto(Long codigo);

    @Transaction
    @Query("SELECT * FROM tb_venda")
    public abstract LiveData<VendaBackup[]> getVendasBackup();

    @Query("SELECT SUM(venda_valor_total) AS valor_total, SUM(venda_valor_comissao) AS valor_comissao, SUM(venda_valor_pago) AS valor_pago " +
            "FROM tb_venda WHERE venda_data BETWEEN :inicio AND :fim ")
    public abstract LiveData<TotaisVendas> getTotaisVendas(Date inicio, Date fim);

    @Query("SELECT SUM(venda_valor_total) AS valor_total, SUM(venda_valor_comissao) AS valor_comissao, SUM(venda_valor_pago) AS valor_pago, b.* " +
            "FROM tb_venda a JOIN tb_vendedor b ON (a.venda_vendedor_codigo = b.vendedor_codigo) " +
            "WHERE venda_data BETWEEN :inicio AND :fim " +
            "GROUP BY a.venda_vendedor_codigo " +
            "ORDER BY valor_total DESC LIMIT 1")
    public abstract LiveData<TotaisVendasComVendedor> getTotaisVendasComVendedor(Date inicio, Date fim);

    public static class TotaisVendas {
        @ColumnInfo(name = "valor_total")
        public Long valorTotal;
        @ColumnInfo(name = "valor_pago")
        public Long valorPago;
        @ColumnInfo(name = "valor_comissao")
        public Long valorComissao;
    }

    public static class VendasPorData extends TotaisVendas {
        @ColumnInfo(name = "venda_data")
        public String data;
    }

    public static class TotaisVendasComVendedor extends TotaisVendas {
        @Embedded
        public VendedorImpl vendedor;
    }

    public static class VendaComVendedorTotal {
        @Embedded
        public VendaImpl venda;
        @Embedded
        public VendedorImpl vendedor;

        public VendaImpl getModel() {
            if (venda != null)
                venda.setVendedor(vendedor);
            return venda;
        }
    }

    public static class ItemComProduto {
        @Embedded
        public ItemVendaImpl item;
        @Embedded
        public ProdutoImpl produto;

        public ItemVendaImpl getModel() {
            if (item != null)
                item.setProduto(produto);
            return item;
        }
    }

    public static class VendaBackup {
        @Relation(parentColumn = "venda_codigo", entityColumn = "itv_venda_codigo")
        public List<ItemVendaImpl> itens;
        @Embedded
        VendaImpl venda;

        public VendaImpl getModel() {
            if (venda != null)
                venda.setItens(itens.toArray(new ItemVenda[0]));
            return venda;
        }
    }
}
