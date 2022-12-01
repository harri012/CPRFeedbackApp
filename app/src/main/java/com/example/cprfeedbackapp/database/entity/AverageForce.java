package com.example.cprfeedbackapp.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "average_force_table")
public class AverageForce {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "average_force")
    private double forceDatapoint;


    @ColumnInfo(name = "datetime")
    private String datetime;

    // Constructor
    public AverageForce(int id, double forceDatapoint, String datetime) {
        this.id = id;
        this.forceDatapoint = forceDatapoint;
        this.datetime = datetime;
    }

    public int getId() {
        return id;
    }

    public double getForceDatapoint() {
        return forceDatapoint;
    }

    public String getDatetime() {
        return datetime;
    }
}
