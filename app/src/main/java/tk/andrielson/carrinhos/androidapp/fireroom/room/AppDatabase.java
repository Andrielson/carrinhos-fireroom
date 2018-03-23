package tk.andrielson.carrinhos.androidapp.fireroom.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import org.jetbrains.annotations.Contract;

import tk.andrielson.carrinhos.androidapp.CarrinhosApp;
import tk.andrielson.carrinhos.androidapp.fireroom.room.converters.DateToStringConverter;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.ItemVendaDaoRoom;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.ProdutoDaoRoom;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.VendaDaoRoom;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.VendedorDaoRoom;
import tk.andrielson.carrinhos.androidapp.fireroom.room.entities.ItemVendaRoom;
import tk.andrielson.carrinhos.androidapp.fireroom.room.entities.ProdutoRoom;
import tk.andrielson.carrinhos.androidapp.fireroom.room.entities.VendaRoom;
import tk.andrielson.carrinhos.androidapp.fireroom.room.entities.VendedorRoom;

@Database(entities = {ProdutoRoom.class, VendedorRoom.class, VendaRoom.class, ItemVendaRoom.class}, exportSchema = false, version = 3)
@TypeConverters(DateToStringConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProdutoDaoRoom produtoDao();

    public abstract VendedorDaoRoom vendedorDao();

    public abstract VendaDaoRoom vendaDao();

    public abstract ItemVendaDaoRoom itemVendaDao();

    @Contract(pure = true)
    public static AppDatabase getInstancia() {
        return InnerStaticHolder.INSTANCIA;
    }

    private static class InnerStaticHolder {
        static final AppDatabase INSTANCIA = Room.databaseBuilder(CarrinhosApp.getContext(), AppDatabase.class, "CARRINHOS-ROOM.db")
                .fallbackToDestructiveMigration().build();
    }
}
