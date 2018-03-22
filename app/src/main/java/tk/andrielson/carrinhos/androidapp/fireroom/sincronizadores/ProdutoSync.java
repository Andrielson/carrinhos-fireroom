package tk.andrielson.carrinhos.androidapp.fireroom.sincronizadores;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.firestore.DocumentChange;

import java.util.ArrayList;
import java.util.List;

import tk.andrielson.carrinhos.androidapp.fireroom.model.ProdutoImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.room.AppDatabase;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.ProdutoDaoRoom;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;

/**
 * Created by anfesilva on 22/03/2018.
 */

public final class ProdutoSync {

    private static final String TAG = ProdutoSync.class.getSimpleName();
    private static final ProdutoDaoRoom dao = AppDatabase.getInstancia().produtoDao();

    public static void sincronizaRoom(@NonNull List<DocumentChange> documentChangeList) {
        List<ProdutoImpl> adicionados = null;
        List<ProdutoImpl> modificados = null;
        List<ProdutoImpl> excluidos = null;
        for (DocumentChange dc : documentChangeList) {
            ProdutoImpl objeto = dc.getDocument().toObject(ProdutoImpl.class);
            switch (dc.getType()) {
                case ADDED:
                    if (adicionados == null) adicionados = new ArrayList<>();
                    adicionados.add(objeto);
                    LogUtil.Log(TAG, "Objeto adicionado!", Log.DEBUG);
                    break;
                case MODIFIED:
                    if (modificados == null) modificados = new ArrayList<>();
                    modificados.add(objeto);
                    LogUtil.Log(TAG, "Objeto modificado!", Log.DEBUG);
                    break;
                case REMOVED:
                    if (excluidos == null) excluidos = new ArrayList<>();
                    excluidos.add(objeto);
                    LogUtil.Log(TAG, "Objeto excluído!", Log.DEBUG);
                    break;
            }
        }
        ProdutoImpl[] modelo = new ProdutoImpl[0];
        if (adicionados != null && !adicionados.isEmpty())
            dao.insert(adicionados.toArray(modelo));
        if (modificados != null && !modificados.isEmpty())
            dao.update(modificados.toArray(modelo));
        if (excluidos != null && !excluidos.isEmpty())
            dao.delete(excluidos.toArray(modelo));
    }
}
