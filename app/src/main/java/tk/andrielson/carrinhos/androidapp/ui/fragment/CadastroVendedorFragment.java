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
import tk.andrielson.carrinhos.androidapp.observable.VendedorObservable;
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
    private static final String ARG_VENDEDOR = "vendedor";

    private VendedorObservable vendedorObservable;
    private FragmentCadastroVendedorBinding binding;

    private OnFragmentInteractionListener mListener;

    public CadastroVendedorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param vendedor o vendedor cujos dados serão exibidos.
     * @return A new instance of fragment CadastroVendedorFragment.
     */
    public static CadastroVendedorFragment newInstance(@NonNull Vendedor vendedor) {
        CadastroVendedorFragment fragment = new CadastroVendedorFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_VENDEDOR, vendedor);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof OnFragmentInteractionListener)) {
            throw new RuntimeException(context.toString()
                    + " deve implementar OnFragmentInteractionListener");
        }
        mListener = (OnFragmentInteractionListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().getParcelable(ARG_VENDEDOR) != null)
            //noinspection ConstantConditions
            vendedorObservable = new VendedorObservable(getArguments().getParcelable(ARG_VENDEDOR));
        else
            vendedorObservable = new VendedorObservable();
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void salvarVendedor(VendedorObservable vendedor);

        void excluirVendedor(VendedorObservable vendedor);
    }
}
