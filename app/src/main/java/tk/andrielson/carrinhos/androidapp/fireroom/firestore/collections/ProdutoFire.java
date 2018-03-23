package tk.andrielson.carrinhos.androidapp.fireroom.firestore.collections;


import tk.andrielson.carrinhos.androidapp.fireroom.model.ProdutoImpl;

public final class ProdutoFire {
    public static final String COLECAO = "produtos";
    public static final String CODIGO = "codigo";
    public static final String NOME = "nome";
    public static final String SIGLA = "sigla";
    public static final String PRECO = "preco";
    public static final String ATIVO = "ativo";
    public static final String EXCLUIDO = "excluido";

    public Long codigo;
    public String nome;
    public String sigla;
    public Long preco;
    public Boolean ativo = Boolean.TRUE;
    public Boolean excluido = Boolean.FALSE;

    public ProdutoFire() {
    }

    public ProdutoFire(ProdutoImpl produto) {
        this.codigo = produto.getCodigo();
        this.nome = produto.getNome();
        this.sigla = produto.getSigla();
        this.preco = produto.getPreco();
        this.ativo = produto.getAtivo();
        this.excluido = false;
    }
}
