package tk.andrielson.carrinhos.androidapp.data.model;

import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Created by Andrielson on 02/03/2018.
 */

public interface Vendedor {

    public Long getCodigo();

    public void setCodigo(Long codigo);

    public String getNome();

    public void setNome(String nome);

    public Integer getComissao();

    public void setComissao(Integer comissao);

    public Boolean getAtivo();

    public void setAtivo(Boolean ativo);
}
