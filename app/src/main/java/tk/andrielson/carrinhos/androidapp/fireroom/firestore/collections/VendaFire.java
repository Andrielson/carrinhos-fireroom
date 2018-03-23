package tk.andrielson.carrinhos.androidapp.fireroom.firestore.collections;


import android.support.annotation.NonNull;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import tk.andrielson.carrinhos.androidapp.data.model.ItemVenda;
import tk.andrielson.carrinhos.androidapp.fireroom.firestore.dao.FirestoreDao;
import tk.andrielson.carrinhos.androidapp.fireroom.model.VendaImpl;

public final class VendaFire {
    public static final String COLECAO = "vendas";
    public static final String CODIGO = "codigo";
    public static final String COMISSAO = "comissao";
    public static final String DATA = "data";
    public static final String VALOR_TOTAL = "valor_total";
    public static final String VALOR_PAGO = "valor_pago";
    public static final String VALOR_COMISSAO = "valor_comissao";
    public static final String VENDEDOR = "vendedor";
    public static final String VENDEDOR_NOME = "vendedor_nome";
    public static final String STATUS = "status";
    public static final String ITENS = "itens";

    public Long codigo;
    public Date data;
    public Integer comissao;
    public Long valor_total;
    public Long valor_pago;
    public Long valor_comissao;
    public DocumentReference vendedor;
    public String vendedor_nome;
    public String status;
    public CollectionReference itens;

    public VendaFire() {
    }

    public VendaFire(@NonNull VendaImpl venda) {
        this.codigo = venda.getCodigo();
        this.data = venda.getData();
        this.comissao = venda.getComissao();
        this.status = venda.getStatus();
        this.vendedor_nome = venda.getVendedor().getNome();
        this.vendedor = FirebaseFirestore.getInstance().collection(VendedorFire.COLECAO).document(FirestoreDao.getIdFromCodigo(venda.getVendedor().getCodigo()));
        valor_total = 0L;
        for (ItemVenda itv : venda.getItens()) {
            int saiu = itv.getQtSaiu() == null ? 0 : itv.getQtSaiu();
            int voltou = itv.getQtVoltou() == null ? 0 : itv.getQtVoltou();
            long valor = itv.getValor() == null ? 0L : itv.getValor();
            valor_total += valor * (saiu - voltou);
        }
        valor_comissao = valor_total * comissao / 100;
        valor_pago = valor_total - valor_comissao;
        this.itens = FirebaseFirestore.getInstance().collection(String.format("/%s/%s/%s", COLECAO, FirestoreDao.getIdFromCodigo(codigo), ItemVendaFire.COLECAO));
    }
}
