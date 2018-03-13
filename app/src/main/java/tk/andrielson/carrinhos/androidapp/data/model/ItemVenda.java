package tk.andrielson.carrinhos.androidapp.data.model;

/**
 * Created by anfesilva on 12/03/2018.
 */

public abstract class ItemVenda<P extends Produto> extends AbsEntidadePadrao {
    public abstract P getProduto();

    public abstract void setProduto(P produto);

    public abstract Integer getQtSaiu();

    public abstract void setQtSaiu(Integer qtSaiu);

    public abstract Integer getQtVoltou();

    public abstract void setQtVoltou(Integer qtVoltou);

    public abstract Integer getQtVendeu();

    public abstract void setQtVendeu(Integer qtVendeu);

    public abstract Long getValor();

    public abstract void setValor(Long valor);

}
