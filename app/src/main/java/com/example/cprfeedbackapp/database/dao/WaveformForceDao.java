package com.example.cprfeedbackapp.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.cprfeedbackapp.database.entity.WaveformForce;

import java.util.List;

@Dao
public interface WaveformForceDao {


    @Insert
    void insertWaveformForce(WaveformForce force);

    @Query("SELECT * FROM force_table WHERE datetime=:datetime")
    List<WaveformForce> getWaveformForceDatapoints(String datetime);
}
