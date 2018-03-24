package tk.andrielson.carrinhos.androidapp.data.model;

public abstract class ItemVenda extends AbsEntidadePadrao {
    public abstract Produto getProduto();

    public abstract void setProduto(Produto produto);

    public abstract Integer getQtSaiu();

    public abstract void setQtSaiu(Integer qtSaiu);

    public abstract Integer getQtVoltou();

    public abstract void setQtVoltou(Integer qtVoltou);

    public abstract Integer getQtVendeu();

    public abstract void setQtVendeu(Integer qtVendeu);

    public abstract Long getValor();

    public abstract void setValor(Long valor);

    public abstract Long getTotal();

    public abstract void setTotal(Long total);

}
