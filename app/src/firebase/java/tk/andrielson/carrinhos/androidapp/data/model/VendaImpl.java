package tk.andrielson.carrinhos.androidapp.data.model;

import java.util.Date;

/**
 * Created by anfesilva on 12/03/2018.
 */

public class VendaImpl implements Venda<ItemVendaImpl, VendedorImpl> {
    private Long codigo;
    private Integer comissao;
    private Date data;
    private Long total;
    private VendedorImpl vendedor;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Integer getComissao() {
        return comissao;
    }

    public void setComissao(Integer comissao) {
        this.comissao = comissao;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public VendedorImpl getVendedor() {
        return vendedor;
    }

    public void setVendedor(VendedorImpl vendedor) {
        this.vendedor = vendedor;
    }
}
