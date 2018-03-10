package tk.andrielson.carrinhos.androidapp;

import android.support.annotation.NonNull;

import tk.andrielson.carrinhos.androidapp.data.dao.ProdutoDao;
import tk.andrielson.carrinhos.androidapp.data.dao.ProdutoDaoImpl;
import tk.andrielson.carrinhos.androidapp.data.model.Produto;
import tk.andrielson.carrinhos.androidapp.data.model.ProdutoImpl;

/**
 * Created by Andrielson on 02/03/2018.
 */

public final class DI {
    private DI() {
        //construtor privado para impedir o instanciamento da classe.
    }

    @NonNull
    public static Produto newProduto() {
        return new ProdutoImpl();
    }

    @NonNull
    public static ProdutoDao newProdutoDao() {
        return new ProdutoDaoImpl();
    }

}
