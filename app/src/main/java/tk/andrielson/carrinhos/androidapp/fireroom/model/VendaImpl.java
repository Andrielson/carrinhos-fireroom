package tk.andrielson.carrinhos.androidapp.fireroom.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.PropertyName;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

import tk.andrielson.carrinhos.androidapp.data.model.ItemVenda;
import tk.andrielson.carrinhos.androidapp.data.model.Venda;
import tk.andrielson.carrinhos.androidapp.data.model.Vendedor;
import tk.andrielson.carrinhos.androidapp.fireroom.firestore.dao.FirestoreDao;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@IgnoreExtraProperties
@Entity(tableName = "tb_venda", foreignKeys = @ForeignKey(entity = VendedorImpl.class, parentColumns = "vendedor_codigo", childColumns = "venda_vendedor_codigo", onDelete = CASCADE, onUpdate = CASCADE, deferred = true))
public final class VendaImpl extends Venda {

    public static final String COLECAO = "vendas";

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<VendaImpl> CREATOR = new Parcelable.Creator<VendaImpl>() {
        @Override
        public VendaImpl createFromParcel(Parcel in) {
            return new VendaImpl(in);
        }

        @Override
        public VendaImpl[] newArray(int size) {
            return new VendaImpl[size];
        }
    };

    @Ignore
    private DocumentReference refVendedor;

    @PrimaryKey
    @ColumnInfo(name = "venda_codigo")
    private Long codigo;

    @ColumnInfo(name = "venda_data", index = true)
    private Date data;

    @ColumnInfo(name = "venda_comissao")
    private Integer comissao;

    @ColumnInfo(name = "venda_valor_total")
    private Long valorTotal;

    @ColumnInfo(name = "venda_valor_pago")
    private Long valorPago;

    @ColumnInfo(name = "venda_valor_comissao")
    private Long valorComissao;

    @ColumnInfo(name = "venda_vendedor_codigo", index = true)
    private Long vendedorCodigo;

    @Ignore
    private String vendedorNome;

    @ColumnInfo(name = "venda_status")
    private String status;

    @Ignore
    private VendedorImpl vendedor;

    @Ignore
    private ItemVendaImpl[] itens;

    public VendaImpl() {
    }

    private VendaImpl(Parcel in) {
        codigo = in.readByte() == 0x00 ? null : in.readLong();
        long tmpData = in.readLong();
        data = tmpData != -1 ? new Date(tmpData) : null;
        comissao = in.readByte() == 0x00 ? null : in.readInt();
        valorTotal = in.readByte() == 0x00 ? null : in.readLong();
        valorPago = in.readByte() == 0x00 ? null : in.readLong();
        valorComissao = in.readByte() == 0x00 ? null : in.readLong();
        vendedorCodigo = in.readByte() == 0x00 ? null : in.readLong();
        vendedorNome = in.readString();
        status = in.readString();
        vendedor = (VendedorImpl) in.readValue(VendedorImpl.class.getClassLoader());
    }

    /*public VendaImpl preparaFire() {
        if (this.vendedor != null) {
            this.vendedorNome = vendedor.getNome();
            this.refVendedor = FirebaseFirestore.getInstance().collection(COLECAO).document(FirestoreDao.getIdFromCodigo(this.vendedor.getCodigo()));
        }
        return this;
    }*/

    public VendaImpl preparaRoom() {
        if (this.vendedor != null) {
            this.vendedorCodigo = vendedor.getCodigo();
        } else if (this.refVendedor != null) {
            this.vendedorCodigo = Long.valueOf(refVendedor.getId());
        } else if (this.vendedorCodigo == null) {
            //TODO: verificar outra forma de lidar com essa situação
            LogUtil.Log(COLECAO, "Venda sem o código do vendedor!", Log.ERROR);
        }
        return this;
    }

    @Override
    public Long getCodigo() {
        return codigo;
    }

