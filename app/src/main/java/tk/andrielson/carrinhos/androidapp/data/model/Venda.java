package tk.andrielson.carrinhos.androidapp.data.model;

import java.util.Date;

/**
 * Created by anfesilva on 12/03/2018.
 */

public interface Venda<I extends ItemVenda, V extends Vendedor> {
    Long getCodigo();

    void setCodigo(Long codigo);

    Integer getComissao();

    void setComissao(Integer comissao);

    Date getData();

    void setData(Date data);

    Long getTotal();

    void setTotal(Long total);

    V getVendedor();

    void setVendedor(V vendedor);
}
