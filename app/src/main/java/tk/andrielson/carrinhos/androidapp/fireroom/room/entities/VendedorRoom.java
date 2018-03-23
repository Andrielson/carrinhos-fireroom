package tk.andrielson.carrinhos.androidapp.fireroom.room.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import tk.andrielson.carrinhos.androidapp.fireroom.firestore.collections.VendedorFirestore;
import tk.andrielson.carrinhos.androidapp.fireroom.model.VendedorImpl;

@Entity(tableName = "tb_vendedor")
public final class VendedorRoom {
    @PrimaryKey
    public Long codigo;
    public String nome;
    public Integer comissao;
    public Boolean ativo;
    public Boolean excluido = Boolean.FALSE;

    public VendedorRoom() {

    }

    public VendedorRoom(VendedorFirestore vendedor) {
        this.codigo = vendedor.codigo;
        this.nome = vendedor.nome;
        this.comissao = vendedor.comissao;
        this.ativo = vendedor.ativo;
        this.excluido = vendedor.excluido;
    }

    public VendedorImpl getModel() {
        VendedorImpl vendedor = new VendedorImpl();
        vendedor.setCodigo(codigo);
        vendedor.setNome(nome);
        vendedor.setComissao(comissao);
        vendedor.setAtivo(ativo);
        return vendedor;
    }
}
