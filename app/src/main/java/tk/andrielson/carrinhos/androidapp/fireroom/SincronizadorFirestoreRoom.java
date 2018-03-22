package tk.andrielson.carrinhos.androidapp.fireroom;

import android.util.Log;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import tk.andrielson.carrinhos.androidapp.fireroom.firestore.FirestoreQueryLiveData;
import tk.andrielson.carrinhos.androidapp.data.model.ProdutoImpl;
import tk.andrielson.carrinhos.androidapp.data.model.VendedorImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.room.AppDatabase;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.ProdutoDaoRoom;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.VendedorDaoRoom;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;

@SuppressWarnings("unchecked")
public final class SincronizadorFirestoreRoom {

    private static final String TAG = SincronizadorFirestoreRoom.class.getSimpleName();
    private final AppDatabase database = AppDatabase.getInstancia();
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private SincronizadorFirestoreRoom() {
        sincronizaProdutos();
        sincronizaVendedores();
    }

    private void sincronizaProdutos() {
        FirestoreQueryLiveData liveData = new FirestoreQueryLiveData(firestore.collection(ProdutoImpl.COLECAO), true);
        liveData.observeForever(snapshots -> {
            if (snapshots != null)
                Executors.newSingleThreadExecutor().execute(() -> {
                    ProdutoDaoRoom dao = database.produtoDao();
                    List<ProdutoImpl> adicionados = null;
                    List<ProdutoImpl> modificados = null;
                    List<ProdutoImpl> excluidos = null;
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
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
                });
        });
    }

    private void sincronizaVendedores() {
        FirestoreQueryLiveData liveData = new FirestoreQueryLiveData(firestore.collection(VendedorImpl.COLECAO), true);
        liveData.observeForever(snapshots -> {
            if (snapshots != null)
                Executors.newSingleThreadExecutor().execute(() -> {
                    VendedorDaoRoom dao = database.vendedorDao();
                    List<VendedorImpl> adicionados = null;
                    List<VendedorImpl> modificados = null;
                    List<VendedorImpl> excluidos = null;
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        VendedorImpl vendedor = dc.getDocument().toObject(VendedorImpl.class);
                        switch (dc.getType()) {
                            case ADDED:
                                if (adicionados == null) adicionados = new ArrayList<>();
                                adicionados.add(vendedor);
                                LogUtil.Log(TAG, "Vendedor adicionado: " + vendedor.getNome(), Log.DEBUG);
                                break;
                            case MODIFIED:
                                if (modificados == null) modificados = new ArrayList<>();
                                modificados.add(vendedor);
                                LogUtil.Log(TAG, "Vendedor modificado: " + vendedor.getNome(), Log.DEBUG);
                                break;
                            case REMOVED:
                                if (excluidos == null) excluidos = new ArrayList<>();
                                excluidos.add(vendedor);
                                LogUtil.Log(TAG, "Vendedor excluído: " + vendedor.getNome(), Log.DEBUG);
                                break;
                        }
                    }
                    VendedorImpl[] modelo = new VendedorImpl[0];
                    if (adicionados != null && !adicionados.isEmpty())
                        dao.insert(adicionados.toArray(modelo));
                    if (modificados != null && !modificados.isEmpty())
                        dao.update(modificados.toArray(modelo));
                    if (excluidos != null && !excluidos.isEmpty())
                        dao.delete(excluidos.toArray(modelo));
                });
        });
    }

    @Contract(pure = true)
    public static SincronizadorFirestoreRoom getInstancia() {
        return InnerStaticHolder.INSTANCIA;
    }

    private static class InnerStaticHolder {
        static final SincronizadorFirestoreRoom INSTANCIA = new SincronizadorFirestoreRoom();
    }
}
