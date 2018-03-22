package tk.andrielson.carrinhos.androidapp.fireroom.room.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import tk.andrielson.carrinhos.androidapp.fireroom.model.VendedorImpl;

@Entity(tableName = "tb_vendedor")
public class VendedorRoom {
    @PrimaryKey
    public Long codigo;
    public String nome;
    public Integer comissao;
    public Boolean ativo;
    public Boolean excluido;

    public VendedorRoom() {

    }

    public VendedorRoom(VendedorImpl vendedor) {
        this.codigo = vendedor.getCodigo();
        this.nome = vendedor.getNome();
        this.comissao = vendedor.getComissao();
        this.ativo = vendedor.getAtivo();
        this.excluido = false;
    }
}
