package tk.andrielson.carrinhos.androidapp.ui.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tk.andrielson.carrinhos.androidapp.R;
import tk.andrielson.carrinhos.androidapp.data.model.Produto;
import tk.andrielson.carrinhos.androidapp.databinding.FragmentCadastroProdutoBinding;
import tk.andrielson.carrinhos.androidapp.observable.ProdutoObservable;
import tk.andrielson.carrinhos.androidapp.ui.viewhandler.CadastroProdutoHandler;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CadastroProdutoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CadastroProdutoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CadastroProdutoFragment extends Fragment {

    private static final String TAG = CadastroProdutoFragment.class.getSimpleName();
    private static final String ARG_PRODUTO = "produto";

    private ProdutoObservable produtoObservable;
    private FragmentCadastroProdutoBinding binding;

    private OnFragmentInteractionListener mListener;

    public CadastroProdutoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param produto o produto cujos dados serão exibidos.
     * @return A new instance of fragment CadastroProdutoFragment.
     */
    public static CadastroProdutoFragment newInstance(@NonNull Produto produto) {
        CadastroProdutoFragment fragment = new CadastroProdutoFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PRODUTO, produto);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if ((context instanceof OnFragmentInteractionListener)) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " deve implementar OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().getParcelable(ARG_PRODUTO) != null)
            //noinspection ConstantConditions
            produtoObservable = new ProdutoObservable(getArguments().getParcelable(ARG_PRODUTO));
        else
            produtoObservable = new ProdutoObservable();
    }

    @Override
    @NonNull
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cadastro_produto, container, false);
        CadastroProdutoHandler handler = new CadastroProdutoHandler(binding, mListener);
        binding.setProduto(produtoObservable);
        binding.setHandler(handler);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void salvarProduto(ProdutoObservable produto);

        void excluirProduto(ProdutoObservable produto);
    }
}
