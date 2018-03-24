package tk.andrielson.carrinhos.androidapp.fireroom.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;

import tk.andrielson.carrinhos.androidapp.data.model.Vendedor;

@Entity(tableName = "vendedorteste")
@IgnoreExtraProperties
public final class VendedorImpl extends Vendedor {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<VendedorImpl> CREATOR = new Parcelable.Creator<VendedorImpl>() {
        @Override
        public VendedorImpl createFromParcel(Parcel in) {
            return new VendedorImpl(in);
        }

        @Override
        public VendedorImpl[] newArray(int size) {
            return new VendedorImpl[size];
        }
    };
    @PrimaryKey
    @ColumnInfo(name = "vendedor_codigo")
    private Long codigo;
    @ColumnInfo(name = "vendedor_nome")
    private String nome;
    @ColumnInfo(name = "vendedor_comissao")
    private Integer comissao;
    @ColumnInfo(name = "vendedor_ativo")
    private Boolean ativo;
    @ColumnInfo(name = "vendedor_excluido")
    private Boolean excluido = Boolean.FALSE;

    public VendedorImpl() {
    }

    private VendedorImpl(Parcel in) {
        codigo = in.readByte() == 0x00 ? null : in.readLong();
        nome = in.readString();
        comissao = in.readByte() == 0x00 ? null : in.readInt();
        byte ativoVal = in.readByte();
        ativo = ativoVal == 0x02 ? null : ativoVal != 0x00;
        byte excluidoVal = in.readByte();
        excluido = excluidoVal == 0x02 ? null : excluidoVal != 0x00;
    }

    @Override
    public Long getCodigo() {
        return codigo;
    }

    @Override
    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public Integer getComissao() {
        return comissao;
    }

    @Override
    public void setComissao(Integer comissao) {
        this.comissao = comissao;
    }

    @Override
    public Boolean getAtivo() {
        return ativo;
    }

    @Override
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Boolean getExcluido() {
        return excluido;
    }

    public void setExcluido(Boolean excluido) {
        this.excluido = excluido;
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
        dest.writeString(nome);
        if (comissao == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(comissao);
        }
        if (ativo == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (ativo ? 0x01 : 0x00));
        }
        if (excluido == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (excluido ? 0x01 : 0x00));
        }
    }
}
