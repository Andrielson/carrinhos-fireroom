package tk.andrielson.carrinhos.androidapp.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tk.andrielson.carrinhos.androidapp.R;
import tk.andrielson.carrinhos.androidapp.databinding.FragmentInicioBinding;
import tk.andrielson.carrinhos.androidapp.observable.InicioTotais;
import tk.andrielson.carrinhos.androidapp.viewmodel.InicioViewModel;

public class InicioFragment extends Fragment {

    private static final String TAG = InicioFragment.class.getSimpleName();
    private FragmentInicioBinding binding;

    public InicioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_inicio, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InicioViewModel viewModel = ViewModelProviders.of(this).get(InicioViewModel.class);
        configuraTela(viewModel);
    }

    private void configuraTela(@NonNull InicioViewModel viewModel) {
        viewModel.getTotalHoje().observe(this, totais -> binding.setTotalHoje(totais));
        viewModel.getTotalSemana().observe(this, totais -> binding.setTotalSemana(totais));
        viewModel.getTotalMes().observe(this, totais -> binding.setTotalMes(totais));
        viewModel.getTopVendedorSemana().observe(this, arrayMap -> {
            if (arrayMap == null || arrayMap.isEmpty()) return;
            if (arrayMap.containsKey("vendedor"))
                binding.setTopVendedorSemana(arrayMap.get("vendedor"));
            if (arrayMap.containsKey("vendido") && arrayMap.containsKey("pago") && arrayMap.containsKey("comissao")) {
                InicioTotais totais = new InicioTotais(arrayMap.get("vendido"), arrayMap.get("pago"), arrayMap.get("comissao"));
                binding.setTotalTopVendedorSemana(totais);
            }
        });
        viewModel.getTopVendedorMes().observe(this, arrayMap -> {
            if (arrayMap == null || arrayMap.isEmpty()) return;
            if (arrayMap.containsKey("vendedor"))
                binding.setTopVendedorMes(arrayMap.get("vendedor"));
            if (arrayMap.containsKey("vendido") && arrayMap.containsKey("pago") && arrayMap.containsKey("comissao")) {
                InicioTotais totais = new InicioTotais(arrayMap.get("vendido"), arrayMap.get("pago"), arrayMap.get("comissao"));
                binding.setTotalTopVendedorMes(totais);
            }
        });
    }
}
