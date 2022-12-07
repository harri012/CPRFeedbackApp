package com.example.cprfeedbackapp.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.cprfeedbackapp.database.entity.AverageDepthForce;

import java.util.List;

@Dao
public interface AverageDepthForceDao {

        @Insert
        void insertAverageDepthForce(AverageDepthForce averageDepthForce);

        @Query("SELECT * FROM average_table WHERE datetime=:datetime")
        List<AverageDepthForce> getAverageDepthForceDatapoints(String datetime);

        @Query("SELECT DISTINCT datetime FROM average_table")
        List<String> getAllUniqueDates();
}