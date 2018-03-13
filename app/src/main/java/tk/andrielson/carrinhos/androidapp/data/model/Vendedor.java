package tk.andrielson.carrinhos.androidapp.data.model;

/**
 * Created by Andrielson on 02/03/2018.
 */

public abstract class Vendedor extends AbsEntidadePadrao {

    public abstract Long getCodigo();

    public abstract void setCodigo(Long codigo);

    public abstract String getNome();

    public abstract void setNome(String nome);

    public abstract Integer getComissao();

    public abstract void setComissao(Integer comissao);

    public abstract Boolean getAtivo();

    public abstract void setAtivo(Boolean ativo);
}
