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
import tk.andrielson.carrinhos.androidapp.data.model.Vendedor;
import tk.andrielson.carrinhos.androidapp.databinding.FragmentVendedorListaBinding;
import tk.andrielson.carrinhos.androidapp.observable.VendedorObservable;
import tk.andrielson.carrinhos.androidapp.ui.adapter.VendedorRecyclerViewAdapter;
import tk.andrielson.carrinhos.androidapp.viewmodel.ListaVendedorViewModel;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ListaVendedorFragment extends Fragment {

    private static final String TAG = ListaVendedorFragment.class.getSimpleName();
    private OnListFragmentInteractionListener mListener;
    private VendedorRecyclerViewAdapter adapter;
    private FragmentVendedorListaBinding binding;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListaVendedorFragment() {
    }

    public static ListaVendedorFragment newInstance() {
        ListaVendedorFragment fragment = new ListaVendedorFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vendedor_lista, container, false);
        adapter = new VendedorRecyclerViewAdapter(mListener);
        binding.list.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ListaVendedorViewModel viewModel = ViewModelProviders.of(this).get(ListaVendedorViewModel.class);
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

    private void configuraUI(ListaVendedorViewModel viewModel) {
        viewModel.getVendedores().observe(this, vendedores -> {
            if (vendedores != null) {
                binding.setIsLoading(false);
                adapter.setListaVendedor(vendedores);
            } else {
                binding.setIsLoading(true);
            }
            binding.executePendingBindings();
        });
    }

    public interface OnListFragmentInteractionListener {
        void onClickVendedor(VendedorObservable item);
    }
}
