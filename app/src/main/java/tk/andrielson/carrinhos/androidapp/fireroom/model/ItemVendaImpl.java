package tk.andrielson.carrinhos.androidapp.fireroom.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.PropertyName;

import tk.andrielson.carrinhos.androidapp.data.model.ItemVenda;
import tk.andrielson.carrinhos.androidapp.data.model.Produto;
import tk.andrielson.carrinhos.androidapp.fireroom.firestore.dao.FirestoreDao;

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
    @PropertyName("produto")
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

    public ItemVendaImpl(@NonNull ProdutoImpl produto) {
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

    @Nullable
    @PropertyName("produto_ref")
    public DocumentReference getRefProduto() {
        if (refProduto != null)
            return refProduto;
        if (produtoCodigo != null)
            return FirebaseFirestore.getInstance().collection(ProdutoImpl.COLECAO).document(FirestoreDao.getIdFromCodigo(produtoCodigo));
        if (produto != null && produto.getCodigo() != null)
            return FirebaseFirestore.getInstance().collection(ProdutoImpl.COLECAO).document(FirestoreDao.getIdFromCodigo(produto.getCodigo()));
        return null;
    }

    @SuppressWarnings("unused")
    @PropertyName("produto_ref")
    public void setRefProduto(DocumentReference refProduto) {
        this.refProduto = refProduto;
        if (refProduto == null)
            return;
        if (produtoCodigo == null)
            produtoCodigo = Long.valueOf(refProduto.getId());
        if (produto == null)
            produto = new ProdutoImpl();
        if (produto.getCodigo() != null)
            produto.setCodigo(Long.valueOf(refProduto.getId()));
    }

    @Nullable
    @Exclude
    public Long getProdutoCodigo() {
        if (produtoCodigo != null)
            return produtoCodigo;
        if (refProduto != null)
            return Long.valueOf(refProduto.getId());
        if (produto != null && produto.getCodigo() != null)
            return produto.getCodigo();
        return null;
    }

    @Exclude
    public void setProdutoCodigo(Long produtoCodigo) {
        this.produtoCodigo = produtoCodigo;
        if (produtoCodigo == null)
            return;
        if (refProduto == null)
            refProduto = FirebaseFirestore.getInstance().collection(ProdutoImpl.COLECAO).document(FirestoreDao.getIdFromCodigo(produtoCodigo));
        if (produto == null)
            produto = new ProdutoImpl();
        if (produto.getCodigo() == null)
            produto.setCodigo(produtoCodigo);
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
    @Nullable
    @PropertyName("produto_nome")
    public String getProdutoNome() {
        if (produtoNome != null)
            return produtoNome;
        if (produto != null && produto.getNome() != null)
            return produto.getNome();
        return null;
    }

    @SuppressWarnings("unused")
    @PropertyName("produto_nome")
    public void setProdutoNome(String produtoNome) {
        this.produtoNome = produtoNome;
        if (produtoNome == null)
            return;
        if (produto == null)
            produto = new ProdutoImpl();
        if (produto.getNome() == null)
            produto.setNome(produtoNome);
    }

    @SuppressWarnings("unused")
    @Nullable
    @PropertyName("produgo_sigla")
    public String getProdutoSigla() {
        if (produtoSigla != null)
            return produtoSigla;
        if (produto != null && produto.getSigla() != null)
            return produto.getSigla();
        return null;
    }

    @SuppressWarnings("unused")
    @PropertyName("produgo_sigla")
    public void setProdutoSigla(String produtoSigla) {
        this.produtoSigla = produtoSigla;
        if (produtoSigla == null)
            return;
        if (produto == null)
            produto = new ProdutoImpl();
        if (produto.getSigla() == null)
            produto.setSigla(produtoSigla);
    }

    @Nullable
    @Exclude
    @Override
    public Produto getProduto() {
        if (produto != null)
            return produto;
        if (refProduto != null || produtoNome != null || produtoCodigo != null || produtoSigla != null) {
            ProdutoImpl produto = new ProdutoImpl();
            produto.setCodigo(refProduto != null ? Long.valueOf(refProduto.getId()) : produtoCodigo);
            produto.setNome(produtoNome);
            produto.setSigla(produtoSigla);
            return produto;
        }
        return null;
    }

    @Exclude
    @Override
    public void setProduto(Produto produto) {
        this.produto = (ProdutoImpl) produto;
        if (produto != null) {
            produtoCodigo = produto.getCodigo();
            produtoNome = produto.getNome();
            produtoSigla = produto.getSigla();
            refProduto = FirebaseFirestore.getInstance().collection(ProdutoImpl.COLECAO).document(FirestoreDao.getIdFromCodigo(produto.getCodigo()));
        }
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
