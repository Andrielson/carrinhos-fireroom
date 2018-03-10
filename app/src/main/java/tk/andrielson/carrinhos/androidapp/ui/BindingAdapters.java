package tk.andrielson.carrinhos.androidapp.ui;

import android.content.res.ColorStateList;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;
import java.util.Objects;

import tk.andrielson.carrinhos.androidapp.CarrinhosApp;
import tk.andrielson.carrinhos.androidapp.R;
import tk.andrielson.carrinhos.androidapp.textinputmoeda.CurrencyTextInputEditText;

/**
 * Created by anfesilva on 09/03/2018.
 */

public class BindingAdapters {
    private static final String TAG = BindingAdapters.class.getSimpleName();

    @BindingAdapter("android:checked")
    public static void setChecked(CompoundButton view, Boolean valor) {
        view.setChecked(valor != null ? valor : false);
    }

    @BindingAdapter("produtoHabilitado")
    public static void setProdutoListaItemImagemHabilitado(ImageView imageView, Boolean habilitado) {
        ColorStateList csl;
        if (habilitado != null && habilitado) {
            imageView.setImageResource(R.drawable.ic_shopping_cart_black_32dp);
            imageView.setContentDescription("Produto habilitado");
            csl = AppCompatResources.getColorStateList(CarrinhosApp.getContext(), R.color.produtoHabilitado);
        } else {
            imageView.setImageResource(R.drawable.ic_remove_shopping_cart_black_32dp);
            imageView.setContentDescription("Produto desabilitado");
            csl = AppCompatResources.getColorStateList(CarrinhosApp.getContext(), R.color.produtoDesabilitado);
        }
        DrawableCompat.setTintList(imageView.getDrawable(), csl);
    }

    @BindingAdapter(value = "valorRealAttrChanged")
    public static void setListenerOnEditTextMoeda(EditText editText, final InverseBindingListener listener) {
        if (listener != null) {
            editText.addTextChangedListener(new InverseBindingTextChangedListener(listener));
        }
    }

    @NonNull
    @InverseBindingAdapter(attribute = "valorReal")
    public static Long getValorReal(CurrencyTextInputEditText editText) {
        return editText.getRawValue();
    }

    @BindingAdapter("valorReal")
    public static void setValorReal(TextView textView, Long valor) {
        if (textView instanceof CurrencyTextInputEditText)
            setValorRealCurrencyTextInputEditText((CurrencyTextInputEditText) textView, valor);
        else
            setValorRealTextView(textView, valor);
    }

    private static void setValorRealTextView(TextView textView, Long valor) {
        textView.setText(String.format(Locale.getDefault(), "R$ %.2f", (double) valor / 100));
    }

    private static void setValorRealCurrencyTextInputEditText(CurrencyTextInputEditText editText, Long valor) {
        if (Objects.equals(valor, getValorReal(editText)))
            return;
        editText.setText(String.valueOf(valor));
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