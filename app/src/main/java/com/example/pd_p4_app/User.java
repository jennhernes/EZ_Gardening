package com.example.pd_p4_app;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "user")
public class User {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "plant_name")
    private String plantName;

    @ColumnInfo(name = "plant_currenthumidity")
    private String plantCurrentHumidity;

    @ColumnInfo(name = "plant_minhumidity")
    private String plantMinHumidity;

    public int getUid(){
        return uid;
    }

    public void setUid(int uid){
        this.uid = uid;
    }
    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getPlantCurrentHumidity() {
        return plantCurrentHumidity;
    }

    public void setPlantCurrentHumidity(String plantCurrentHumidity) {
        this.plantCurrentHumidity = plantCurrentHumidity;
    }

    public String getPlantMinHumidity() {
        return plantMinHumidity;
    }

    public void setPlantMinHumidity(String plantMinHumidity) {
        this.plantMinHumidity = plantMinHumidity;
    }
}
