package tk.andrielson.carrinhos.androidapp.utils;

import android.graphics.Color;
import android.widget.EditText;

import org.jetbrains.annotations.Contract;

import java.util.Locale;

/**
 * Created by Andrielson on 13/03/2018.
 */

public final class Util {
    @Contract("null -> !null")
    public static String longToRS(Long valor) {
        return valor == null ? "R$ 0,00" : String.format(Locale.getDefault(), "R$ %.2f", (double) valor / 100);
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
