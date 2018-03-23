package tk.andrielson.carrinhos.androidapp.fireroom.room.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import tk.andrielson.carrinhos.androidapp.fireroom.firestore.collections.VendedorFire;
import tk.andrielson.carrinhos.androidapp.fireroom.model.VendedorImpl;

@Entity(tableName = "tb_vendedor")
public final class VendedorRoom {
    @PrimaryKey
    @ColumnInfo(name = "vendedor_codigo")
    public Long codigo;
    @ColumnInfo(name = "vendedor_nome")
    public String nome;
    @ColumnInfo(name = "vendedor_comissao")
    public Integer comissao;
    @ColumnInfo(name = "vendedor_ativo")
    public Boolean ativo;
    @ColumnInfo(name = "vendedor_excluido")
    public Boolean excluido = Boolean.FALSE;

    public VendedorRoom() {

    }

    public VendedorRoom(VendedorFire vendedor) {
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
