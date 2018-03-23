package tk.andrielson.carrinhos.androidapp.fireroom;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import tk.andrielson.carrinhos.androidapp.fireroom.firestore.FirestoreQueryLiveData;
import tk.andrielson.carrinhos.androidapp.fireroom.firestore.collections.ItemVendaFirestore;
import tk.andrielson.carrinhos.androidapp.fireroom.firestore.collections.ProdutoFirestore;
import tk.andrielson.carrinhos.androidapp.fireroom.firestore.collections.VendaFirestore;
import tk.andrielson.carrinhos.androidapp.fireroom.firestore.collections.VendedorFirestore;
import tk.andrielson.carrinhos.androidapp.fireroom.firestore.dao.FirestoreDao;
import tk.andrielson.carrinhos.androidapp.fireroom.room.AppDatabase;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.ItemVendaDaoRoom;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.ProdutoDaoRoom;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.VendaDaoRoom;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.VendedorDaoRoom;
import tk.andrielson.carrinhos.androidapp.fireroom.room.entities.ItemVendaRoom;
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
        sincronizaVendas();
    }

    @Contract(pure = true)
    public static SincronizadorFirestoreRoom getInstancia() {
        return InnerStaticHolder.INSTANCIA;
    }

    private void sincronizaProdutos() {
        FirestoreQueryLiveData liveData = new FirestoreQueryLiveData(firestore.collection(ProdutoFirestore.COLECAO), true);
        liveData.observeForever(snapshots -> {
            if (snapshots != null)
                Executors.newSingleThreadExecutor().execute(() -> {
                    ProdutoDaoRoom dao = database.produtoDao();
                    List<ProdutoRoom> adicionados = null;
                    List<ProdutoRoom> modificados = null;
                    List<ProdutoRoom> excluidos = null;
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        ProdutoFirestore produto = dc.getDocument().toObject(ProdutoFirestore.class);
                        switch (dc.getType()) {
                            case ADDED:
                                if (adicionados == null) adicionados = new ArrayList<>();
                                adicionados.add(new ProdutoRoom(produto));
                                LogUtil.Log(TAG, "Produto adicionado: " + produto.nome, Log.DEBUG);
                                break;
                            case MODIFIED:
                                if (modificados == null) modificados = new ArrayList<>();
                                modificados.add(new ProdutoRoom(produto));
                                LogUtil.Log(TAG, "Produto modificado: " + produto.nome, Log.DEBUG);
                                break;
                            case REMOVED:
                                if (excluidos == null) excluidos = new ArrayList<>();
                                excluidos.add(new ProdutoRoom(produto));
                                LogUtil.Log(TAG, "Produto excluído: " + produto.nome, Log.DEBUG);
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
        FirestoreQueryLiveData liveData = new FirestoreQueryLiveData(firestore.collection(VendedorFirestore.COLECAO), true);
        liveData.observeForever(snapshots -> {
            if (snapshots != null)
                Executors.newSingleThreadExecutor().execute(() -> {
                    VendedorDaoRoom dao = database.vendedorDao();
                    List<VendedorRoom> adicionados = null;
                    List<VendedorRoom> modificados = null;
                    List<VendedorRoom> excluidos = null;
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        VendedorFirestore vendedor = dc.getDocument().toObject(VendedorFirestore.class);
                        switch (dc.getType()) {
                            case ADDED:
                                if (adicionados == null) adicionados = new ArrayList<>();
                                adicionados.add(new VendedorRoom(vendedor));
                                LogUtil.Log(TAG, "Vendedor adicionado: " + vendedor.nome, Log.DEBUG);
                                break;
                            case MODIFIED:
                                if (modificados == null) modificados = new ArrayList<>();
                                modificados.add(new VendedorRoom(vendedor));
                                LogUtil.Log(TAG, "Vendedor modificado: " + vendedor.nome, Log.DEBUG);
                                break;
                            case REMOVED:
                                if (excluidos == null) excluidos = new ArrayList<>();
                                excluidos.add(new VendedorRoom(vendedor));
                                LogUtil.Log(TAG, "Vendedor excluído: " + vendedor.nome, Log.DEBUG);
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
        FirestoreQueryLiveData liveData = new FirestoreQueryLiveData(firestore.collection(VendaFirestore.COLECAO), true);
        liveData.observeForever(snapshots -> {
            if (snapshots != null)
                Executors.newSingleThreadExecutor().execute(() -> {
                    VendaDaoRoom vendaDao = database.vendaDao();
                    ItemVendaDaoRoom itemVendaDao = database.itemVendaDao();
                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        VendaFirestore venda = dc.getDocument().toObject(VendaFirestore.class);
                        CollectionReference itens;
                        switch (dc.getType()) {
                            case ADDED:
                                itens = FirebaseFirestore.getInstance().collection(String.format("/%s/%s/%s", VendaFirestore.COLECAO, FirestoreDao.getIdFromCodigo(venda.codigo), ItemVendaFirestore.COLECAO));
                                itens.get().addOnSuccessListener(itemSnapshots -> {
                                    if (itemSnapshots != null) {
                                        Executors.newSingleThreadExecutor().execute(() -> {
                                            vendaDao.insert(new VendaRoom(venda));
                                            List<ItemVendaRoom> lista = new ArrayList<>();
                                            for (DocumentSnapshot doc : itemSnapshots.getDocuments())
                                                lista.add(new ItemVendaRoom(doc.toObject(ItemVendaFirestore.class), venda.codigo));
                                            itemVendaDao.insert(lista.toArray(new ItemVendaRoom[0]));
                                            LogUtil.Log(TAG, "Venda adicionada: " + venda.codigo, Log.DEBUG);
                                        });
                                    }
                                });
                                break;
                            case MODIFIED:
                                itens = FirebaseFirestore.getInstance().collection(String.format("/%s/%s/%s", VendaFirestore.COLECAO, FirestoreDao.getIdFromCodigo(venda.codigo), ItemVendaFirestore.COLECAO));
                                itens.get().addOnSuccessListener(itemSnapshots -> {
                                    if (itemSnapshots != null) {
                                        Executors.newSingleThreadExecutor().execute(() -> {
                                            vendaDao.update(new VendaRoom(venda));
                                            List<ItemVendaRoom> lista = new ArrayList<>();
                                            for (DocumentSnapshot doc : itemSnapshots.getDocuments())
                                                lista.add(new ItemVendaRoom(doc.toObject(ItemVendaFirestore.class), venda.codigo));
                                            itemVendaDao.replace(lista.toArray(new ItemVendaRoom[0]), venda.codigo);
                                            LogUtil.Log(TAG, "Venda modificada: " + venda.codigo, Log.DEBUG);
                                        });
                                    }
                                });
                                break;
                            case REMOVED:
                                vendaDao.delete(new VendaRoom(venda));
                                LogUtil.Log(TAG, "Venda excluída: " + venda.codigo, Log.DEBUG);
                                break;
                        }
                    }
                });
        });
    }

    private static class InnerStaticHolder {
        static final SincronizadorFirestoreRoom INSTANCIA = new SincronizadorFirestoreRoom();
    }
}
