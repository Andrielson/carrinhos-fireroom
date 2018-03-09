package tk.andrielson.carrinhos.androidapp.ui;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

import tk.andrielson.carrinhos.androidapp.textinputmoeda.CurrencyTextInputEditText;

/**
 * Created by anfesilva on 09/03/2018.
 */

public class BindingAdapters {
    @BindingAdapter(value = "valorRealAttrChanged")
    public static void setListenerOnEditTextMoeda(EditText editText, final InverseBindingListener listener) {
        if (listener != null) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    listener.onChange();
                }
            });
        }
    }

    @BindingAdapter("valorReal")
    public static void setValorReal(TextView editText, Double valor) {
        if ((editText instanceof CurrencyTextInputEditText) && Objects.equals(valor, getValorReal((CurrencyTextInputEditText) editText)))
            return;
//        editText.setText(valor != null ? String.format(Locale.getDefault(), "%.2f", valor) : "");
        editText.setText(String.valueOf(valor * 10));
    }

    @InverseBindingAdapter(attribute = "valorReal")
    public static Double getValorReal(CurrencyTextInputEditText editText) {
        /*String texto = editText.getText().toString().replace(",", ".");
        return texto.isEmpty() ? null : Double.valueOf(texto);*/
        return (double) (editText.getRawValue() / 100);
    }
}
