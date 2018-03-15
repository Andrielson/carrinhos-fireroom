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
import tk.andrielson.carrinhos.androidapp.data.model.ItemVenda;
import tk.andrielson.carrinhos.androidapp.databinding.FragmentCadastroVendaBinding;
import tk.andrielson.carrinhos.androidapp.databinding.FragmentItemvendaBinding;
import tk.andrielson.carrinhos.androidapp.observable.ItemVendaObservable;
import tk.andrielson.carrinhos.androidapp.observable.VendaObservable;
import tk.andrielson.carrinhos.androidapp.observable.VendedorObservable;
import tk.andrielson.carrinhos.androidapp.ui.viewhandler.ItemVendaHandler;
import tk.andrielson.carrinhos.androidapp.viewmodel.CadastroVendaViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CadastroVendaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CadastroVendaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CadastroVendaFragment extends Fragment {

    private static final String TAG = CadastroVendaFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private FragmentCadastroVendaBinding binding;
    private ItemVenda[] itemVendas;

    public CadastroVendaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CadastroVendaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CadastroVendaFragment newInstance() {
        CadastroVendaFragment fragment = new CadastroVendaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " deve implementar OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cadastro_venda, container, false);
        binding.dataDia.setTransformationMethod(null);
        binding.dataMes.setTransformationMethod(null);
        binding.dataAno.setTransformationMethod(null);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final CadastroVendaViewModel viewModel = ViewModelProviders.of(this).get(CadastroVendaViewModel.class);
        configuraUI(viewModel);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setVendedorObservable(VendedorObservable vendedorObservable) {
        binding.getVenda().vendedor.set(vendedorObservable);
        binding.getVenda().comissao.set(vendedorObservable.comissao.get());
        binding.botaoSelecionarVendedor.setText(vendedorObservable.nome.get());
        binding.executePendingBindings();
    }

    //TODO: encontrar uma forma de parar a observação da LiveData
    private void configuraUI(final CadastroVendaViewModel viewModel) {
        viewModel.getItensVenda().observe(this, itens -> {
            if (itens != null) {
                LayoutInflater layoutInflater = LayoutInflater.from(binding.layoutDosItens.getContext());
                binding.layoutDosItens.removeAllViews();
                VendaObservable vendaObservable = new VendaObservable();
                vendaObservable.setItensVendaObservable(itens);
                for (ItemVendaObservable ito : vendaObservable.itens.get()) {
                    FragmentItemvendaBinding itemvendaBinding = FragmentItemvendaBinding.inflate(layoutInflater, binding.layoutDosItens, false);
                    itemvendaBinding.setItemVenda(ito);
                    itemvendaBinding.setHandler(new ItemVendaHandler(itemvendaBinding));
                    itemvendaBinding.qtLevou.setTransformationMethod(null);
                    itemvendaBinding.qtVoltou.setTransformationMethod(null);
                    binding.layoutDosItens.addView(itemvendaBinding.getRoot());
                }
                binding.setVenda(vendaObservable);
                binding.setCadastroVendaListener(mListener);
                binding.executePendingBindings();
            }
        });
    }

    public interface OnFragmentInteractionListener {

        void onClickSelecionarVendedor();
    }
}
