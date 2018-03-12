package tk.andrielson.carrinhos.androidapp.data.model;

/**
 * Created by anfesilva on 12/03/2018.
 */

public class ItemVendaImpl implements ItemVenda<ProdutoImpl> {
    private ProdutoImpl produto;
    private Integer qtSaiu;
    private Integer qtVoltou;
    private Integer qtVendeu;
    private Long valor;

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
}
