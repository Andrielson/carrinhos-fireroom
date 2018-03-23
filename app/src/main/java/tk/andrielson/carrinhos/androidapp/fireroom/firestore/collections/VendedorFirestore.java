package tk.andrielson.carrinhos.androidapp.fireroom.firestore.collections;


import tk.andrielson.carrinhos.androidapp.fireroom.model.VendedorImpl;

public final class VendedorFirestore {
    public static final String COLECAO = "vendedores";
    public static final String CODIGO = "codigo";
    public static final String NOME = "nome";
    public static final String COMISSAO = "comissao";
    public static final String ATIVO = "ativo";
    public static final String EXCLUIDO = "excluido";

    public Long codigo;
    public String nome;
    public Integer comissao;
    public Boolean ativo;
    public Boolean excluido;

    public VendedorFirestore() {
    }

    public VendedorFirestore(VendedorImpl vendedor) {
        this.codigo = vendedor.getCodigo();
        this.nome = vendedor.getNome();
        this.comissao = vendedor.getComissao();
        this.ativo = vendedor.getAtivo();
        this.excluido = false;
    }
}
