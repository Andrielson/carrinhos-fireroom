package tk.andrielson.carrinhos.androidapp.fireroom.room.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import tk.andrielson.carrinhos.androidapp.fireroom.firestore.collections.ProdutoFirestore;
import tk.andrielson.carrinhos.androidapp.fireroom.model.ProdutoImpl;

@Entity(tableName = "tb_produto")
public final class ProdutoRoom {
    @PrimaryKey
    public Long codigo;
    public String nome;
    public String sigla;
    public Long preco;
    public Boolean ativo;
    public Boolean excluido;

    public ProdutoRoom() {

    }

    public ProdutoRoom(ProdutoFirestore produto) {
        this.codigo = produto.codigo;
        this.nome = produto.nome;
        this.sigla = produto.sigla;
        this.preco = produto.preco;
        this.ativo = produto.ativo;
        this.excluido = produto.excluido;
    }

    public ProdutoImpl getModel() {
        ProdutoImpl produto = new ProdutoImpl();
        produto.setCodigo(codigo);
        produto.setNome(nome);
        produto.setSigla(sigla);
        produto.setPreco(preco);
        produto.setAtivo(ativo);
        return produto;
    }
}
