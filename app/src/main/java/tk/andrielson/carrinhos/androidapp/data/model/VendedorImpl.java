package tk.andrielson.carrinhos.androidapp.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.Contract;

/**
 * Created by Andrielson on 11/03/2018.
 */
@Entity(tableName = "tb_vendedor")
public final class VendedorImpl extends Vendedor {

    //<editor-fold desc="Campos do objeto JSON do Firestore">
    @Ignore
    public static final String COLECAO = "vendedores";
    @Ignore
    public static final String CODIGO = "codigo";
    @Ignore
    public static final String NOME = "nome";
    @Ignore
    public static final String COMISSAO = "comissao";
    @Ignore
    public static final String ATIVO = "ativo";
    @Ignore
    public static final String EXCLUIDO = "excluido";
    //</editor-fold>

    @SuppressWarnings("unused")
    @Ignore
    public static final Parcelable.Creator<VendedorImpl> CREATOR = new Parcelable.Creator<VendedorImpl>() {
        @NonNull
        @Override
        public VendedorImpl createFromParcel(Parcel in) {
            return new VendedorImpl(in);
        }

        @NonNull
        @Contract(pure = true)
        @Override
        public VendedorImpl[] newArray(int size) {
            return new VendedorImpl[size];
        }
    };
    @PrimaryKey
    private Long codigo = 0L;
    private String nome;
    private Integer comissao;
    private Boolean ativo = Boolean.TRUE;
    @Ignore
    private Boolean excluido = Boolean.FALSE;

    public VendedorImpl() {

    }

    @Ignore
    private VendedorImpl(Parcel in) {
        codigo = in.readByte() == 0x00 ? null : in.readLong();
        nome = in.readString();
        comissao = in.readByte() == 0x00 ? null : in.readInt();
        byte ativoVal = in.readByte();
        ativo = ativoVal == 0x02 ? null : ativoVal != 0x00;
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

    public void setExcluido(Boolean excluido) {
        this.excluido = excluido;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VendedorImpl vendedor = (VendedorImpl) o;

        if (!codigo.equals(vendedor.codigo)) return false;
        if (nome != null ? !nome.equals(vendedor.nome) : vendedor.nome != null) return false;
        if (!comissao.equals(vendedor.comissao)) return false;
        if (!ativo.equals(vendedor.ativo)) return false;
        return excluido.equals(vendedor.excluido);
    }

    @Override
    public int hashCode() {
        int result = codigo.hashCode();
        result = 31 * result + (nome != null ? nome.hashCode() : 0);
        result = 31 * result + (comissao != null ? comissao.hashCode() : 0);
        result = 31 * result + ativo.hashCode();
        result = 31 * result + excluido.hashCode();
        return result;
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
    }
}
