package tk.andrielson.carrinhos.androidapp.data.model;

import java.util.Date;
import java.util.List;

/**
 * Created by anfesilva on 12/03/2018.
 */

public abstract class Venda<I extends ItemVenda, V extends Vendedor> extends AbsEntidadePadrao {
    public abstract Long getCodigo();

    public abstract void setCodigo(Long codigo);

    public abstract Integer getComissao();

    public abstract void setComissao(Integer comissao);

    public abstract Date getData();

    public abstract void setData(Date data);

    public abstract Long getValorTotal();

    public abstract void setValorTotal(Long total);

    public abstract V getVendedor();

    public abstract void setVendedor(V vendedor);

    public abstract String getStatus();

    public abstract void setStatus(String status);

    public abstract List<I> getItens();

    public abstract void setItens(List<I> itens);
}
