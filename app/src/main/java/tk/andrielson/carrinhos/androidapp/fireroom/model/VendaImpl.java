package tk.andrielson.carrinhos.androidapp.fireroom.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;
import com.google.firebase.firestore.PropertyName;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tk.andrielson.carrinhos.androidapp.data.model.Venda;

@IgnoreExtraProperties
@Entity(tableName = "vendateste")
public final class VendaImpl extends Venda<ItemVendaImpl, VendedorImpl> {

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

    @PrimaryKey
    @ColumnInfo(name = "venda_codigo")
    private Long codigo;

    @ColumnInfo(name = "venda_data")
    @ServerTimestamp
    private Date data;

    @ColumnInfo(name = "venda_comissao")
    private Integer comissao;

    @ColumnInfo(name = "venda_valor_total")
    private Long valorTotal;

    @ColumnInfo(name = "venda_valor_pago")
    private Long valorPago;

    @ColumnInfo(name = "venda_valor_comissao")
    private Long valorComissao;

    @Ignore
    @PropertyName("vendedor")
    private DocumentReference refVendedor;

    @ColumnInfo(name = "venda_vendedor_codigo")
    @Exclude
    private Long vendedorCodigo;

    @Ignore
    @PropertyName("vendedor_nome")
    private String vendedorNome;

    @ColumnInfo(name = "venda_status")
    private String status;

    @Exclude
    @Ignore
    private VendedorImpl vendedor;

    @Exclude
    @Ignore
    private List<ItemVendaImpl> itens;

    protected VendaImpl(Parcel in) {
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
        if (in.readByte() == 0x01) {
            itens = new ArrayList<ItemVendaImpl>();
            in.readList(itens, ItemVendaImpl.class.getClassLoader());
        } else {
            itens = null;
        }
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Integer getComissao() {
        return comissao;
    }

    public void setComissao(Integer comissao) {
        this.comissao = comissao;
    }

    public Long getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Long valor_total) {
        this.valorTotal = valor_total;
    }

    public Long getValorPago() {
        return valorPago;
    }

    public void setValorPago(Long valor_pago) {
        this.valorPago = valor_pago;
    }

    public Long getValorComissao() {
        return valorComissao;
    }

    public void setValorComissao(Long valor_comissao) {
        this.valorComissao = valor_comissao;
    }

    public DocumentReference getRefVendedor() {
        return refVendedor;
    }

    public void setRefVendedor(DocumentReference vendedor_ref) {
        this.refVendedor = vendedor_ref;
    }

    public String getVendedorNome() {
        return vendedorNome;
    }

    public void setVendedorNome(String vendedor_nome) {
        this.vendedorNome = vendedor_nome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public VendedorImpl getVendedor() {
        return vendedor;
    }

    public void setVendedor(VendedorImpl vendedor) {
        this.vendedor = vendedor;
    }

    public Long getVendedorCodigo() {
        return vendedorCodigo;
    }

    public void setVendedorCodigo(Long vendedorCodigo) {
        this.vendedorCodigo = vendedorCodigo;
    }

    public List<ItemVendaImpl> getItens() {
        return itens;
    }

    public void setItens(List<ItemVendaImpl> itens) {
        this.itens = itens;
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
        if (itens == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(itens);
        }
    }
}
