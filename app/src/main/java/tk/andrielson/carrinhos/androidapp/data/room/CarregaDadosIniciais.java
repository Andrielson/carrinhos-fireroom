package tk.andrielson.carrinhos.androidapp.data.room;

import java.math.BigDecimal;

import tk.andrielson.carrinhos.androidapp.CarrinhosApp;
import tk.andrielson.carrinhos.androidapp.data.room.model.ProdutoRoom;

/**
 * Created by Andrielson on 02/03/2018.
 */

public final class CarregaDadosIniciais {
    private static AppDatabase database = AppDatabase.getDatabase(CarrinhosApp.getContext());

    public static void produtos() {
        database.produtoRoomDao().deleteAll();
        for (long i = 1; i < 30; i++) {
            ProdutoRoom produto = new ProdutoRoom();
            produto.setCodigo(i);
            produto.setNome("Produto " + i);
            produto.setSigla("P" + i);
            produto.setPreco(new BigDecimal(i));
            database.produtoRoomDao().insert(produto);
        }
    }
}
