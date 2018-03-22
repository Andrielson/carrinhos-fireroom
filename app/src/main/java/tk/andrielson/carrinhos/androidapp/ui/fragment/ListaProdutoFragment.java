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
import tk.andrielson.carrinhos.androidapp.databinding.FragmentProdutoListaBinding;
import tk.andrielson.carrinhos.androidapp.observable.ProdutoObservable;
import tk.andrielson.carrinhos.androidapp.ui.adapter.ProdutoRecyclerViewAdapter;
import tk.andrielson.carrinhos.androidapp.viewmodel.ListaProdutoViewModel;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ListaProdutoFragment extends Fragment {

    private static final String TAG = ListaProdutoFragment.class.getSimpleName();
    private OnListFragmentInteractionListener mListener;
    private ProdutoRecyclerViewAdapter adapter;
    private FragmentProdutoListaBinding binding;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListaProdutoFragment() {
    }

    public static ListaProdutoFragment newInstance() {
        ListaProdutoFragment fragment = new ListaProdutoFragment();
        Bundle args = new Bundle();
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_produto_lista, container, false);
        adapter = new ProdutoRecyclerViewAdapter(mListener);
        binding.list.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ListaProdutoViewModel viewModel = ViewModelProviders.of(this).get(ListaProdutoViewModel.class);
        configuraUI(viewModel);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void configuraUI(ListaProdutoViewModel viewModel) {
        viewModel.getProdutos().observe(this, produtos -> {
            if (produtos != null) {
                binding.setCarregando(false);
                adapter.setListaProduto(produtos);
                binding.setListaVazia(produtos.isEmpty());
            } else
                binding.setCarregando(true);
            binding.executePendingBindings();
        });
    }

    public interface OnListFragmentInteractionListener {
        void onClickProduto(ProdutoObservable item);
    }
}
