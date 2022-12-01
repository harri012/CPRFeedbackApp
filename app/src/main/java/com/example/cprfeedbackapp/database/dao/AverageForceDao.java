package com.example.cprfeedbackapp.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.cprfeedbackapp.database.entity.AverageForce;

import java.util.List;

@Dao
public interface AverageForceDao {

    @Insert
    void insertAverageForce(AverageForce force);

    @Query("SELECT * FROM average_force_table WHERE datetime=:datetime")
    List<AverageForce> getAverageForceDatapoints(String datetime);
}
