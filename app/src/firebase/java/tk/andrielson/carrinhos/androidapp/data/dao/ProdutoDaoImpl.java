package tk.andrielson.carrinhos.androidapp.data.dao;

import android.annotation.SuppressLint;
import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import tk.andrielson.carrinhos.androidapp.data.model.Produto;
import tk.andrielson.carrinhos.androidapp.data.model.ProdutoImpl;

/**
 * Created by anfesilva on 07/03/2018.
 */
@SuppressLint("DefaultLocale")
public final class ProdutoDaoImpl extends FirestoreDao implements ProdutoDao {

    private static final String COLECAO = ProdutoImpl.COLLECTION;
    private static final String TAG = ProdutoDaoImpl.class.getSimpleName();
    private final Query queryPadrao;

    public ProdutoDaoImpl() {
        super();
        collectionReference = db.collection(COLECAO);
        queryPadrao = collectionReference.whereEqualTo(ProdutoImpl.ATIVO, true);
    }

    @Override
    public long insert(Produto produto) {
        //TODO: implementar coleção para armazenar e recuperar IDs
        Task<QuerySnapshot> task = collectionReference.get();
        QuerySnapshot snapshot;
        try {
            //FIXME: não se pode executar Tasks.await da thread principal
            snapshot = Tasks.await(task);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return 0;
        }
        Long novoCodigo = snapshot.isEmpty() ? Long.valueOf("1") : Long.valueOf(snapshot.size() + 1);
        produto.setCodigo(novoCodigo);
        String id = String.format("%020d", novoCodigo);
        collectionReference.document(id).set(produto);
        return novoCodigo;
    }

    @Override
    public int update(Produto produto) {
        return 0;
    }

    @Override
    public int delete(Produto produto) {
        return 0;
    }

    @Override
    public LiveData<Produto> getByCodigo(final Long codigo) {
        Query query = queryPadrao.whereEqualTo("codigo", codigo);
        FirestoreQueryLiveData liveData = new FirestoreQueryLiveData(query);
        return Transformations.map(liveData, new ObjetoDeserializer());
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public LiveData<List<Produto>> getAll() {
        FirestoreQueryLiveData liveData = new FirestoreQueryLiveData(queryPadrao);
        return Transformations.map(liveData, new ListaDeserializer());
    }

    private class ListaDeserializer implements Function<QuerySnapshot, List<Produto>> {
        @Override
        public List<Produto> apply(QuerySnapshot input) {
            List<Produto> lista = new ArrayList<>();
            for (DocumentSnapshot doc : input.getDocuments()) {
                lista.add(doc.toObject(ProdutoImpl.class));
            }
            return lista;
        }
    }

    private class ObjetoDeserializer implements Function<QuerySnapshot, Produto> {
        @Override
        public ProdutoImpl apply(QuerySnapshot input) {
            return input.getDocuments().get(0).toObject(ProdutoImpl.class);
        }
    }

}
