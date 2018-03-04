package tk.andrielson.carrinhos.androidapp.viewholder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import tk.andrielson.carrinhos.androidapp.R;
import tk.andrielson.carrinhos.androidapp.data.model.Produto;

/**
 * Created by Andrielson on 04/03/2018.
 */

public class ProdutoViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = ProdutoViewHolder.class.getSimpleName();

    public final View mView;
    public final TextView mNomeView;
    public final TextView mValorView;
    public Produto mItem;

    public ProdutoViewHolder(View itemView) {
        super(itemView);
        mView = itemView.findViewById(R.id.fragment_produto_item);
        mNomeView = itemView.findViewById(R.id.fragment_produto_nome);
        mValorView = itemView.findViewById(R.id.fragment_produto_valor);
        Log.d(TAG, "ProdutoViewHolder");
    }
}
