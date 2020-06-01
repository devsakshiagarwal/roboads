package com.goyal.roboads.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.goyal.roboads.db.dao.UserDao;
import com.goyal.roboads.db.model.Users;

@Database(entities = Users.class, version = 1)
public abstract class RoboadsDb extends RoomDatabase {
  private static final String ROBOADS_DB = "roboads_db";
  private static RoboadsDb instance;

  public static synchronized RoboadsDb getInstance(Context context) {
    if (instance == null) {
      instance = Room.databaseBuilder(context.getApplicationContext(), RoboadsDb.class, ROBOADS_DB)
          .fallbackToDestructiveMigration().build();
    }
    return instance;
  }

  public abstract UserDao userDao();
}
