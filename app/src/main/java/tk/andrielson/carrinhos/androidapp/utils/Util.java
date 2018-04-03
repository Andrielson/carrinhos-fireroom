package tk.andrielson.carrinhos.androidapp.utils;

import android.graphics.Color;
import android.util.Log;
import android.widget.EditText;

import org.jetbrains.annotations.Contract;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import tk.andrielson.carrinhos.androidapp.fireroom.room.converters.DateToStringConverter;

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

    public static Date inicioSemana() {
        Calendar calendar = Calendar.getInstance();
        Date hoje = DateToStringConverter.dateFromString("2018-01-26");
        calendar.setTime(hoje);
        int dow = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE, -1 * dow + 1);
        return calendar.getTime();
    }

    public static Date inicioMes() {
        Calendar calendar = Calendar.getInstance();
        Date hoje = DateToStringConverter.dateFromString("2018-01-26");
        calendar.setTime(hoje);
        int dom = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DATE, -1 * dom + 1);
        return calendar.getTime();
    }
}
