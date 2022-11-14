package com.example.cprfeedbackapp.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sensor_table")
//setting up table name and column
public class Sensor {
    @PrimaryKey(autoGenerate = true)
    public int uid;
    @ColumnInfo(name = "sensor_name")
    public String sensorName;
    @ColumnInfo(name = "sensor_value")
    public  String sensorValue;

    //Constructor
    public Sensor(int uid, String sensorName, String sensorValue) {
        this.uid = uid;
        this.sensorName = sensorName;
        this.sensorValue = sensorValue;
    }
}
