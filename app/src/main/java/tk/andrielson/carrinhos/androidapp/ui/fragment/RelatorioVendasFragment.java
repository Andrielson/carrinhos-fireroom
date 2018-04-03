package tk.andrielson.carrinhos.androidapp.ui.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.List;
import java.util.Objects;

import tk.andrielson.carrinhos.androidapp.R;
import tk.andrielson.carrinhos.androidapp.databinding.FragmentRelatorioVendasPorDiaBinding;
import tk.andrielson.carrinhos.androidapp.observable.RelatorioVendaPorDia;
import tk.andrielson.carrinhos.androidapp.ui.adapter.RelatorioVendaDiariaRecyclerViewAdapter;
import tk.andrielson.carrinhos.androidapp.ui.adapter.RelatorioVendaMensalRecyclerViewAdapter;
import tk.andrielson.carrinhos.androidapp.viewmodel.RelatorioVendasViewModel;

public class RelatorioVendasFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private RecyclerView.Adapter adapter;
    private FragmentRelatorioVendasPorDiaBinding binding;
    private RelatorioVendasViewModel viewModel;

    public RelatorioVendasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_relatorio_vendas_por_dia, container, false);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(RelatorioVendasViewModel.class);
        configuraSpinners();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_agrupamento:
                ArrayAdapter<CharSequence> periodoAdapter;
                switch (position) {
                    case 1:
                        adapter = new RelatorioVendaMensalRecyclerViewAdapter();
                        periodoAdapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()), R.array.frag_relat_vendas_spinner_periodo_mes, R.layout.layout_spinner_item);
                        break;
                    default:
                        adapter = new RelatorioVendaDiariaRecyclerViewAdapter();
                        periodoAdapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()), R.array.frag_relat_vendas_spinner_periodo_dia, R.layout.layout_spinner_item);
                }
                binding.list.setAdapter(adapter);
                periodoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinnerPeriodo.setAdapter(periodoAdapter);
                binding.spinnerPeriodo.setOnItemSelectedListener(this);
                break;
            case R.id.spinner_periodo:
                switch (binding.spinnerAgrupamento.getSelectedItemPosition()) {
                    case 0:
                        switch (binding.spinnerPeriodo.getSelectedItemPosition()) {
                            case 0:
                                exibeRelatorioDiario(RelatorioVendasViewModel.RelatorioDiario.ESTA_SEMANA);
                                break;
                            case 1:
                                exibeRelatorioDiario(RelatorioVendasViewModel.RelatorioDiario.QUINZE_DIAS);
                                break;
                            case 2:
                                exibeRelatorioDiario(RelatorioVendasViewModel.RelatorioDiario.ESTE_MES);
                                break;
                        }
                        break;
                    case 1:
                        switch (binding.spinnerPeriodo.getSelectedItemPosition()) {
                            case 0:
                                exibeRelatorioMensal(RelatorioVendasViewModel.RelatorioMensal.SEIS_MESES);
                                break;
                            case 1:
                                exibeRelatorioMensal(RelatorioVendasViewModel.RelatorioMensal.ESTE_ANO);
                                break;
                        }
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void configuraSpinners() {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()), R.array.frag_relat_vendas_spinner_agrupamento, R.layout.layout_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerAgrupamento.setAdapter(spinnerAdapter);
        binding.spinnerAgrupamento.setOnItemSelectedListener(this);
        onItemSelected(binding.spinnerAgrupamento, null, 1, 0L);
    }

    private void exibeRelatorioDiario(RelatorioVendasViewModel.RelatorioDiario relatorio) {
        viewModel.getRelatorioDiario(relatorio).observe(this, new RelatorioDiarioObserver());
    }

    private void exibeRelatorioMensal(RelatorioVendasViewModel.RelatorioMensal relatorio) {
        viewModel.getRelatorioMensal(relatorio).observe(this, new RelatorioMensalObserver());
    }

    private class RelatorioDiarioObserver implements Observer<List<RelatorioVendaPorDia>> {

        private final RelatorioVendaDiariaRecyclerViewAdapter adapter = new RelatorioVendaDiariaRecyclerViewAdapter();

        @Override
        public void onChanged(@Nullable List<RelatorioVendaPorDia> relatorio) {
            if (relatorio != null) {
                binding.setCarregando(false);
                adapter.setLista(relatorio);
                binding.setListaVazia(relatorio.isEmpty());
            } else {
                binding.setCarregando(true);
            }
            binding.executePendingBindings();
        }
    }

    private class RelatorioMensalObserver implements Observer<List<RelatorioVendaPorDia>> {

        private final RelatorioVendaMensalRecyclerViewAdapter adapter = new RelatorioVendaMensalRecyclerViewAdapter();

        @Override
        public void onChanged(@Nullable List<RelatorioVendaPorDia> relatorio) {
            if (relatorio != null) {
                binding.setCarregando(false);
                adapter.setLista(relatorio);
                binding.setListaVazia(relatorio.isEmpty());
            } else {
                binding.setCarregando(true);
            }
            binding.executePendingBindings();
        }
    }
}
