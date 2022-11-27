package com.example.cprfeedbackapp.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "CprSession_table")
public class CprSessionDatapoint {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "force_applied")
    private double forceDatapoint;

    @ColumnInfo(name = "depth_measured")
    private double depthDatapoint;

    @ColumnInfo(name = "datetime")
    private String datetime;

    // Constructor
    public CprSessionDatapoint(int id, double forceDatapoint, double depthDatapoint, String datetime) {
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
