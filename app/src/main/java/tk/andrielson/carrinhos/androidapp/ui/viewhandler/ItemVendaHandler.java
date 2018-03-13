package tk.andrielson.carrinhos.androidapp.ui.viewhandler;

import android.text.Editable;

import java.util.Locale;

import tk.andrielson.carrinhos.androidapp.databinding.FragmentItemvendaBinding;

/**
 * Created by anfesilva on 13/03/2018.
 */

public class ItemVendaHandler {
    private static final String TAG = ItemVendaHandler.class.getSimpleName();
    private final FragmentItemvendaBinding binding;
    private final ItemVendaListener listener;

    public ItemVendaHandler(FragmentItemvendaBinding binding, ItemVendaListener listener) {
        this.binding = binding;
        this.listener = listener;
    }

    public void calculaItem(Editable editable) {
        String strQtLevou = binding.qtLevou.getText().toString();
        String strQtVoltou = binding.qtVoltou.getText().toString();
        int qtLevou = strQtLevou.isEmpty() ? 0 : Integer.valueOf(strQtLevou);
        int qtVoltou = strQtVoltou.isEmpty() ? 0 : Integer.valueOf(strQtVoltou);
        int qtVendeu = qtLevou - qtVoltou;
        long valor = binding.getItemVenda().getProduto().getPreco();
        long total = qtVendeu * valor;
        binding.qtVendeu.setText(String.valueOf(qtVendeu));
        binding.valorTotal.setText(String.format(Locale.getDefault(), "R$ %.2f", (double) total / 100));
        listener.atualizaTotalVenda(total);
    }

    public interface ItemVendaListener {
        void atualizaTotalVenda(Long total);
    }
}
