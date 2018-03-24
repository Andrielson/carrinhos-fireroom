package tk.andrielson.carrinhos.androidapp.fireroom.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.IgnoreExtraProperties;

import tk.andrielson.carrinhos.androidapp.data.model.Produto;

@Entity(tableName = "tb_produto")
@IgnoreExtraProperties
public final class ProdutoImpl extends Produto {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ProdutoImpl> CREATOR = new Parcelable.Creator<ProdutoImpl>() {
        @Override
        public ProdutoImpl createFromParcel(Parcel in) {
            return new ProdutoImpl(in);
        }

        @Override
        public ProdutoImpl[] newArray(int size) {
            return new ProdutoImpl[size];
        }
    };

    public static final String COLECAO = "produtos";
    public static final String EXCLUIDO = "excluido";

    @PrimaryKey
    @ColumnInfo(name = "produto_codigo")
    private Long codigo;
    @ColumnInfo(name = "produto_nome")
    private String nome;
    @ColumnInfo(name = "produto_sigla")
    private String sigla;
    @ColumnInfo(name = "produto_preco")
    private Long preco;
    @ColumnInfo(name = "produto_ativo")
    private Boolean ativo = Boolean.TRUE;
    @ColumnInfo(name = "produto_excluido")
    private Boolean excluido = Boolean.FALSE;

    public ProdutoImpl() {
    }

    private ProdutoImpl(Parcel in) {
        codigo = in.readByte() == 0x00 ? null : in.readLong();
        nome = in.readString();
        sigla = in.readString();
        preco = in.readByte() == 0x00 ? null : in.readLong();
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
    public String getSigla() {
        return sigla;
    }

    @Override
    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    @Override
    public Long getPreco() {
        return preco;
    }

    @Override
    public void setPreco(Long preco) {
        this.preco = preco;
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
        dest.writeString(sigla);
        if (preco == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(preco);
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
