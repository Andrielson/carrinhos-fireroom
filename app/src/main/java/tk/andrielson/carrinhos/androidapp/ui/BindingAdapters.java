package tk.andrielson.carrinhos.androidapp.ui;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;
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

    @BindingAdapter("valorReal")
    public static void setValorReal(TextView textView, Long valor) {
        if (textView instanceof CurrencyTextInputEditText)
            setValorRealCurrencyTextInputEditText((CurrencyTextInputEditText) textView, valor);
        else
            setValorRealTextView(textView, valor);
    }

    @BindingAdapter("android:checked")
    public static void setChecked(CompoundButton view, Boolean valor) {
        view.setChecked(valor);
    }

    private static void setValorRealTextView(TextView textView, Long valor) {
        textView.setText(String.format(Locale.getDefault(), "R$ %.2f", (double) valor / 100));
    }

    private static void setValorRealCurrencyTextInputEditText(CurrencyTextInputEditText editText, Long valor) {
        if (Objects.equals(valor, getValorReal(editText)))
            return;
        editText.setText(String.valueOf(valor));
    }

    @NonNull
    @InverseBindingAdapter(attribute = "valorReal")
    public static Long getValorReal(CurrencyTextInputEditText editText) {
        LogUtil.Log(TAG, "Dígitos decimais: " + editText.getDecimalDigits(), Log.DEBUG);
        return editText.getRawValue();
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