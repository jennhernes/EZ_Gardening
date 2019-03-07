package com.example.pd_p4_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        Plant testPlant = ((MyApplication)this.getApplication()).getPlant();

        ArrayList<Plant> plants = new ArrayList<>();
        plants.add(testPlant);
        PlantsAdapter adapter = new PlantsAdapter(this, plants);
        ListView listView = findViewById(R.id.plant_list);
        listView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toggle_menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_toggle_list:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}

class PlantsAdapter extends ArrayAdapter<Plant> {
    public PlantsAdapter(Context context, ArrayList<Plant> plants) {
        super(context, 0, plants);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Get the data item for this position
        Plant plant = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        // Lookup view for data population
        TextView plantName = convertView.findViewById(R.id.plantName);
        TextView currentHumidity = convertView.findViewById(R.id.currentHumidity);
        // Populate the data into the template view using the data object
        plantName.setText(plant.getName());
        currentHumidity.setText(Integer.toString(plant.getCurrentHumidity()));
        // Return the completed view to render on screen
        return convertView;
    }
}