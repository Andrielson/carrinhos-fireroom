package tk.andrielson.carrinhos.androidapp.fireroom.firestore.dao;

import tk.andrielson.carrinhos.androidapp.fireroom.firestore.collections.VendaFirestore;

public final class VendaDaoImpl extends FirestoreDao {
    private static final String COLECAO = VendaFirestore.COLECAO;
    private static final String TAG = VendaDaoImpl.class.getSimpleName();

    public VendaDaoImpl() {
        super(COLECAO);
    }
}
