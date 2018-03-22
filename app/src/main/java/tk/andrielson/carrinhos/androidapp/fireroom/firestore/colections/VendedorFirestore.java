package tk.andrielson.carrinhos.androidapp.fireroom.firestore.colections;


public final class VendedorFirestore {
    public static final String COLECAO = "vendedores";
    public static final String CODIGO = "codigo";
    public static final String NOME = "nome";
    public static final String COMISSAO = "comissao";
    public static final String ATIVO = "ativo";
    public static final String EXCLUIDO = "excluido";

    private Long codigo = 0L;
    private String nome;
    private Integer comissao;
    private Boolean ativo = Boolean.TRUE;
    private Boolean excluido = Boolean.FALSE;
}
