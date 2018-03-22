package tk.andrielson.carrinhos.androidapp.fireroom;

import android.util.Log;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import tk.andrielson.carrinhos.androidapp.fireroom.firestore.FirestoreQueryLiveData;
import tk.andrielson.carrinhos.androidapp.fireroom.model.ProdutoImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.model.VendaImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.model.VendedorImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.room.AppDatabase;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.ProdutoDaoRoom;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.VendaDaoRoom;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.VendedorDaoRoom;
import tk.andrielson.carrinhos.androidapp.fireroom.room.entities.ProdutoRoom;
import tk.andrielson.carrinhos.androidapp.fireroom.room.entities.VendaRoom;
import tk.andrielson.carrinhos.androidapp.fireroom.room.entities.VendedorRoom;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;

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
                    List<ProdutoRoom> adicionados = null;
                    List<ProdutoRoom> modificados = null;
                    List<ProdutoRoom> excluidos = null;
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        ProdutoImpl objeto = dc.getDocument().toObject(ProdutoImpl.class);
                        switch (dc.getType()) {
                            case ADDED:
                                if (adicionados == null) adicionados = new ArrayList<>();
                                adicionados.add(new ProdutoRoom(objeto));
                                LogUtil.Log(TAG, "Objeto adicionado!", Log.DEBUG);
                                break;
                            case MODIFIED:
                                if (modificados == null) modificados = new ArrayList<>();
                                modificados.add(new ProdutoRoom(objeto));
                                LogUtil.Log(TAG, "Objeto modificado!", Log.DEBUG);
                                break;
                            case REMOVED:
                                if (excluidos == null) excluidos = new ArrayList<>();
                                excluidos.add(new ProdutoRoom(objeto));
                                LogUtil.Log(TAG, "Objeto excluído!", Log.DEBUG);
                                break;
                        }
                    }
                    ProdutoRoom[] modelo = new ProdutoRoom[0];
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
                    List<VendedorRoom> adicionados = null;
                    List<VendedorRoom> modificados = null;
                    List<VendedorRoom> excluidos = null;
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        VendedorImpl vendedor = dc.getDocument().toObject(VendedorImpl.class);
                        switch (dc.getType()) {
                            case ADDED:
                                if (adicionados == null) adicionados = new ArrayList<>();
                                adicionados.add(new VendedorRoom(vendedor));
                                LogUtil.Log(TAG, "Vendedor adicionado: " + vendedor.getNome(), Log.DEBUG);
                                break;
                            case MODIFIED:
                                if (modificados == null) modificados = new ArrayList<>();
                                modificados.add(new VendedorRoom(vendedor));
                                LogUtil.Log(TAG, "Vendedor modificado: " + vendedor.getNome(), Log.DEBUG);
                                break;
                            case REMOVED:
                                if (excluidos == null) excluidos = new ArrayList<>();
                                excluidos.add(new VendedorRoom(vendedor));
                                LogUtil.Log(TAG, "Vendedor excluído: " + vendedor.getNome(), Log.DEBUG);
                                break;
                        }
                    }
                    VendedorRoom[] modelo = new VendedorRoom[0];
                    if (adicionados != null && !adicionados.isEmpty())
                        dao.insert(adicionados.toArray(modelo));
                    if (modificados != null && !modificados.isEmpty())
                        dao.update(modificados.toArray(modelo));
                    if (excluidos != null && !excluidos.isEmpty())
                        dao.delete(excluidos.toArray(modelo));
                });
        });
    }

    private void sincronizaVendas() {
        FirestoreQueryLiveData liveData = new FirestoreQueryLiveData(firestore.collection(VendaImpl.COLECAO), true);
        liveData.observeForever(snapshots -> {
            if (snapshots != null)
                Executors.newSingleThreadExecutor().execute(() -> {
                    VendaDaoRoom dao = database.vendaDao();
                    List<VendaRoom> adicionados = null;
                    List<VendaRoom> modificados = null;
                    List<VendaRoom> excluidos = null;
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        VendaImpl venda = new VendaImpl();
                        DocumentSnapshot doc = dc.getDocument();
                        venda.setCodigo(doc.getLong(VendaImpl.CODIGO));
                        venda.setComissao(doc.getLong(VendaImpl.COMISSAO).intValue());
                        venda.setData(doc.getDate(VendaImpl.DATA));
                        venda.setStatus(doc.getString(VendaImpl.STATUS));
                        VendedorImpl vendedor = new VendedorImpl();
                        vendedor.setCodigo(Long.valueOf(doc.getDocumentReference(VendaImpl.VENDEDOR).getId()));
                        venda.setVendedor(vendedor);
                        switch (dc.getType()) {
                            case ADDED:
                                if (adicionados == null) adicionados = new ArrayList<>();
                                adicionados.add(new VendaRoom(venda));
                                LogUtil.Log(TAG, "Objeto adicionado!", Log.DEBUG);
                                break;
                            case MODIFIED:
                                if (modificados == null) modificados = new ArrayList<>();
                                modificados.add(new VendaRoom(venda));
                                LogUtil.Log(TAG, "Objeto modificado!", Log.DEBUG);
                                break;
                            case REMOVED:
                                if (excluidos == null) excluidos = new ArrayList<>();
                                excluidos.add(new VendaRoom(venda));
                                LogUtil.Log(TAG, "Objeto excluído!", Log.DEBUG);
                                break;
                        }
                    }
                    VendaRoom[] modelo = new VendaRoom[0];
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
