package tk.andrielson.carrinhos.androidapp.data.model;

/**
 * Created by Andrielson on 02/03/2018.
 */

public interface Produto {

    Long getCodigo();

    void setCodigo(Long codigo);

    String getNome();

    void setNome(String nome);

    String getSigla();

    void setSigla(String sigla);

    Double getPreco();

    void setPreco(Double preco);

    Boolean getAtivo();

    void setAtivo(Boolean ativo);

}
