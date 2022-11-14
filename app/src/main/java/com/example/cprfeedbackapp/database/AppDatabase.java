package com.example.cprfeedbackapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.cprfeedbackapp.database.DataAccessObject.SensorDAO;
import com.example.cprfeedbackapp.database.entity.Sensor;

@Database(entities = {Sensor.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase instance;
    private static final String DB_Name = "sensorsdatabase";
    protected AppDatabase(){}

    private static AppDatabase create(Context context){
        return Room.databaseBuilder(context, AppDatabase.class, DB_Name).allowMainThreadQueries().build();
    }

    public static synchronized AppDatabase getInstance(Context context){
        if (instance == null){
            instance = create(context);
        }
        return instance;
    }

    public abstract SensorDAO sensorDAO();

}
