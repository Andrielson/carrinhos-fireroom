package tk.andrielson.carrinhos.androidapp.fireroom.firestore.colections;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.Date;

public final class VendaFirestore {
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
    public Integer comissao;
    public Date data;
    public Long valor_total;
    public Long valor_pago;
    public Long valor_comissao;
    public DocumentReference vendedor;
    public String vendedor_nome;
    public String status;
    public CollectionReference itens;
}
