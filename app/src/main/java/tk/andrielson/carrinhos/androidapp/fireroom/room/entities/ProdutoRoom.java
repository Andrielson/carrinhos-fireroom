package tk.andrielson.carrinhos.androidapp.fireroom.room.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import tk.andrielson.carrinhos.androidapp.fireroom.firestore.collections.ProdutoFirestore;
import tk.andrielson.carrinhos.androidapp.fireroom.model.ProdutoImpl;

@Entity(tableName = "tb_produto")
public final class ProdutoRoom {
    @PrimaryKey
    @ColumnInfo(name = "produto_codigo")
    public Long codigo;
    @ColumnInfo(name = "produto_nome")
    public String nome;
    @ColumnInfo(name = "produto_sigla")
    public String sigla;
    @ColumnInfo(name = "produto_preco")
    public Long preco;
    @ColumnInfo(name = "produto_ativo")
    public Boolean ativo;
    @ColumnInfo(name = "produto_excluido")
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
