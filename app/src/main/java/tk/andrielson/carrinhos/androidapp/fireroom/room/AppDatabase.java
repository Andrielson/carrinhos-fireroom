package tk.andrielson.carrinhos.androidapp.fireroom.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import org.jetbrains.annotations.Contract;

import tk.andrielson.carrinhos.androidapp.CarrinhosApp;
import tk.andrielson.carrinhos.androidapp.fireroom.model.ItemVendaImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.model.ProdutoImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.model.VendaImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.model.VendedorImpl;
import tk.andrielson.carrinhos.androidapp.fireroom.room.converters.DateToStringConverter;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.ItemVendaRoomDao;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.ProdutoRoomDao;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.VendaRoomDao;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.VendedorRoomDao;

@Database(entities = {ProdutoImpl.class, VendedorImpl.class, VendaImpl.class, ItemVendaImpl.class}, exportSchema = false, version = 5)
@TypeConverters(DateToStringConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    @Contract(pure = true)
    public static AppDatabase getInstancia() {
        return InnerStaticHolder.INSTANCIA;
    }

    public abstract ProdutoRoomDao produtoDao();

    public abstract VendedorRoomDao vendedorDao();

    public abstract VendaRoomDao vendaDao();

    public abstract ItemVendaRoomDao itemVendaDao();

    private static class InnerStaticHolder {
        static final AppDatabase INSTANCIA = Room.databaseBuilder(CarrinhosApp.getContext(), AppDatabase.class, "CARRINHOS-ROOM.db")
                .fallbackToDestructiveMigration().build();
    }
}
