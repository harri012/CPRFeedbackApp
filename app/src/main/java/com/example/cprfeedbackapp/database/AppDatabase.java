package com.example.cprfeedbackapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.cprfeedbackapp.database.dao.CprSessionDao;
import com.example.cprfeedbackapp.database.entity.CprSessionDatapoint;

@Database(entities = {CprSessionDatapoint.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase instance;
    private static final String DB_NAME = "CprSessionDatabase";

    protected AppDatabase(){}

    private static AppDatabase create(Context context) {

        // All database operations will run on a separate, parallel thread
        // Might need to modify the thread
        return Room.databaseBuilder(context, AppDatabase.class, DB_NAME).allowMainThreadQueries().build();
    }

    // Singleton design for generating an AppDatabase object
    public static synchronized AppDatabase getInstance(Context context) {

        if (instance == null)
            instance = create(context);

        return instance;
    }

    public abstract CprSessionDao cprSessionDao();
}
