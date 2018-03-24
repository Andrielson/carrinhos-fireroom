package tk.andrielson.carrinhos.androidapp.fireroom.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.PropertyName;

import tk.andrielson.carrinhos.androidapp.data.model.ItemVenda;
import tk.andrielson.carrinhos.androidapp.data.model.Produto;
import tk.andrielson.carrinhos.androidapp.fireroom.firestore.dao.FirestoreDao;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "tb_item_venda",
        foreignKeys = {
                @ForeignKey(entity = VendaImpl.class, parentColumns = "venda_codigo", childColumns = "itv_venda_codigo", onDelete = CASCADE, onUpdate = CASCADE, deferred = true),
                @ForeignKey(entity = ProdutoImpl.class, parentColumns = "produto_codigo", childColumns = "itv_produto_codigo", onDelete = CASCADE, onUpdate = CASCADE, deferred = true)},
        indices = {
                @Index(value = {"itv_venda_codigo", "itv_produto_codigo"}, unique = true)})
@IgnoreExtraProperties
public final class ItemVendaImpl extends ItemVenda {

    public static final String COLECAO = "itens";

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

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "item_venda_id")
    private Long id;

    @ColumnInfo(name = "itv_venda_codigo", index = true)
    private Long vendaCodigo;

    @Ignore
    private DocumentReference refProduto;

    @ColumnInfo(name = "itv_produto_codigo", index = true)
    private Long produtoCodigo;

    @ColumnInfo(name = "qt_saiu")
    private Integer qtSaiu;

    @ColumnInfo(name = "qt_voltou")
    private Integer qtVoltou;

    @ColumnInfo(name = "qt_vendeu")
    private Integer qtVendeu;

    @ColumnInfo(name = "item_valor")
    private Long valor;

    @ColumnInfo(name = "item_total")
    private Long total;

    @Ignore
    private String produtoNome;

    @Ignore
    private String produtoSigla;

    @Ignore
    private ProdutoImpl produto;

    public ItemVendaImpl() {
    }

    public ItemVendaImpl(ProdutoImpl produto) {
        setProduto(produto);
    }

    private ItemVendaImpl(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readLong();
        vendaCodigo = in.readByte() == 0x00 ? null : in.readLong();
        qtSaiu = in.readByte() == 0x00 ? null : in.readInt();
        qtVoltou = in.readByte() == 0x00 ? null : in.readInt();
        qtVendeu = in.readByte() == 0x00 ? null : in.readInt();
        valor = in.readByte() == 0x00 ? null : in.readLong();
        total = in.readByte() == 0x00 ? null : in.readLong();
        produtoNome = in.readString();
        produtoSigla = in.readString();
        produto = (ProdutoImpl) in.readValue(ProdutoImpl.class.getClassLoader());
    }

    public ItemVendaImpl preparaFire() {
        if (this.produto != null) {
            this.produtoNome = this.produto.getNome();
            this.produtoSigla = this.produto.getSigla();
            this.refProduto = FirebaseFirestore.getInstance().collection(COLECAO).document(FirestoreDao.getIdFromCodigo(this.produto.getCodigo()));
        }
        return this;
    }

    public ItemVendaImpl preparaRoom() {
        if (this.produto != null) {
            this.produtoCodigo = this.produto.getCodigo();
        } else if (this.refProduto != null) {
            this.produtoCodigo = Long.valueOf(this.refProduto.getId());
        } else if (this.produtoCodigo == null) {
            //TODO: verificar outra forma de lidar com essa situação
            LogUtil.Log(COLECAO, "Item venda sem o código do produto!", Log.ERROR);
        }
        return this;
    }

    @Exclude
    public Long getId() {
        return id;
    }

    @Exclude
    public void setId(Long id) {
        this.id = id;
    }

    @Exclude
    public Long getVendaCodigo() {
        return vendaCodigo;
    }

    @Exclude
    public void setVendaCodigo(Long vendaCodigo) {
        this.vendaCodigo = vendaCodigo;
    }

    @PropertyName("produto")
    public DocumentReference getRefProduto() {
        return refProduto;
    }

    @SuppressWarnings("unused")
    @PropertyName("produto")
    public void setRefProduto(DocumentReference refProduto) {
        this.refProduto = refProduto;
        if (this.produtoCodigo == null)
            this.produtoCodigo = Long.valueOf(refProduto.getId());
    }

    @Exclude
    public Long getProdutoCodigo() {
        return produtoCodigo;
    }

    @Exclude
    public void setProdutoCodigo(Long produtoCodigo) {
        this.produtoCodigo = produtoCodigo;
    }

    @PropertyName("qt_saiu")
    @Override
    public Integer getQtSaiu() {
        return qtSaiu;
    }

    @PropertyName("qt_saiu")
    @Override
    public void setQtSaiu(Integer qtSaiu) {
        this.qtSaiu = qtSaiu;
    }

    @PropertyName("qt_voltou")
    @Override
    public Integer getQtVoltou() {
        return qtVoltou;
    }

    @PropertyName("qt_voltou")
    @Override
    public void setQtVoltou(Integer qtVoltou) {
        this.qtVoltou = qtVoltou;
    }

    @PropertyName("qt_vendeu")
    @Override
    public Integer getQtVendeu() {
        return qtVendeu;
    }

    @PropertyName("qt_vendeu")
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

    @SuppressWarnings("unused")
    @PropertyName("produto_nome")
    public String getProdutoNome() {
        return produtoNome;
    }

    @SuppressWarnings("unused")
    @PropertyName("produto_nome")
    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
    }

    @SuppressWarnings("unused")
    @PropertyName("produgo_sigla")
    public String getProdutoSigla() {
        return produtoSigla;
    }

    @SuppressWarnings("unused")
    @PropertyName("produgo_sigla")
    public void setProdutoSigla(String produtoSigla) {
        this.produtoSigla = produtoSigla;
    }

    @Exclude
    @Override
    public Produto getProduto() {
        return produto;
    }

    @Exclude
    @Override
    public void setProduto(Produto produto) {
        this.produto = (ProdutoImpl) produto;
        this.produtoCodigo = produto.getCodigo();
        this.produtoNome = produto.getNome();
        this.produtoSigla = produto.getSigla();
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
        if (vendaCodigo == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(vendaCodigo);
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
