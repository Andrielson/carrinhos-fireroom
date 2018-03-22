package tk.andrielson.carrinhos.androidapp.fireroom.firestore.colections;


public final class ProdutoFirestore {
    public static final String COLECAO = "produtos";
    public static final String CODIGO = "codigo";
    public static final String NOME = "nome";
    public static final String SIGLA = "sigla";
    public static final String PRECO = "preco";
    public static final String ATIVO = "ativo";
    public static final String EXCLUIDO = "excluido";

    public Long codigo = 0L;
    public String nome;
    public String sigla;
    public Long preco = 0L;
    public Boolean ativo = Boolean.TRUE;
    public Boolean excluido = Boolean.FALSE;
}
