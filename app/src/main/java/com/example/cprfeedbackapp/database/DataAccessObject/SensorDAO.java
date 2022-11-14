package com.example.cprfeedbackapp.database.DataAccessObject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.cprfeedbackapp.database.entity.Sensor;

import java.util.List;

//Data Access Object
@Dao
public interface SensorDAO {
    @Query("SELECT * FROM sensor_table")
    List<Sensor> getALL();

    @Query("SeLECT * FROM sensor_table WHERE uid=:uid")
    Sensor findById(int uid);

    @Insert
        // can accept multiple sensors object with ..
    void insertAll(Sensor... sensors);

    @Delete
    void delete(Sensor sensor);

}
