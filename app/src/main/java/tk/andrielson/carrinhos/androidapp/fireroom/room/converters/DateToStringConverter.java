package tk.andrielson.carrinhos.androidapp.fireroom.room.converters;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.Nullable;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Classe que contém métodos para converter String em Date e vice-versa.
 * É utilizada principalmente pela biblioteca de persistência Android Room.
 */
public final class DateToStringConverter {
    /**
     * Formatador de data: 2018-03-30.
     */
    private static final DateFormat formato = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    /**
     * Converte uma String no formato yyyy-MM-dd para Date.
     *
     * @param value a String a ser convertida.
     * @return a data obtida da conversão da String.
     */
    @Nullable
    @TypeConverter
    public static Date dateFromString(String value) {
        try {
            return value == null ? null : formato.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converte um Date em uma String no formato yyyy-MM-dd.
     *
     * @param value o Date a ser convertido.
     * @return a String obtida a partir da conversão.
     */
    @Nullable
    @TypeConverter
    public static String stringFromDate(Date value) {
        return value == null ? null : formato.format(value);
    }
}
