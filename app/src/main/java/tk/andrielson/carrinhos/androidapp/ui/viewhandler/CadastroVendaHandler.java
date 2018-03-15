package tk.andrielson.carrinhos.androidapp.ui.viewhandler;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import tk.andrielson.carrinhos.androidapp.databinding.FragmentCadastroVendaBinding;
import tk.andrielson.carrinhos.androidapp.observable.VendaObservable;
import tk.andrielson.carrinhos.androidapp.ui.fragment.CadastroVendaFragment;

public final class CadastroVendaHandler {

    private static final String TAG = CadastroVendaHandler.class.getSimpleName();
    private final FragmentCadastroVendaBinding binding;
    private final CadastroVendaFragment.OnFragmentInteractionListener listener;

    private final int[] meses30 = {4, 6, 9, 11};
    private final int[] meses31 = {1, 3, 5, 7, 8, 10, 12};

    public CadastroVendaHandler(FragmentCadastroVendaBinding binding, CadastroVendaFragment.OnFragmentInteractionListener listener) {
        this.binding = binding;
        this.listener = listener;
    }

    public void preencheDataHoje() {
        DateFormat diaFormat = new SimpleDateFormat("dd", Locale.getDefault());
        DateFormat mesFormat = new SimpleDateFormat("MM", Locale.getDefault());
        DateFormat anoFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        binding.dataDia.setText(diaFormat.format(Calendar.getInstance().getTime()));
        binding.dataMes.setText(mesFormat.format(Calendar.getInstance().getTime()));
        binding.dataAno.setText(anoFormat.format(Calendar.getInstance().getTime()));
    }

    public void afterTextChangedDataDia(Editable s) {
        if (s.toString().length() == 2) {
            int dia = Integer.valueOf(s.toString());
            if (dia < 1 || dia > 31) {
                binding.dataDia.setError("Dia inválido!");
                return;
            }
            if (binding.dataMes.getText().toString().length() == 2) {
                int mes = Integer.valueOf(binding.dataMes.getText().toString());
                //Fevereiro ou mês de 30 dias
                if ((mes == 2 && dia > 29) || (Arrays.binarySearch(meses30, mes) >= 0 && dia > 30)) {
                    binding.dataDia.setError("Dia ou mês inválido!");
                    return;
                }
            }
            setNextFocus(binding.dataMes);
        }
    }

    public void afterTextChangedDataMes(Editable s) {
        if (s.toString().length() == 2) {
            int mes = Integer.valueOf(s.toString());
            if (mes < 1 || mes > 12) {
                binding.dataMes.setError("Mês inválido!");
                return;
            }
            if (binding.dataDia.getText().toString().length() == 2) {
                int dia = Integer.valueOf(binding.dataDia.getText().toString());
                //Fevereiro ou mês de 30 dias
                if ((mes == 2 && dia > 29) || (Arrays.binarySearch(meses30, mes) >= 0 && dia > 30)) {
                    binding.dataMes.setError("Dia ou mês inválido!");
                    return;
                }
            }
            setNextFocus(binding.dataAno);
        }
    }

    public void onBotaoExcluirClick(final VendaObservable observable) {
        Snackbar confirmacao = Snackbar.make(binding.coordinatorLayout, "Tem certeza de que quer excluir essa venda?", Snackbar.LENGTH_LONG);
        confirmacao.setAction("SIM", v -> listener.excluirVenda(observable));
        confirmacao.show();
    }

    public void onBotaoSalvarClick(VendaObservable observable) {
        if (true)
            listener.salvarVenda(observable);
        else
            Toast.makeText(binding.getRoot().getContext(), "Por favor, corrija as informações incorretas!", Toast.LENGTH_SHORT).show();
    }

    public void onBotaoFinalizarClick(VendaObservable observable) {
        if (true)
            listener.finalizarVenda(observable);
        else
            Toast.makeText(binding.getRoot().getContext(), "Por favor, corrija as informações incorretas!", Toast.LENGTH_SHORT).show();
    }

    //TODO: generalizar validação da data no método abaixo
    private boolean ehDataValida() {
        String dia = binding.dataDia.getText().toString();
        String mes = binding.dataMes.getText().toString();
        String ano = binding.dataAno.getText().toString();
        String data = String.format("%s/%s/%s", dia, mes, ano);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            dateFormat.parse(data);
        } catch (ParseException e) {
            //data inválida
            return false;
        }
        return true;
    }

    private void setNextFocus(@NonNull final EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }
}
