package tk.andrielson.carrinhos.androidapp.data.model;

import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Created by Andrielson on 02/03/2018.
 */

public interface Produto extends Parcelable {
    public Long getCodigo();

    public void setCodigo(Long codigo);

    public String getNome();

    public void setNome(String nome);

    public String getSigla();

    public void setSigla(String sigla);

    public BigDecimal getPreco();

    public void setPreco(BigDecimal preco);

    public BigDecimal getCusto();

    public void setCusto(BigDecimal custo);

    public Boolean getAtivo();

    public void setAtivo(Boolean ativo);
}
