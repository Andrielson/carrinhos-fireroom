package tk.andrielson.carrinhos.androidapp.fireroom.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import org.jetbrains.annotations.Contract;

import tk.andrielson.carrinhos.androidapp.CarrinhosApp;
import tk.andrielson.carrinhos.androidapp.fireroom.room.converters.DateToStringConverter;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.ItemVendaRoomDao;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.ProdutoRoomDao;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.VendaRoomDao;
import tk.andrielson.carrinhos.androidapp.fireroom.room.dao.VendedorRoomDao;
import tk.andrielson.carrinhos.androidapp.fireroom.room.entities.ItemVendaRoom;
import tk.andrielson.carrinhos.androidapp.fireroom.room.entities.ProdutoRoom;
import tk.andrielson.carrinhos.androidapp.fireroom.room.entities.VendaRoom;
import tk.andrielson.carrinhos.androidapp.fireroom.room.entities.VendedorRoom;

@Database(entities = {ProdutoRoom.class, VendedorRoom.class, VendaRoom.class, ItemVendaRoom.class}, exportSchema = false, version = 4)
@TypeConverters(DateToStringConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProdutoRoomDao produtoDao();

    public abstract VendedorRoomDao vendedorDao();

    public abstract VendaRoomDao vendaDao();

    public abstract ItemVendaRoomDao itemVendaDao();

    @Contract(pure = true)
    public static AppDatabase getInstancia() {
        return InnerStaticHolder.INSTANCIA;
    }

    private static class InnerStaticHolder {
        static final AppDatabase INSTANCIA = Room.databaseBuilder(CarrinhosApp.getContext(), AppDatabase.class, "CARRINHOS-ROOM.db")
                .fallbackToDestructiveMigration().build();
    }
}
