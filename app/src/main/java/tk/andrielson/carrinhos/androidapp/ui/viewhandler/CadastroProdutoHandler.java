package tk.andrielson.carrinhos.androidapp.ui.viewhandler;

import android.util.Log;
import android.view.View;

import tk.andrielson.carrinhos.androidapp.data.model.Produto;
import tk.andrielson.carrinhos.androidapp.ui.fragment.CadastroProdutoFragment;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;

/**
 * Created by anfesilva on 09/03/2018.
 */

public class CadastroProdutoHandler {
    private static final String TAG = CadastroProdutoHandler.class.getSimpleName();
    private final CadastroProdutoFragment.OnFragmentInteractionListener listener;

    public CadastroProdutoHandler(CadastroProdutoFragment.OnFragmentInteractionListener listener) {
        this.listener = listener;
    }

    public void onBotaoSalvarClick(View view, Produto produto) {
        if (produto != null)
            listener.onFragmentInteraction(produto, produto.getCodigo() == null);
        else
            LogUtil.Log(TAG, "O produto é nulo!", Log.ERROR);
    }
}
