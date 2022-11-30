package com.example.cprfeedbackapp.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.cprfeedbackapp.database.entity.CprSessionDatapoint;

import java.util.List;

@Dao
public interface CprSessionDao {

    @Insert
    void insertSession(CprSessionDatapoint session);

    @Query("SELECT * FROM CprSession_table WHERE datetime=:datetime")
    List<CprSessionDatapoint> getSessionDatapoints(String datetime);

    @Query("SELECT * FROM CprSession_table")
    List<CprSessionDatapoint> getAllSessions();

    @Delete
    void deleteSession(CprSessionDatapoint session);

    @Query("DELETE FROM CprSession_table")
    void deleteAllSessions();

    @Query("SELECT DISTINCT datetime FROM CprSession_table")
    List<String> getAllUniqueDates();
}
