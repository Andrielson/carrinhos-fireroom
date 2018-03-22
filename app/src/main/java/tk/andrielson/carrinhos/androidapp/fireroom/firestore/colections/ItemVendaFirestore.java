package tk.andrielson.carrinhos.androidapp.fireroom.firestore.colections;


import com.google.firebase.firestore.DocumentReference;

public final class ItemVendaFirestore {
    public static final String COLECAO = "itens";
    public static final String PRODUTO = "produto";
    public static final String QT_SAIU = "qt_saiu";
    public static final String QT_VENDEU = "qt_vendeu";
    public static final String QT_VOLTOU = "qt_voltou";
    public static final String VALOR = "valor";

    private DocumentReference produto;
    private Long qtSaiu;
    private Long qtVoltou;
    private Long qtVendeu;
    private Long valor;
    private String produto_nome;
    private String produto_sigla;
}
