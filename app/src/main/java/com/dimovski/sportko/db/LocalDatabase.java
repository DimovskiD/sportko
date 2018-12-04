package com.dimovski.sportko.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import com.dimovski.sportko.data.DbConstants;
import com.dimovski.sportko.db.dao.EventDao;
import com.dimovski.sportko.db.dao.UserDao;
import com.dimovski.sportko.db.model.Event;
import com.dimovski.sportko.db.model.User;

@Database(entities = {Event.class, User.class}, version = 1)
public abstract class LocalDatabase extends RoomDatabase {

    public abstract EventDao eventDao();
    public abstract UserDao userDao();

    //SINGLETON
    private static LocalDatabase INSTANCE;

    public static LocalDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (LocalDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LocalDatabase.class, DbConstants.DB_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}