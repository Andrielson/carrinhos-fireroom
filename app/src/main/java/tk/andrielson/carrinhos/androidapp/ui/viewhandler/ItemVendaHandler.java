package tk.andrielson.carrinhos.androidapp.ui.viewhandler;

import android.text.Editable;

import tk.andrielson.carrinhos.androidapp.databinding.FragmentItemvendaBinding;
import tk.andrielson.carrinhos.androidapp.utils.Util;

public class ItemVendaHandler {
    private static final String TAG = ItemVendaHandler.class.getSimpleName();
    private final FragmentItemvendaBinding binding;

    public ItemVendaHandler(FragmentItemvendaBinding binding) {
        this.binding = binding;
    }

    public void calculaItem(Editable editable) {
        String strQtLevou = binding.qtLevou.getText().toString();
        String strQtVoltou = binding.qtVoltou.getText().toString();
        int qtLevou = strQtLevou.isEmpty() ? 0 : Integer.valueOf(strQtLevou);
        int qtVoltou = strQtVoltou.isEmpty() ? 0 : Integer.valueOf(strQtVoltou);
        if (qtVoltou > qtLevou) {
            binding.qtVoltou.setError("Valor inválido!");
            binding.qtVendeu.setText("0");
            binding.valorTotal.setText(Util.longToRS(0L));
            return;
        }
        int qtVendeu = qtLevou - qtVoltou;
        long valor = binding.getItemVenda().produto.get().getProdutoModel().getPreco();
        long total = qtVendeu * valor;
        binding.qtVendeu.setText(String.valueOf(qtVendeu));
        binding.valorTotal.setText(Util.longToRS(total));
    }
}
