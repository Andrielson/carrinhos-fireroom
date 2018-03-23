package tk.andrielson.carrinhos.androidapp.fireroom.room.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import tk.andrielson.carrinhos.androidapp.fireroom.firestore.collections.ItemVendaFirestore;
import tk.andrielson.carrinhos.androidapp.fireroom.model.ItemVendaImpl;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "tb_item_venda",
        foreignKeys = {
                @ForeignKey(entity = VendaRoom.class, parentColumns = "codigo", childColumns = "cod_venda", onDelete = CASCADE, deferred = true),
                @ForeignKey(entity = ProdutoRoom.class, parentColumns = "codigo", childColumns = "cod_produto", deferred = true)},
        indices = {
                @Index(name = "venda_produto", value = {"cod_venda", "cod_produto"}, unique = true),
                @Index(name = "idx_item_venda", value = {"cod_venda"})})
public final class ItemVendaRoom {
    @PrimaryKey(autoGenerate = true)
    public Long id;
    @ColumnInfo(name = "cod_venda")
    public Long venda;
    @ColumnInfo(name = "cod_produto")
    public Long produto;
    @ColumnInfo(name = "qt_saiu")
    public Integer qtSaiu;
    @ColumnInfo(name = "qt_voltou")
    public Integer qtVoltou;
    @ColumnInfo(name = "valor")
    public Long valor;

    public ItemVendaRoom() {
    }

    public ItemVendaRoom(ItemVendaImpl item, Long venda) {
        this.venda = venda;
        this.produto = item.getProduto().getCodigo();
        this.qtSaiu = item.getQtSaiu();
        this.qtVoltou = item.getQtVoltou();
        this.valor = item.getValor();
    }

    public ItemVendaRoom(ItemVendaFirestore item, Long venda) {
        this.venda = venda;
        this.produto = Long.valueOf(item.produto.getId());
        this.qtSaiu = item.qt_saiu;
        this.qtVoltou = item.qt_voltou;
        this.valor = item.valor;
    }
}
