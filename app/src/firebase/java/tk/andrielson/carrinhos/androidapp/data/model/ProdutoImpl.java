package tk.andrielson.carrinhos.androidapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by Andrielson on 03/03/2018.
 */

public final class ProdutoImpl extends Produto {

    //<editor-fold desc="Campos do objeto JSON do Firebase">
    public static final String COLECAO = "produtos";
    public static final String CODIGO = "codigo";
    public static final String NOME = "nome";
    public static final String SIGLA = "sigla";
    public static final String PRECO = "preco";
    public static final String ATIVO = "ativo";
    public static final String EXCLUIDO = "excluido";
    //</editor-fold>

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
    private Long codigo = 0L;
    private String nome;
    private String sigla;
    private Long preco = 0L;
    private Boolean ativo = Boolean.TRUE;
    private Boolean excluido = Boolean.FALSE;

    public ProdutoImpl() {
    }

    public ProdutoImpl(Long codigo, String nome, String sigla, Long preco) {
        this.codigo = codigo;
        this.nome = nome;
        this.sigla = sigla;
        this.preco = preco;
    }

    private ProdutoImpl(Parcel in) {
        codigo = in.readByte() == 0x00 ? null : in.readLong();
        nome = in.readString();
        sigla = in.readString();
        preco = in.readByte() == 0x00 ? null : in.readLong();
        byte ativoVal = in.readByte();
        ativo = ativoVal == 0x02 ? null : ativoVal != 0x00;
        byte excluidoVal = in.readByte();
        excluido = excluidoVal == 0x02 ? null : ativoVal != 0x00;
    }

    @NonNull
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

    @NonNull
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
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProdutoImpl produto = (ProdutoImpl) o;

        if (!codigo.equals(produto.codigo)) return false;
        if (nome != null ? !nome.equals(produto.nome) : produto.nome != null) return false;
        if (sigla != null ? !sigla.equals(produto.sigla) : produto.sigla != null) return false;
        if (!preco.equals(produto.preco)) return false;
        if (!ativo.equals(produto.ativo)) return false;
        return excluido.equals(produto.excluido);
    }

    @Override
    public int hashCode() {
        int result = codigo.hashCode();
        result = 31 * result + (nome != null ? nome.hashCode() : 0);
        result = 31 * result + (sigla != null ? sigla.hashCode() : 0);
        result = 31 * result + preco.hashCode();
        result = 31 * result + ativo.hashCode();
        result = 31 * result + excluido.hashCode();
        return result;
    }
}
