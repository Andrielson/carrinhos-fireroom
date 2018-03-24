package tk.andrielson.carrinhos.androidapp.fireroom.room.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;

import tk.andrielson.carrinhos.androidapp.fireroom.firestore.collections.ItemVendaFire;
import tk.andrielson.carrinhos.androidapp.fireroom.model.ItemVendaImpl;

//@Entity(tableName = "tb_item_venda",
//        foreignKeys = {
//                @ForeignKey(entity = VendaRoom.class, parentColumns = "venda_codigo", childColumns = "venda_codigo", onDelete = CASCADE, onUpdate = CASCADE, deferred = true),
//                @ForeignKey(entity = ProdutoRoom.class, parentColumns = "produto_codigo", childColumns = "produto_codigo", onDelete = CASCADE, onUpdate = CASCADE, deferred = true)},
//        indices = {
//                @Index(name = "venda_produto", value = {"venda_codigo", "produto_codigo"}, unique = true),
//                @Index(name = "idx_item_venda", value = {"venda_codigo"}),
//                @Index(name = "idx_item_produto", value = {"produto_codigo"})})
public final class ItemVendaRoom {
    @PrimaryKey(autoGenerate = true)
    public Long id;
    @ColumnInfo(name = "venda_codigo")
    public Long venda;
    @ColumnInfo(name = "produto_codigo")
    public Long produto;
    @ColumnInfo(name = "qt_saiu")
    public Integer qtSaiu;
    @ColumnInfo(name = "qt_voltou")
    public Integer qtVoltou;
    @ColumnInfo(name = "valor_item")
    public Long valor;

    public ItemVendaRoom() {
    }

    public ItemVendaRoom(ItemVendaFire item, Long venda) {
        this.venda = venda;
        this.produto = Long.valueOf(item.produto.getId());
        this.qtSaiu = item.qt_saiu;
        this.qtVoltou = item.qt_voltou;
        this.valor = item.valor;
    }

    public ItemVendaImpl getModel() {
        ItemVendaImpl itemVenda = new ItemVendaImpl();
        itemVenda.setQtSaiu(qtSaiu);
        itemVenda.setQtVoltou(qtVoltou);
        itemVenda.setQtVendeu(qtSaiu - qtVoltou);
        itemVenda.setValor(valor);
        return itemVenda;
    }
}
