package com.example.pd_p4_app;

import android.app.Application;

public class MyApplication extends Application {
    private Plant plant;

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(String name, int threshold, int currentHumidity) {
        plant = new Plant(name, threshold, currentHumidity);
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
