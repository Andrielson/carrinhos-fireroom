package tk.andrielson.carrinhos.androidapp.data.model;

/**
 * Created by anfesilva on 12/03/2018.
 */

public interface ItemVenda<P extends Produto> {
    P getProduto();

    void setProduto(P produto);

    Integer getQtSaiu();

    void setQtSaiu(Integer qtSaiu);

    Integer getQtVoltou();

    void setQtVoltou(Integer qtVoltou);

    Integer getQtVendeu();

    void setQtVendeu(Integer qtVendeu);

    Long getValor();

    void setValor(Long valor);

}
