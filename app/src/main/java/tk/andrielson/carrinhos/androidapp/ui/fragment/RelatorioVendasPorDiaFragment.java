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
import tk.andrielson.carrinhos.androidapp.databinding.FragmentRelatorioVendasPorDiaBinding;
import tk.andrielson.carrinhos.androidapp.ui.adapter.RelatorioVendaPorDiaRecyclerViewAdapter;
import tk.andrielson.carrinhos.androidapp.viewmodel.RelatorioVendasPorDiaViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class RelatorioVendasPorDiaFragment extends Fragment {

    private RelatorioVendaPorDiaRecyclerViewAdapter adapter;
    private FragmentRelatorioVendasPorDiaBinding binding;

    public RelatorioVendasPorDiaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_relatorio_vendas_por_dia, container, false);
        adapter = new RelatorioVendaPorDiaRecyclerViewAdapter();
        binding.list.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RelatorioVendasPorDiaViewModel viewModel = ViewModelProviders.of(this).get(RelatorioVendasPorDiaViewModel.class);
        configuraUI(viewModel);
    }

    private void configuraUI(RelatorioVendasPorDiaViewModel viewModel) {
        viewModel.getRelatorio().observe(this, relatorio -> {
            if (relatorio != null) {
                binding.setCarregando(false);
                adapter.setLista(relatorio);
                binding.setListaVazia(relatorio.isEmpty());
            } else {
                binding.setCarregando(true);
            }
            binding.executePendingBindings();
        });
    }

}
