package tk.andrielson.carrinhos.androidapp.fireroom;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import tk.andrielson.carrinhos.androidapp.fireroom.firestore.FirestoreQueryLiveData;
import tk.andrielson.carrinhos.androidapp.fireroom.firestore.dao.FirestoreDao;
import tk.andrielson.carrinhos.androidapp.fireroom.model.ItemVendaImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.model.ProdutoImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.model.VendaImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.model.VendedorImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.room.AppDatabase;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.ItemVendaRoomDao;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.ProdutoRoomDao;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.VendaRoomDao;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.VendedorRoomDao;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;

public final class SincronizadorFirestoreRoom {

    private static final String TAG = SincronizadorFirestoreRoom.class.getSimpleName();
    private final AppDatabase database = AppDatabase.getInstancia();
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final Executor executor = Executors.newFixedThreadPool(5);

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
        FirestoreQueryLiveData liveData = new FirestoreQueryLiveData(firestore.collection(ProdutoImpl.COLECAO), true);
        liveData.observeForever(snapshots -> {
            if (snapshots == null)
                return;
            executor.execute(() -> {
                ProdutoRoomDao dao = database.produtoDao();
                List<ProdutoImpl> adicionados = null;
                List<ProdutoImpl> modificados = null;
                List<ProdutoImpl> excluidos = null;
                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    ProdutoImpl produto = dc.getDocument().toObject(ProdutoImpl.class);
                    switch (dc.getType()) {
                        case ADDED:
                            if (adicionados == null) adicionados = new ArrayList<>();
                            adicionados.add(produto);
                            LogUtil.Log(TAG, "Produto adicionado: " + produto.getNome(), Log.DEBUG);
                            break;
                        case MODIFIED:
                            if (modificados == null) modificados = new ArrayList<>();
                            modificados.add(produto);
                            LogUtil.Log(TAG, "Produto modificado: " + produto.getNome(), Log.DEBUG);
                            break;
                        case REMOVED:
                            if (excluidos == null) excluidos = new ArrayList<>();
                            excluidos.add(produto);
                            LogUtil.Log(TAG, "Produto excluído: " + produto.getNome(), Log.DEBUG);
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
            if (snapshots == null)
                return;
            executor.execute(() -> {
                VendedorRoomDao dao = database.vendedorDao();
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

    private void sincronizaVendas() {
        FirestoreQueryLiveData liveData = new FirestoreQueryLiveData(firestore.collection(VendaImpl.COLECAO), true);
        liveData.observeForever(snapshots -> {
            if (snapshots == null)
                return;
            executor.execute(() -> {
                VendaRoomDao vendaDao = database.vendaDao();
                ItemVendaRoomDao itemVendaDao = database.itemVendaDao();
                for (DocumentChange dc : snapshots.getDocumentChanges()) {
                    VendaImpl venda = dc.getDocument().toObject(VendaImpl.class);
                    CollectionReference itens;
                    switch (dc.getType()) {
                        case ADDED:
                            itens = firestore.collection(String.format("/%s/%s/%s", VendaImpl.COLECAO, FirestoreDao.getIdFromCodigo(venda.getCodigo()), ItemVendaImpl.COLECAO));
                            itens.get().addOnSuccessListener(itemSnapshots -> {
                                if (itemSnapshots != null) {
                                    executor.execute(() -> {
                                        vendaDao.insert(venda);
                                        List<ItemVendaImpl> lista = new ArrayList<>();
                                        for (DocumentSnapshot doc : itemSnapshots.getDocuments()) {
                                            ItemVendaImpl itvenda = doc.toObject(ItemVendaImpl.class);
                                            itvenda.setVendaCodigo(venda.getCodigo());
                                            lista.add(itvenda);
                                        }
                                        itemVendaDao.insert(lista.toArray(new ItemVendaImpl[0]));
                                        LogUtil.Log(TAG, "Venda adicionada: " + venda.getCodigo(), Log.DEBUG);
                                    });
                                }
                            });
                            break;
                        case MODIFIED:
                            itens = firestore.collection(String.format("/%s/%s/%s", VendaImpl.COLECAO, FirestoreDao.getIdFromCodigo(venda.getCodigo()), ItemVendaImpl.COLECAO));
                            itens.get().addOnSuccessListener(itemSnapshots -> {
                                if (itemSnapshots != null) {
                                    executor.execute(() -> {
                                        vendaDao.update(venda);
                                        List<ItemVendaImpl> lista = new ArrayList<>();
                                        for (DocumentSnapshot doc : itemSnapshots.getDocuments()) {
                                            ItemVendaImpl itvenda = doc.toObject(ItemVendaImpl.class);
                                            itvenda.setVendaCodigo(venda.getCodigo());
                                            lista.add(itvenda);
                                        }
                                        itemVendaDao.replace(lista.toArray(new ItemVendaImpl[0]), venda.getCodigo());
                                        LogUtil.Log(TAG, "Venda modificada: " + venda.getCodigo(), Log.DEBUG);
                                    });
                                }
                            });
                            break;
                        case REMOVED:
                            vendaDao.delete(venda);
                            LogUtil.Log(TAG, "Venda excluída: " + venda.getCodigo(), Log.DEBUG);
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
