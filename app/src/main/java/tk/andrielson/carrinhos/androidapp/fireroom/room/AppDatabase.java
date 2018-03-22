package tk.andrielson.carrinhos.androidapp.fireroom.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import org.jetbrains.annotations.Contract;

import tk.andrielson.carrinhos.androidapp.CarrinhosApp;
import tk.andrielson.carrinhos.androidapp.data.model.ProdutoImpl;
import tk.andrielson.carrinhos.androidapp.data.model.VendedorImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.room.converters.DateToStringConverter;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.ProdutoDaoRoom;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.VendedorDaoRoom;

@Database(entities = {ProdutoImpl.class, VendedorImpl.class}, exportSchema = false, version = 1)
@TypeConverters(DateToStringConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProdutoDaoRoom produtoDao();

    public abstract VendedorDaoRoom vendedorDao();

    @Contract(pure = true)
    public static AppDatabase getInstancia() {
        return InnerStaticHolder.INSTANCIA;
    }

    private static class InnerStaticHolder {
        static final AppDatabase INSTANCIA = Room.databaseBuilder(CarrinhosApp.getContext(), AppDatabase.class, "CARRINHOS-ROOM.db")
                .fallbackToDestructiveMigration().build();
    }
}
