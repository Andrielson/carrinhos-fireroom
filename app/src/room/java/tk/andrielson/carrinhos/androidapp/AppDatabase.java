package tk.andrielson.carrinhos.androidapp;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import tk.andrielson.carrinhos.androidapp.dao.ProdutoRoomDao;
import tk.andrielson.carrinhos.androidapp.data.model.ProdutoRoom;

/**
 * Created by Andrielson on 02/03/2018.
 */
@Database(entities = {ProdutoRoom.class}, exportSchema = false, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DBNAME = "Carrinhos.db";
    private static final Object sLock = new Object();
    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, DBNAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return INSTANCE;
    }

    public abstract ProdutoRoomDao produtoRoomDao();
}
