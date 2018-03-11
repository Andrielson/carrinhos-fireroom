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
import tk.andrielson.carrinhos.androidapp.databinding.FragmentCadastroVendedorBinding;
import tk.andrielson.carrinhos.androidapp.ui.viewhandler.CadastroVendedorHandler;
import tk.andrielson.carrinhos.androidapp.viewmodel.CadastroVendedorViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CadastroVendedorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CadastroVendedorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CadastroVendedorFragment extends Fragment {

    private static final String TAG = CadastroVendedorFragment.class.getSimpleName();
    private static final String ARG_CODIGO = "codigo";

    private Long vendedorCodigo;
    private FragmentCadastroVendedorBinding binding;

    private OnFragmentInteractionListener mListener;

    public CadastroVendedorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param codigo o código do vendedor existente.
     * @return A new instance of fragment CadastroVendedorFragment.
     */
    public static CadastroVendedorFragment newInstance(Long codigo) {
        CadastroVendedorFragment fragment = new CadastroVendedorFragment();
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
        vendedorCodigo = (getArguments() != null) ? getArguments().getLong(ARG_CODIGO) : null;
    }

    @Override
    @NonNull
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cadastro_vendedor, container, false);
        CadastroVendedorHandler handler = new CadastroVendedorHandler(binding, mListener);
        binding.setHandler(handler);
        binding.inputComissao.setTransformationMethod(null);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CadastroVendedorViewModel.Factory factory = new CadastroVendedorViewModel.Factory(this.vendedorCodigo);
        //noinspection ConstantConditions
        CadastroVendedorViewModel viewModel = ViewModelProviders.of(getActivity(), factory).get(CadastroVendedorViewModel.class);
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

    private void configuraViewModel(CadastroVendedorViewModel viewModel) {
        viewModel.getVendedor().observe(this, vendedor -> binding.setVendedor(vendedor));
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
        void salvarVendedor(Vendedor vendedor, boolean insercao);

        void excluirVendedor(Vendedor vendedor);
    }
}
