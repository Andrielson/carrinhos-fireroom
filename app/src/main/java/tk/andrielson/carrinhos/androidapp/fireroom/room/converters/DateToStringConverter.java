package tk.andrielson.carrinhos.androidapp.fireroom.room.converters;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.Nullable;

import org.jetbrains.annotations.Contract;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class DateToStringConverter {
    private static final DateFormat formato = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

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

    @Nullable
    @TypeConverter
    public static String stringFromDate(Date value) {
        return value == null ? null : formato.format(value);
    }
}
