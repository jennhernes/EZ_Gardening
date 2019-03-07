package com.example.pd_p4_app;

import android.app.Application;

import java.util.ArrayList;

public class MyApplication extends Application {

    ArrayList<Plant> plants = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        setPlant("Benjamin", 52, 70);
        setPlant("Clara", 50, 62);
    }

    public ArrayList<Plant> getPlants() {
        return plants;
    }

    public Plant getPlantAt(int position) {
        return plants.get(position);
    }

    public void setPlant(String name, int threshold, int currentHumidity) {
        plants.add(new Plant(name, threshold, currentHumidity));
    }
}

class Plant {
    private String name;
    private int threshold;
    private int currentHumidity;

    public Plant(String name, int threshold, int currentHumidity) {
        this.name = name;
        this.threshold = threshold;
        this.currentHumidity = currentHumidity;
    }

    public String getName() {
        return this.name;
    }

    public int getThreshold() {
        return this.threshold;
    }

    public int getCurrentHumidity() {
        return this.currentHumidity;
    }
}
