package tk.andrielson.carrinhos.androidapp.fireroom.firestore.collections;


import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import tk.andrielson.carrinhos.androidapp.data.model.ItemVenda;
import tk.andrielson.carrinhos.androidapp.fireroom.firestore.dao.FirestoreDao;

public final class ItemVendaFirestore {
    public static final String COLECAO = "itens";
    public static final String PRODUTO = "produto";
    public static final String QT_SAIU = "qt_saiu";
    public static final String QT_VENDEU = "qt_vendeu";
    public static final String QT_VOLTOU = "qt_voltou";
    public static final String VALOR = "valor";

    public DocumentReference produto;
    public Integer qt_saiu;
    public Integer qt_voltou;
    public Integer qt_vendeu;
    public Long valor;
    public Long total;
    public String produto_nome;
    public String produto_sigla;

    public ItemVendaFirestore() {
    }

    public ItemVendaFirestore(ItemVenda item) {
        this.qt_saiu = item.getQtSaiu() == null ? 0 : item.getQtSaiu();
        this.qt_voltou = item.getQtVoltou() == null ? 0 : item.getQtVoltou();
        this.qt_vendeu = qt_saiu - qt_voltou;
        this.valor = item.getValor() == null ? 0 : item.getValor();
        this.total = valor * qt_vendeu;
        this.produto_nome = item.getProduto() == null ? "" : item.getProduto().getNome();
        this.produto_sigla = item.getProduto() == null ? "" : item.getProduto().getSigla();
        this.produto = FirebaseFirestore.getInstance().collection(ProdutoFirestore.COLECAO).document(FirestoreDao.getIdFromCodigo(item.getProduto().getCodigo()));
    }
}
