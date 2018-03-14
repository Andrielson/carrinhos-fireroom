package tk.andrielson.carrinhos.androidapp.ui.viewhandler;

import android.view.View;

import tk.andrielson.carrinhos.androidapp.data.model.Vendedor;
import tk.andrielson.carrinhos.androidapp.ui.fragment.ListaVendedorFragment;

/**
 * Created by Andrielson on 11/03/2018.
 */

public class ListaVendedorItemHandler {
    private final ListaVendedorFragment.OnListFragmentInteractionListener mListener;
    private final Vendedor vendedor;

    public ListaVendedorItemHandler(ListaVendedorFragment.OnListFragmentInteractionListener mListener, Vendedor vendedor) {
        this.mListener = mListener;
        this.vendedor = vendedor;
    }

    public void onClickVendedor(View view) {
//        mListener.onClickVendedor(vendedor);
    }
}
