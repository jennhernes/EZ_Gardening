package com.example.pd_p4_app;

import android.app.Application;

import java.util.ArrayList;
import java.util.Comparator;

public class MyApplication extends Application {

    ArrayList<Plant> plants = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        addPlant("Benjamin", 52, 50);
        addPlant("Clara", 50, 55);
        addPlant("David", 65, 78);
    }

    public ArrayList<Plant> getPlants() {
        return plants;
    }

    public Plant getPlantAt(int position) {
        return plants.get(position);
    }

    public void addPlant(String name, int threshold, int currentHumidity) {
        plants.add(new Plant(name, threshold, currentHumidity));
    }
}

class Plant {
    private String name;
    private int minHumidity;
    private int currentHumidity;

    public Plant(String name, int minHumidity, int currentHumidity) {
        this.name = name;
        this.minHumidity = minHumidity;
        this.currentHumidity = currentHumidity;
    }

    public String getName() {
        return this.name;
    }

    public int getMinHumidity() {
        return this.minHumidity;
    }

    public int getCurrentHumidity() {
        return this.currentHumidity;
    }

    public static Comparator<Plant> HumdityDiffComparator = new Comparator<Plant>() {

        public int compare(Plant p1, Plant p2) {
            int diff1 = p1.currentHumidity - p1.minHumidity;
            int diff2 = p2.currentHumidity - p2.minHumidity;

            if (diff1 < diff2) {
                return -1;
            } else if (diff1 == diff2) {
                return 0;
            } else {
                return 1;
            }
        }};
}
