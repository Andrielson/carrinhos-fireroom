package tk.andrielson.carrinhos.androidapp.fireroom.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.PropertyName;

import tk.andrielson.carrinhos.androidapp.data.model.ItemVenda;
import tk.andrielson.carrinhos.androidapp.fireroom.room.entities.ProdutoRoom;
import tk.andrielson.carrinhos.androidapp.fireroom.room.entities.VendaRoom;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "tb_item_venda",
        foreignKeys = {
                @ForeignKey(entity = VendaImpl.class, parentColumns = "venda_codigo", childColumns = "itv_venda_codigo", onDelete = CASCADE, onUpdate = CASCADE, deferred = true),
                @ForeignKey(entity = ProdutoImpl.class, parentColumns = "produto_codigo", childColumns = "itv_produto_codigo", onDelete = CASCADE, onUpdate = CASCADE, deferred = true)},
        indices = {
                @Index(value = {"item_venda_codigo", "item_venda_produto_codigo"}, unique = true)})
@IgnoreExtraProperties
public final class ItemVendaImpl extends ItemVenda<ProdutoImpl> {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ItemVendaImpl> CREATOR = new Parcelable.Creator<ItemVendaImpl>() {
        @Override
        public ItemVendaImpl createFromParcel(Parcel in) {
            return new ItemVendaImpl(in);
        }

        @Override
        public ItemVendaImpl[] newArray(int size) {
            return new ItemVendaImpl[size];
        }
    };

    @PrimaryKey
    @ColumnInfo(name = "item_venda_id")
    @Exclude
    private Long id;

    @Exclude
    @ColumnInfo(name = "itv_venda_codigo", index = true)
    private Long codigo;

    @Ignore
    @PropertyName("produto")
    private DocumentReference refProduto;

    @Exclude
    @ColumnInfo(name = "itv_produto_codigo", index = true)
    private Long produtoCodigo;

    @ColumnInfo(name = "qt_saiu")
    @PropertyName("qt_saiu")
    private Integer qtSaiu;

    @ColumnInfo(name = "qt_voltou")
    @PropertyName("qt_voltou")
    private Integer qtVoltou;

    @ColumnInfo(name = "qt_vendeu")
    @PropertyName("qt_vendeu")
    private Integer qtVendeu;

    @ColumnInfo(name = "item_valor")
    private Long valor;

    @ColumnInfo(name = "item_total")
    private Long total;

    @Ignore
    @PropertyName("produto_nome")
    private String produtoNome;

    @Ignore
    @PropertyName("produgo_sigla")
    private String produtoSigla;

    @Exclude
    @Ignore
    private ProdutoImpl produto;

    public ItemVendaImpl() {
    }

    private ItemVendaImpl(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readLong();
        codigo = in.readByte() == 0x00 ? null : in.readLong();
        qtSaiu = in.readByte() == 0x00 ? null : in.readInt();
        qtVoltou = in.readByte() == 0x00 ? null : in.readInt();
        qtVendeu = in.readByte() == 0x00 ? null : in.readInt();
        valor = in.readByte() == 0x00 ? null : in.readLong();
        total = in.readByte() == 0x00 ? null : in.readLong();
        produtoNome = in.readString();
        produtoSigla = in.readString();
        produto = (ProdutoImpl) in.readValue(ProdutoImpl.class.getClassLoader());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public DocumentReference getRefProduto() {
        return refProduto;
    }

    public void setRefProduto(DocumentReference refProduto) {
        this.refProduto = refProduto;
    }

    public Long getProdutoCodigo() {
        return produtoCodigo;
    }

    public void setProdutoCodigo(Long produtoCodigo) {
        this.produtoCodigo = produtoCodigo;
    }

    @Override
    public Integer getQtSaiu() {
        return qtSaiu;
    }

    @Override
    public void setQtSaiu(Integer qtSaiu) {
        this.qtSaiu = qtSaiu;
    }

    @Override
    public Integer getQtVoltou() {
        return qtVoltou;
    }

    @Override
    public void setQtVoltou(Integer qtVoltou) {
        this.qtVoltou = qtVoltou;
    }

    @Override
    public Integer getQtVendeu() {
        return qtVendeu;
    }

    @Override
    public void setQtVendeu(Integer qtVendeu) {
        this.qtVendeu = qtVendeu;
    }

    @Override
    public Long getValor() {
        return valor;
    }

    @Override
    public void setValor(Long valor) {
        this.valor = valor;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    public String getProdutoSigla() {
        return produtoSigla;
    }

    public void setProdutoSigla(String produtoSigla) {
        this.produtoSigla = produtoSigla;
    }

    @Override
    public ProdutoImpl getProduto() {
        return produto;
    }

    @Override
    public void setProduto(ProdutoImpl produto) {
        this.produto = produto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(id);
        }
        if (codigo == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(codigo);
        }
        if (qtSaiu == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(qtSaiu);
        }
        if (qtVoltou == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(qtVoltou);
        }
        if (qtVendeu == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(qtVendeu);
        }
        if (valor == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(valor);
        }
        if (total == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(total);
        }
        dest.writeString(produtoNome);
        dest.writeString(produtoSigla);
        dest.writeValue(produto);
    }
}
