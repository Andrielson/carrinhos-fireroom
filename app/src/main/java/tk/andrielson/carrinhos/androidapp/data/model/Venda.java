package tk.andrielson.carrinhos.androidapp.data.model;

import java.util.Date;
import java.util.List;

public abstract class Venda extends AbsEntidadePadrao {
    public abstract Long getCodigo();

    public abstract void setCodigo(Long codigo);

    public abstract Integer getComissao();

    public abstract void setComissao(Integer comissao);

    public abstract Date getData();

    public abstract void setData(Date data);

    public abstract Long getValorTotal();

    public abstract void setValorTotal(Long total);

    public abstract Long getValorPago();

    public abstract void setValorPago(Long valor_pago);

    public abstract Long getValorComissao();

    public abstract void setValorComissao(Long valor_comissao);

    public abstract Vendedor getVendedor();

    public abstract void setVendedor(Vendedor vendedor);

    public abstract String getStatus();

    public abstract void setStatus(String status);

    public abstract ItemVenda[] getItens();

    public abstract void setItens(ItemVenda[] itens);
}
