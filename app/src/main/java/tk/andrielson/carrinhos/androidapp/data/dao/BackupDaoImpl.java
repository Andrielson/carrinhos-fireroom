package tk.andrielson.carrinhos.androidapp.data.dao;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.Executors;

import tk.andrielson.carrinhos.androidapp.fireroom.model.ItemVendaImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.model.ProdutoImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.model.VendaImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.model.VendedorImpl;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;

public final class BackupDaoImpl {

    private static final String TAG = BackupDaoImpl.class.getSimpleName();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void backupProdutos() {
        db.collection(ProdutoImpl.COLECAO).get().addOnCompleteListener(Executors.newSingleThreadExecutor(), task -> {
            if (task.isSuccessful()) {
                try {
                    JSONObject jsonProdutos = new JSONObject();
                    JSONArray jsonArray = new JSONArray();
                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put(ProdutoImpl.CODIGO, doc.getLong(ProdutoImpl.CODIGO));
                        jsonObject.put(ProdutoImpl.NOME, doc.getString(ProdutoImpl.NOME));
                        jsonObject.put(ProdutoImpl.SIGLA, doc.getString(ProdutoImpl.SIGLA));
                        jsonObject.put(ProdutoImpl.PRECO, doc.getLong(ProdutoImpl.PRECO));
                        jsonObject.put(ProdutoImpl.ATIVO, doc.getBoolean(ProdutoImpl.ATIVO));
                        jsonArray.put(jsonObject);
                    }
                    jsonProdutos.put("produtos", jsonArray);
                    String str = jsonProdutos.toString();
                    LogUtil.Log(TAG, str, Log.DEBUG);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void backupVendedores() {
        db.collection(VendedorImpl.COLECAO).get().addOnCompleteListener(Executors.newSingleThreadExecutor(), task -> {
            if (task.isSuccessful()) {
                try {
                    JSONObject jsonVendedores = new JSONObject();
                    JSONArray jsonArray = new JSONArray();
                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put(VendedorImpl.CODIGO, doc.getLong(VendedorImpl.CODIGO));
                        jsonObject.put(VendedorImpl.NOME, doc.getString(VendedorImpl.NOME));
                        jsonObject.put(VendedorImpl.COMISSAO, doc.getLong(VendedorImpl.COMISSAO));
                        jsonObject.put(VendedorImpl.ATIVO, doc.getBoolean(VendedorImpl.ATIVO));
                        jsonArray.put(jsonObject);
                    }
                    jsonVendedores.put("vendedores", jsonArray);
                    String str = jsonVendedores.toString();
                    LogUtil.Log(TAG, str, Log.DEBUG);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //FIXME: finalizar método
    public void backupVendas() {
        db.collection(VendaImpl.COLECAO).get().addOnCompleteListener(Executors.newSingleThreadExecutor(), new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                if (task.isSuccessful()) {
                    try {
                        JSONObject jsonVendas = new JSONObject();
                        JSONArray jsonArray = new JSONArray();
                        for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put(VendaImpl.CODIGO, doc.getLong(VendaImpl.CODIGO));
                            jsonObject.put(VendaImpl.DATA, dateFormat.format(doc.getDate(VendaImpl.DATA)));
                            jsonObject.put(VendaImpl.COMISSAO, doc.getLong(VendaImpl.COMISSAO));
                            jsonObject.put(VendaImpl.STATUS, doc.getString(VendaImpl.STATUS));
                            jsonObject.put(VendaImpl.VENDEDOR, Long.valueOf(doc.getDocumentReference(VendaImpl.VENDEDOR).getId()));
                            final JSONArray itensArray = new JSONArray();
                            db.collection(String.format("/%s/%s/%s", VendaImpl.COLECAO, String.format("%018d", doc.getLong(VendaImpl.CODIGO)), ItemVendaImpl.COLECAO)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task2) {
                                    if (task2.isSuccessful()) {
                                        try {
                                            for (DocumentSnapshot doc2 : task2.getResult().getDocuments()) {
                                                JSONObject jsonObject2 = new JSONObject();
                                                jsonObject2.put(ItemVendaImpl.PRODUTO, Long.valueOf(doc2.getDocumentReference(ItemVendaImpl.PRODUTO).getId()));
                                                jsonObject2.put(ItemVendaImpl.QT_SAIU, doc2.getLong(ItemVendaImpl.QT_SAIU));
                                                jsonObject2.put(ItemVendaImpl.QT_VOLTOU, doc2.getLong(ItemVendaImpl.QT_VOLTOU));
                                                jsonObject2.put(ItemVendaImpl.QT_VENDEU, doc2.getLong(ItemVendaImpl.QT_VENDEU));
                                                jsonObject2.put(ItemVendaImpl.VALOR, doc2.getLong(ItemVendaImpl.VALOR));
                                                itensArray.put(jsonObject2);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                            jsonArray.put(jsonObject);
                        }
                        jsonVendas.put("vendedores", jsonArray);
                        String str = jsonVendas.toString();
                        LogUtil.Log(TAG, str, Log.DEBUG);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
//{"produtos":[{"codigo":1,"nome":"PRODUTO 1","sigla":"PR1","preco":450,"ativo":true}],"vendedores":[{"codigo":1,"nome":"VENDEDOR 1","comissao":40,"ativo":true}],"vendas":[{"codigo":1,"comissao":40,"data":"2018-03-21","status":"FINALIZADA","vendedor":1,"itens":[{"produto":1,"qt_saiu":10,"qt_voltou":3,"qt_vendeu":7,"valor":450}]}]}