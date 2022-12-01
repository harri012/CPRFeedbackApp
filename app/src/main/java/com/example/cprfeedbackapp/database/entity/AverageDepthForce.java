package com.example.cprfeedbackapp.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "average_table")
public class AverageDepthForce {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "average_force")
    private double forceDatapoint;

    @ColumnInfo(name = "average_depth")
    private double depthDatapoint;

    @ColumnInfo(name = "datetime")
    private String datetime;

    // Constructor
    public AverageDepthForce(int id, double forceDatapoint, double depthDatapoint, String datetime) {
        this.id = id;
        this.forceDatapoint = forceDatapoint;
        this.depthDatapoint = depthDatapoint;
        this.datetime = datetime;
    }

    public int getId() {
        return id;
    }

    public double getForceDatapoint() {
        return forceDatapoint;
    }

    public double getDepthDatapoint() {
        return depthDatapoint;
    }

    public String getDatetime() {
        return datetime;
    }
}
