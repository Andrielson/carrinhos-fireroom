package tk.andrielson.carrinhos.androidapp.ui.viewhandler;

import android.view.View;

import tk.andrielson.carrinhos.androidapp.data.model.Produto;
import tk.andrielson.carrinhos.androidapp.ui.fragment.ListaProdutoFragment;

/**
 * Created by Andrielson on 05/03/2018.
 */
public class ListaProdutoItemHandler {
    private final ListaProdutoFragment.OnListFragmentInteractionListener mListener;
    private final Produto produto;

    public ListaProdutoItemHandler(ListaProdutoFragment.OnListFragmentInteractionListener mListener, Produto produto) {
        this.mListener = mListener;
        this.produto = produto;
    }

    public void onClickProduto(View view) {
//        mListener.onClickProduto(produto);
    }
}
