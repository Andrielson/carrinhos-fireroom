package tk.andrielson.carrinhos.androidapp.converter;

import android.arch.persistence.room.TypeConverter;

import java.math.BigDecimal;

/**
 * Created by Andrielson on 02/03/2018.
 */

public final class BigDecimalToLongConverter {
    @TypeConverter
    public static BigDecimal fromLong(Long value) {
        return value == null ? null : (new BigDecimal(value)).divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_CEILING);
    }

    @TypeConverter
    public static Long bigDecimalToLong(BigDecimal value) {

        return value == null ? null : value.multiply(BigDecimal.valueOf(100)).longValue();
    }
}
