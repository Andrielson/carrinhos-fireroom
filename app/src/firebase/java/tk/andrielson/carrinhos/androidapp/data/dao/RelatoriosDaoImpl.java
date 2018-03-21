package tk.andrielson.carrinhos.androidapp.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Date;

import tk.andrielson.carrinhos.androidapp.data.model.VendaImpl;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;

public final class RelatoriosDaoImpl {

    private static final String TAG = RelatoriosDaoImpl.class.getSimpleName();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @NonNull
    public LiveData<SimpleArrayMap<Date, SimpleArrayMap<String, Long>>> vendasPorDia() {
        Query query = db.collection(VendaImpl.COLECAO).orderBy(VendaImpl.DATA, Query.Direction.DESCENDING);
        FirestoreQueryLiveData liveData = new FirestoreQueryLiveData(query);
        return Transformations.map(liveData, input -> {
            if (input == null)
                return null;
            SimpleArrayMap<Date, SimpleArrayMap<String, Long>> simpleArrayMap = new SimpleArrayMap<>(input.size());
            for (DocumentSnapshot docSnap : input.getDocuments()) {
                Date data = docSnap.getDate(VendaImpl.DATA);
                SimpleArrayMap<String, Long> arrayMap = simpleArrayMap.containsKey(data) ? simpleArrayMap.get(data) : new SimpleArrayMap<>(3);
                Long total = arrayMap.containsKey("total") ? arrayMap.get("total") : 0L;
                total += docSnap.getLong(VendaImpl.TOTAL);
                arrayMap.put("total", total);
                Long pago = arrayMap.containsKey("pago") ? arrayMap.get("pago") : 0L;
                pago += docSnap.getLong(VendaImpl.TOTAL) * (1 - docSnap.getLong(VendaImpl.COMISSAO) / 100L);
                arrayMap.put("pago", pago);
                Long comissao = arrayMap.containsKey("comissao") ? arrayMap.get("comissao") : 0L;
                comissao += docSnap.getLong(VendaImpl.TOTAL) * docSnap.getLong(VendaImpl.COMISSAO) / 100L;
                arrayMap.put("comissao", comissao);
                simpleArrayMap.put(data, arrayMap);
            }
            return simpleArrayMap;
        });
    }

}
