package tk.andrielson.carrinhos.androidapp;

import android.support.annotation.NonNull;

import tk.andrielson.carrinhos.androidapp.data.model.Produto;
import tk.andrielson.carrinhos.androidapp.data.room.model.ProdutoRoom;

/**
 * Created by Andrielson on 02/03/2018.
 */

public final class DI {
    private DI() {
        //construtor privado para impedir o instanciamento da classe.
    }

    @NonNull
    public static Produto newProduto() {
        return new ProdutoRoom();
    }
}
