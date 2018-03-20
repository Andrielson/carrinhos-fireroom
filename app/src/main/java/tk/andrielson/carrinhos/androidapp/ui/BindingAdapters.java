package tk.andrielson.carrinhos.androidapp.ui;

import android.content.res.ColorStateList;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;
import java.util.Objects;

import tk.andrielson.carrinhos.androidapp.CarrinhosApp;
import tk.andrielson.carrinhos.androidapp.R;
import tk.andrielson.carrinhos.androidapp.data.model.Venda;
import tk.andrielson.carrinhos.androidapp.textinputmoeda.CurrencyTextInputEditText;
import tk.andrielson.carrinhos.androidapp.utils.LogUtil;

/**
 * Created by anfesilva on 09/03/2018.
 */

public final class BindingAdapters {
    private static final String TAG = BindingAdapters.class.getSimpleName();

    @BindingAdapter("android:checked")
    public static void setChecked(CompoundButton view, Boolean valor) {
        view.setChecked(valor != null ? valor : false);
    }

    @BindingAdapter("carrinhoHabilitado")
    public static void setCarrinhoImagemHabilitado(ImageView imageView, Boolean habilitado) {
        ColorStateList csl;
        if (habilitado != null && habilitado) {
            imageView.setImageResource(R.drawable.ic_shopping_cart_black_32dp);
            imageView.setContentDescription("Habilitado");
            csl = AppCompatResources.getColorStateList(CarrinhosApp.getContext(), R.color.carrinhoHabilitado);
        } else {
            imageView.setImageResource(R.drawable.ic_remove_shopping_cart_black_32dp);
            imageView.setContentDescription("Desabilitado");
            csl = AppCompatResources.getColorStateList(CarrinhosApp.getContext(), R.color.carrinhoDesabilitado);
        }
        DrawableCompat.setTintList(imageView.getDrawable(), csl);
    }

    @BindingAdapter("editavel")
    public static void setEditavelEditText(EditText editText, boolean editavel) {
        if (!editavel) {
            editText.setEnabled(false);
            editText.setInputType(InputType.TYPE_NULL);
            editText.setTextColor(Color.BLACK);
        }
    }

    @BindingAdapter("vendaStatus")
    public static void setVendaStatus(TextView view, String status) {
        if (status != null && status.trim().toUpperCase().equals("FINALIZADA")) {
            view.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_all_blue_16dp, 0, 0, 0);
        } else { //Venda PENDENTE
            view.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done_black_16dp, 0, 0, 0);
        }
        view.setText(status);
    }

    @BindingAdapter("vendaValorPago")
    public static void setVendaValorPago(TextView view, Venda venda) {
        Long valor;
        if (venda.getTotal() == null || venda.getComissao() == null)
            valor = 0L;
        else
            valor = venda.getTotal() * (1 - venda.getComissao() / 100);
        setValorRealTextView(view, valor);
    }

    @BindingAdapter("vendaValorComissao")
    public static void setVendaValorComissao(TextView view, Venda venda) {
        Long valor;
        if (venda.getTotal() == null || venda.getComissao() == null)
            valor = 0L;
        else
            valor = venda.getTotal() * venda.getComissao() / 100;
        setValorRealTextView(view, valor);
    }

    @BindingAdapter(value = "valorInteiroAttrChanged")
    public static void setListenerOnEditTextInteiro(EditText editText, final InverseBindingListener listener) {
        if (listener != null)
            editText.addTextChangedListener(new InverseBindingTextChangedListener(listener));
    }

    @InverseBindingAdapter(attribute = "valorInteiro")
    public static Integer getValorInteiro(EditText editText) {
        String texto = editText.getText().toString();
        try {
            return texto.isEmpty() ? 0 : Integer.valueOf(texto);
        } catch (NumberFormatException e) {
            LogUtil.Log(TAG, texto, Log.DEBUG);
            LogUtil.Log(TAG, e.getLocalizedMessage(), Log.ERROR);
            return 0;
        }
    }

    @BindingAdapter("valorInteiro")
    public static void setValorInteiro(TextView textView, Integer valor) {
        if (textView instanceof TextInputEditText)
            setValorInteiroEditText((TextInputEditText) textView, valor);
        else
            setValorInteiroTextView(textView, valor);
    }

    private static void setValorInteiroTextView(TextView textView, Integer valor) {
        textView.setText(String.format(Locale.getDefault(), "%d %%", valor));
    }

    private static void setValorInteiroEditText(TextInputEditText editText, Integer valor) {
        if (Objects.equals(valor, getValorInteiro(editText)))
            return;
        editText.setText(valor == null ? "" : String.valueOf(valor));
    }

    @BindingAdapter(value = "valorRealAttrChanged")
    public static void setListenerOnEditTextMoeda(EditText editText, final InverseBindingListener listener) {
        if (listener != null)
            editText.addTextChangedListener(new InverseBindingTextChangedListener(listener));
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
        if (valor == null)
            valor = 0L;
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