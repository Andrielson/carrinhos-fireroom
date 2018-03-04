package tk.andrielson.carrinhos.androidapp.data.model;

import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Created by Andrielson on 02/03/2018.
 */

public interface Vendedor extends Parcelable {

    public Long getCodigo();

    public void setCodigo(Long codigo);

    public String getNome();

    public void setNome(String nome);

    public BigDecimal getComissao();

    public void setComissao(BigDecimal comissao);

    public Boolean getAtivo();

    public void setAtivo(Boolean ativo);
}
