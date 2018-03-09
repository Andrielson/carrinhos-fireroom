package tk.andrielson.carrinhos.androidapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Andrielson on 03/03/2018.
 */

public class ProdutoImpl
        extends AbsEntidadePadrao
        implements Produto {

    //<editor-fold desc="Campos do objeto JSON do Firebase">
    public static final String COLLECTION = "produtos";
    public static final String CODIGO = "codigo";
    public static final String NOME = "nome";
    public static final String SIGLA = "sigla";
    public static final String PRECO = "preco";
    public static final String ATIVO = "ativo";
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
    private Long codigo;
    private String nome;
    private String sigla;
    private Long preco;
    private Boolean ativo = Boolean.TRUE;

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

        if (!getCodigo().equals(produto.getCodigo())) return false;
        if (!getNome().equals(produto.getNome())) return false;
        if (!getSigla().equals(produto.getSigla())) return false;
        if (!getPreco().equals(produto.getPreco())) return false;
        return getAtivo().equals(produto.getAtivo());
    }

    @Override
    public int hashCode() {
        int result = getCodigo().hashCode();
        result = 31 * result + getNome().hashCode();
        result = 31 * result + getSigla().hashCode();
        result = 31 * result + getPreco().hashCode();
        result = 31 * result + getAtivo().hashCode();
        return result;
    }
}
