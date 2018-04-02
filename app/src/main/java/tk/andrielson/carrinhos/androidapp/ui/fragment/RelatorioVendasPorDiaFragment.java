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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.Objects;

import tk.andrielson.carrinhos.androidapp.R;
import tk.andrielson.carrinhos.androidapp.databinding.FragmentRelatorioVendasPorDiaBinding;
import tk.andrielson.carrinhos.androidapp.ui.adapter.RelatorioVendaPorDiaRecyclerViewAdapter;
import tk.andrielson.carrinhos.androidapp.viewmodel.RelatorioVendasPorDiaViewModel;

public class RelatorioVendasPorDiaFragment extends Fragment implements AdapterView.OnItemSelectedListener {

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
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()), R.array.frag_relat_vendas_spinner_agrupamento, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerAgrupamento.setAdapter(spinnerAdapter);
        binding.spinnerAgrupamento.setOnItemSelectedListener(this);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RelatorioVendasPorDiaViewModel viewModel = ViewModelProviders.of(this).get(RelatorioVendasPorDiaViewModel.class);
        configuraUI(viewModel);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_agrupamento:
                ArrayAdapter<CharSequence> periodoAdapter;
                switch (position) {
                    case 1:
                        periodoAdapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()), R.array.frag_relat_vendas_spinner_periodo_dia, android.R.layout.simple_spinner_item);
                        break;
                    case 2:
                        periodoAdapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()), R.array.frag_relat_vendas_spinner_periodo_mes, android.R.layout.simple_spinner_item);
                        break;
                    default:
                        binding.spinnerPeriodo.setVisibility(View.GONE);
                        return;
                }
                periodoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinnerPeriodo.setAdapter(periodoAdapter);
                binding.spinnerPeriodo.setVisibility(View.VISIBLE);
                binding.spinnerPeriodo.setOnItemSelectedListener(this);
                break;
            case R.id.spinner_periodo:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
