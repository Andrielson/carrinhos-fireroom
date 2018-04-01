package tk.andrielson.carrinhos.androidapp.utils;

import android.graphics.Color;
import android.widget.EditText;

import org.jetbrains.annotations.Contract;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * Created by Andrielson on 13/03/2018.
 */

public final class Util {

    private static NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

    @Contract("null -> !null")
    public static String longToRS(Long valor) {
        return valor == null ? "R$ 0,00" : numberFormat.format((double) valor / 100).replaceAll("(^\\D+)","$1 ");
    }

    @Contract("null -> null")
    public static Long RStoLong(String valor) {
        if (valor == null)
            return null;
        if (valor.isEmpty())
            return 0L;
        return Long.valueOf(valor.replaceAll("\\D", ""));
    }

    public static void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }
}
