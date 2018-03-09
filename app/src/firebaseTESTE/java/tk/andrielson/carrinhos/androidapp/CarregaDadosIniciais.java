package tk.andrielson.carrinhos.androidapp;

import com.google.firebase.firestore.FirebaseFirestore;

import tk.andrielson.carrinhos.androidapp.data.model.ProdutoImpl;

/**
 * Created by Andrielson on 03/03/2018.
 */

public class CarregaDadosIniciais {

    private static final FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    public static void produtos() {
        for (long i = 1; i < 30; i++) {
            ProdutoImpl produto = new ProdutoImpl();
            produto.setCodigo(i);
            produto.setNome("ProdutoImpl " + i);
            produto.setSigla("P" + i);
            produto.setPreco(200L);
            mFirestore.collection(ProdutoImpl.COLLECTION).add(produto);
        }
    }
}
