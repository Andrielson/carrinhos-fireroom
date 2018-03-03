package tk.andrielson.carrinhos.androidapp.data.room.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

import tk.andrielson.carrinhos.androidapp.data.model.Produto;
import tk.andrielson.carrinhos.androidapp.data.room.converter.BigDecimalToLongConverter;

/**
 * Created by Andrielson on 02/03/2018.
 */

@Entity(tableName = "tb_produto", indices = {@Index(value = {"codigo"}, unique = true)})
public class ProdutoRoom implements Produto {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ProdutoRoom> CREATOR = new Parcelable.Creator<ProdutoRoom>() {
        @Override
        public ProdutoRoom createFromParcel(Parcel in) {
            return new ProdutoRoom(in);
        }

        @Override
        public ProdutoRoom[] newArray(int size) {
            return new ProdutoRoom[size];
        }
    };

    @PrimaryKey(autoGenerate = true)
    private Long codigo;
    private String nome;
    private String sigla;
    @TypeConverters({BigDecimalToLongConverter.class})
    private BigDecimal preco;
    @TypeConverters({BigDecimalToLongConverter.class})
    private BigDecimal custo;
    private Boolean ativo = Boolean.TRUE;

    public ProdutoRoom() {

    }

    protected ProdutoRoom(Parcel in) {
        codigo = in.readByte() == 0x00 ? null : in.readLong();
        nome = in.readString();
        sigla = in.readString();
        preco = (BigDecimal) in.readValue(BigDecimal.class.getClassLoader());
        custo = (BigDecimal) in.readValue(BigDecimal.class.getClassLoader());
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
    public BigDecimal getPreco() {
        return preco;
    }

    @Override
    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    @Override
    public BigDecimal getCusto() {
        return custo;
    }

    @Override
    public void setCusto(BigDecimal custo) {
        this.custo = custo;
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
        dest.writeValue(preco);
        dest.writeValue(custo);
        if (ativo == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (ativo ? 0x01 : 0x00));
        }
    }
}
