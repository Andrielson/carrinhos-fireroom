package tk.andrielson.carrinhos.androidapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by anfesilva on 12/03/2018.
 */

public class ItemVendaImpl
        extends ItemVenda<ProdutoImpl> {

    public static final String COLECAO = "itens";
    public static final String PRODUTO = "produto";
    public static final String QT_SAIU = "qt_saiu";
    public static final String QT_VENDEU = "qt_vendeu";
    public static final String QT_VOLTOU = "qt_voltou";
    public static final String VALOR = "valor";
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
    private ProdutoImpl produto;
    private Integer qtSaiu;
    private Integer qtVoltou;
    private Integer qtVendeu;
    private Long valor;

    public ItemVendaImpl() {
    }

    public ItemVendaImpl(ProdutoImpl produto) {
        this.produto = produto;
    }

    private ItemVendaImpl(Parcel in) {
        produto = (ProdutoImpl) in.readValue(ProdutoImpl.class.getClassLoader());
        qtSaiu = in.readByte() == 0x00 ? null : in.readInt();
        qtVoltou = in.readByte() == 0x00 ? null : in.readInt();
        qtVendeu = in.readByte() == 0x00 ? null : in.readInt();
        valor = in.readByte() == 0x00 ? null : in.readLong();
    }

    public ProdutoImpl getProduto() {
        return produto;
    }

    public void setProduto(ProdutoImpl produto) {
        this.produto = produto;
    }

    public Integer getQtSaiu() {
        return qtSaiu;
    }

    public void setQtSaiu(Integer qtSaiu) {
        this.qtSaiu = qtSaiu;
    }

    public Integer getQtVoltou() {
        return qtVoltou;
    }

    public void setQtVoltou(Integer qtVoltou) {
        this.qtVoltou = qtVoltou;
    }

    public Integer getQtVendeu() {
        return qtVendeu;
    }

    public void setQtVendeu(Integer qtVendeu) {
        this.qtVendeu = qtVendeu;
    }

    public Long getValor() {
        return valor;
    }

    public void setValor(Long valor) {
        this.valor = valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemVendaImpl itemVenda = (ItemVendaImpl) o;

        if (produto != null ? !produto.equals(itemVenda.produto) : itemVenda.produto != null)
            return false;
        if (qtSaiu != null ? !qtSaiu.equals(itemVenda.qtSaiu) : itemVenda.qtSaiu != null)
            return false;
        if (qtVoltou != null ? !qtVoltou.equals(itemVenda.qtVoltou) : itemVenda.qtVoltou != null)
            return false;
        if (qtVendeu != null ? !qtVendeu.equals(itemVenda.qtVendeu) : itemVenda.qtVendeu != null)
            return false;
        return valor != null ? valor.equals(itemVenda.valor) : itemVenda.valor == null;
    }

    @Override
    public int hashCode() {
        int result = produto != null ? produto.hashCode() : 0;
        result = 31 * result + (qtSaiu != null ? qtSaiu.hashCode() : 0);
        result = 31 * result + (qtVoltou != null ? qtVoltou.hashCode() : 0);
        result = 31 * result + (qtVendeu != null ? qtVendeu.hashCode() : 0);
        result = 31 * result + (valor != null ? valor.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(produto);
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
    }
}
