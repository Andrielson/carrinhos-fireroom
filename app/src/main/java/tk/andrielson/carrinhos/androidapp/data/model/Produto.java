package tk.andrielson.carrinhos.androidapp.data.model;

/**
 * Created by Andrielson on 02/03/2018.
 */

public abstract class Produto extends AbsEntidadePadrao {

    public abstract Long getCodigo();

    public abstract void setCodigo(Long codigo);

    public abstract String getNome();

    public abstract void setNome(String nome);

    public abstract String getSigla();

    public abstract void setSigla(String sigla);

    public abstract Long getPreco();

    public abstract void setPreco(Long preco);

    public abstract Boolean getAtivo();

    public abstract void setAtivo(Boolean ativo);

}
