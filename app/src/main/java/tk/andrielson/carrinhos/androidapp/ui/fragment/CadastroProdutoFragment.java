package tk.andrielson.carrinhos.androidapp.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
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
import tk.andrielson.carrinhos.androidapp.ui.viewhandler.CadastroProdutoHandler;
import tk.andrielson.carrinhos.androidapp.viewmodel.CadastroProdutoViewModel;

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
    private static final String ARG_CODIGO = "codigo";

    private Long produtoCodigo;
    private FragmentCadastroProdutoBinding binding;

    private OnFragmentInteractionListener mListener;

    public CadastroProdutoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param codigo o código do produto existente.
     * @return A new instance of fragment CadastroProdutoFragment.
     */
    public static CadastroProdutoFragment newInstance(Long codigo) {
        CadastroProdutoFragment fragment = new CadastroProdutoFragment();
        if (codigo != null) {
            Bundle args = new Bundle();
            args.putLong(ARG_CODIGO, codigo);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        produtoCodigo = (getArguments() != null) ? getArguments().getLong(ARG_CODIGO) : null;
    }

    @Override
    @NonNull
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cadastro_produto, container, false);
        CadastroProdutoHandler handler = new CadastroProdutoHandler(binding, mListener);
        binding.setHandler(handler);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CadastroProdutoViewModel.Factory factory = new CadastroProdutoViewModel.Factory(this.produtoCodigo);
        //noinspection ConstantConditions
        CadastroProdutoViewModel viewModel = ViewModelProviders.of(getActivity(), factory).get(CadastroProdutoViewModel.class);
        configuraViewModel(viewModel);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof OnFragmentInteractionListener)) {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        mListener = (OnFragmentInteractionListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void configuraViewModel(CadastroProdutoViewModel viewModel) {
        viewModel.getProduto().observe(this, produto -> binding.setProduto(produto));
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void salvarProduto(Produto produto, boolean insercao);

        void excluirProduto(Produto produto);
    }
}
