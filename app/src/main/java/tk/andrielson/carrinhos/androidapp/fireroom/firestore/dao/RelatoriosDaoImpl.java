package tk.andrielson.carrinhos.androidapp.fireroom.firestore.dao;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import tk.andrielson.carrinhos.androidapp.fireroom.firestore.FirestoreQueryLiveData;
import tk.andrielson.carrinhos.androidapp.fireroom.model.VendaImpl;

public final class RelatoriosDaoImpl {

    private static final String TAG = RelatoriosDaoImpl.class.getSimpleName();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @NonNull
    public LiveData<SimpleArrayMap<Date, SimpleArrayMap<String, Long>>> vendasPorDia() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date inicio;
        Date fim;
        try {
            inicio = dateFormat.parse("15/01/2018");
        } catch (ParseException e) {
            inicio = Calendar.getInstance().getTime();
            e.printStackTrace();
        }
        Query query = db.collection(VendaImpl.COLECAO).whereGreaterThanOrEqualTo(VendaImpl.DATA, inicio).orderBy(VendaImpl.DATA, Query.Direction.DESCENDING);
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
                Long comissao = arrayMap.containsKey("comissao") ? arrayMap.get("comissao") : 0L;
                comissao += docSnap.getLong(VendaImpl.TOTAL) * docSnap.getLong(VendaImpl.COMISSAO) / 100L;
                arrayMap.put("comissao", comissao);
                Long pago = total - comissao;
                arrayMap.put("pago", pago);
                simpleArrayMap.put(data, arrayMap);
            }
            return simpleArrayMap;
        });
    }

}