    @Override
    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    @ServerTimestamp
    @Override
    public Date getData() {
        return data;
    }

    @ServerTimestamp
    @Override
    public void setData(Date data) {
        this.data = data;
    }

    @Override
    public Integer getComissao() {
        return comissao;
    }

    @Override
    public void setComissao(Integer comissao) {
        this.comissao = comissao;
    }

    @PropertyName("valor_total")
    @Override
    public Long getValorTotal() {
        return valorTotal;
    }

    @PropertyName("valor_total")
    @Override
    public void setValorTotal(Long valor_total) {
        this.valorTotal = valor_total;
    }

    @PropertyName("valor_pago")
    @Override
    public Long getValorPago() {
        return valorPago;
    }

    @PropertyName("valor_pago")
    @Override
    public void setValorPago(Long valor_pago) {
        this.valorPago = valor_pago;
    }

    @PropertyName("valor_comissao")
    @Override
    public Long getValorComissao() {
        return valorComissao;
    }

    @PropertyName("valor_comissao")
    @Override
    public void setValorComissao(Long valor_comissao) {
        this.valorComissao = valor_comissao;
    }

    @PropertyName("vendedor_nome")
    public String getVendedorNome() {
        return vendedorNome;
    }

    @PropertyName("vendedor_nome")
    public void setVendedorNome(String vendedorNome) {
        this.vendedorNome = vendedorNome;
        if (this.vendedor == null)
            this.vendedor = new VendedorImpl();
        if (this.vendedor.getNome() == null)
            this.vendedor.setNome(vendedorNome);
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Exclude
    @Override
    public Vendedor getVendedor() {
        return vendedor;
    }

    @Exclude
    @Override
    public void setVendedor(Vendedor vendedor) {
        this.vendedor = (VendedorImpl) vendedor;
        this.vendedorNome = vendedor.getNome();
        this.refVendedor = FirebaseFirestore.getInstance().collection(COLECAO).document(FirestoreDao.getIdFromCodigo(this.vendedor.getCodigo()));
        this.vendedorCodigo = vendedor.getCodigo();
    }

    @Exclude
    public Long getVendedorCodigo() {
        return vendedorCodigo;
    }

    @Exclude
    public void setVendedorCodigo(Long vendedorCodigo) {
        this.vendedorCodigo = vendedorCodigo;
    }

    @PropertyName("vendedor")
    public DocumentReference getRefVendedor() {
        return refVendedor;
    }

    @PropertyName("vendedor")
    public void setRefVendedor(DocumentReference refVendedor) {
        this.refVendedor = refVendedor;
        this.vendedorCodigo = Long.valueOf(refVendedor.getId());
        if (this.vendedor == null)
            this.vendedor = new VendedorImpl();
        if (this.vendedor.getCodigo() == null)
            this.vendedor.setCodigo(Long.valueOf(refVendedor.getId()));
    }

    @Exclude
    @Override
    public ItemVenda[] getItens() {
        return itens;
    }

    @Exclude
    @Override
    public void setItens(ItemVenda[] itens) {
        this.itens = new ItemVendaImpl[itens.length];
        for (int i = 0; i < itens.length; i++)
            this.itens[i] = (ItemVendaImpl) itens[i];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (codigo == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(codigo);
        }
        dest.writeLong(data != null ? data.getTime() : -1L);
        if (comissao == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(comissao);
        }
        if (valorTotal == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(valorTotal);
        }
        if (valorPago == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(valorPago);
        }
        if (valorComissao == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(valorComissao);
        }
        if (vendedorCodigo == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(vendedorCodigo);
        }
        dest.writeString(vendedorNome);
        dest.writeString(status);
        dest.writeValue(vendedor);
        dest.writeTypedArray(itens, 0);
    }

    public static class VendaComVendedorTotal {
        @Embedded
        public VendaImpl venda;
        @Embedded
        public VendedorImpl vendedor;
    }
}
