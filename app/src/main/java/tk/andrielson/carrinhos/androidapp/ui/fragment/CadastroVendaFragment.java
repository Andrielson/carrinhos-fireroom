package tk.andrielson.carrinhos.androidapp.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import tk.andrielson.carrinhos.androidapp.R;
import tk.andrielson.carrinhos.androidapp.data.model.Venda;
import tk.andrielson.carrinhos.androidapp.databinding.FragmentCadastroVendaBinding;
import tk.andrielson.carrinhos.androidapp.databinding.FragmentItemvendaBinding;
import tk.andrielson.carrinhos.androidapp.observable.ItemVendaObservable;
import tk.andrielson.carrinhos.androidapp.observable.VendaObservable;
import tk.andrielson.carrinhos.androidapp.observable.VendedorObservable;
import tk.andrielson.carrinhos.androidapp.ui.viewhandler.CadastroVendaHandler;
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
    private static final String ARG_VENDA = "venda";

    private OnFragmentInteractionListener mListener;
    private FragmentCadastroVendaBinding binding;
    private VendaObservable vendaObservable;

    public CadastroVendaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param venda a venda a ser cadastrada ou exibida no fragment
     * @return A new instance of fragment CadastroVendaFragment.
     */
    public static CadastroVendaFragment newInstance(@NonNull Venda venda) {
        CadastroVendaFragment fragment = new CadastroVendaFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_VENDA, venda);
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
        if (getArguments() != null && getArguments().getParcelable(ARG_VENDA) != null)
            //noinspection ConstantConditions
            vendaObservable = new VendaObservable(getArguments().getParcelable(ARG_VENDA));
        else
            vendaObservable = new VendaObservable();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cadastro_venda, container, false);
        configuraCamposData();
        binding.setVenda(vendaObservable);
        binding.setHandler(new CadastroVendaHandler(binding, mListener));
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

    public void setVendedorObservable(@NonNull VendedorObservable vendedorObservable) {
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
                    if (vendaObservable.itens.get().indexOf(ito) == 0) {
                        setProximoCampo(itemvendaBinding.qtLevou);
                    }
                }
                binding.setVenda(vendaObservable);
                binding.setCadastroVendaListener(mListener);
                binding.executePendingBindings();
            }
        });
    }

    private void configuraCamposData() {
        binding.dataDia.setTransformationMethod(null);
        //setProximoCampo(binding.dataDia, binding.dataMes, 2, 1, 31);
        binding.dataMes.setTransformationMethod(null);
        //setProximoCampo(binding.dataMes, binding.dataAno, 2, 1, 12);
        binding.dataAno.setTransformationMethod(null);
    }

    private void setProximoCampo(final EditText editText) {
        binding.dataAno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 4) {
                    if (Integer.valueOf(s.toString()) < 2000 || Integer.valueOf(s.toString()) > 2018) {
                        binding.dataAno.setError("Ano inválido!");
                        return;
                    }
                    editText.setFocusable(true);
                    editText.setFocusableInTouchMode(true);
                    editText.requestFocus();
                }
            }
        });
    }

    public interface OnFragmentInteractionListener {

        void onClickSelecionarVendedor();

        void finalizarVenda(VendaObservable observable);

        void salvarVenda(VendaObservable observable);

        void excluirVenda(VendaObservable observable);
    }
}
