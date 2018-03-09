package tk.andrielson.carrinhos.androidapp.ui;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Objects;

import tk.andrielson.carrinhos.androidapp.textinputmoeda.CurrencyTextInputEditText;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;

/**
 * Created by anfesilva on 09/03/2018.
 */

public class BindingAdapters {
    private static final String TAG = BindingAdapters.class.getSimpleName();

    @BindingAdapter(value = "valorRealAttrChanged")
    public static void setListenerOnEditTextMoeda(EditText editText, final InverseBindingListener listener) {
        if (listener != null) {
            editText.addTextChangedListener(new InverseBindingTextChangedListener(listener));
        }
    }

    @BindingAdapter(value = "valorReal")
    public static void setValorRealTextView(TextView textView, double valor) {
        if (textView instanceof CurrencyTextInputEditText)
            setValorRealCurrencyTextInputEditText((CurrencyTextInputEditText) textView, valor);
        else
            textView.setText(String.valueOf(valor * 10));
    }

    @BindingAdapter(value = "valorReal")
    public static void setValorRealCurrencyTextInputEditText(CurrencyTextInputEditText editText, double valor) {
        if (Objects.equals(valor, getValorReal(editText)))
            return;
        editText.setText(String.valueOf(valor * 10));
    }

    @NonNull
    @InverseBindingAdapter(attribute = "valorReal")
    public static Double getValorReal(CurrencyTextInputEditText editText) {
        LogUtil.Log(TAG, "Dígitos decimais: " + editText.getDecimalDigits(), Log.DEBUG);
        return ((double) editText.getRawValue()) / 100;
    }

    private static class InverseBindingTextChangedListener implements TextWatcher {
        private final InverseBindingListener bindingListener;

        InverseBindingTextChangedListener(InverseBindingListener bindingListener) {
            this.bindingListener = bindingListener;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            bindingListener.onChange();
        }
    }
}