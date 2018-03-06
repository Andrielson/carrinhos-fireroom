package tk.andrielson.carrinhos.androidapp.ui.viewhandler;

import android.view.View;

import tk.andrielson.carrinhos.androidapp.data.model.Produto;
import tk.andrielson.carrinhos.androidapp.ui.fragment.ProdutoFragment;

/**
 * Created by Andrielson on 05/03/2018.
 */
public class ProdutoHandler {
    private final ProdutoFragment.OnListFragmentInteractionListener mListener;
    private final Produto produto;

    public ProdutoHandler(ProdutoFragment.OnListFragmentInteractionListener mListener, Produto produto) {
        this.mListener = mListener;
        this.produto = produto;
    }

    public void onClickProduto(View view) {
        mListener.onListFragmentInteraction(produto);
    }
}
