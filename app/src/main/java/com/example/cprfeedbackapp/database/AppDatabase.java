package com.example.cprfeedbackapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.cprfeedbackapp.database.dao.AverageDepthForceDao;
import com.example.cprfeedbackapp.database.dao.AverageForceDao;
import com.example.cprfeedbackapp.database.dao.WaveformForceDao;
import com.example.cprfeedbackapp.database.entity.AverageDepthForce;
import com.example.cprfeedbackapp.database.entity.AverageForce;
import com.example.cprfeedbackapp.database.entity.WaveformForce;

@Database(entities = {AverageDepthForce.class, AverageForce.class, WaveformForce.class}, version = 2)
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

    public abstract AverageDepthForceDao averageDepthForceDao();
    public abstract AverageForceDao averageForceDao();
    public abstract WaveformForceDao waveformForceDao();
}
