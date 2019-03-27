package com.example.pd_p4_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

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

        Drawable drawerToggle = myToolbar.getNavigationIcon();
        drawerToggle.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);

        Collections.sort(((MyApplication)getApplication()).plants, Plant.HumdityDiffComparator);
        final PlantsAdapter adapter = new PlantsAdapter(this, ((MyApplication)this.getApplication()).getPlants());
        ListView listView = findViewById(R.id.plant_list);
        listView.setAdapter(adapter);
        listView.setLongClickable(true);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                builder.setTitle("Modify plant")
                        .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ListActivity.this, EditPlantActivity.class);
                                intent.putExtra("pos", position);
                            }
                        })
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Plant toRemove = adapter.getItem(position);
                                adapter.remove(toRemove);
                            }
                        })
                        .setNeutralButton("Cancel", null)						//Do nothing on no
                        .show();
                return true;
            }
        });

        Button buttonAddPlant = findViewById(R.id.buttonAddPlant);
        buttonAddPlant.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ListActivity.this, AddPlantActivity.class);
                startActivity(intent);
            }
        });
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
        TextView minHumidity = convertView.findViewById(R.id.minHumidity);
        // Populate the data into the template view using the data object
        plantName.setText(plant.getName());
        currentHumidity.setText(Integer.toString(plant.getCurrentHumidity()));
        minHumidity.setText(Integer.toString(plant.getMinHumidity()));

        if (plant.getCurrentHumidity() - plant.getMinHumidity() < getContext().getResources().getInteger(R.integer.intRedDiff)) { // RED
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.colorBigButtonRed));
        } else if (plant.getCurrentHumidity() - plant.getMinHumidity() < getContext().getResources().getInteger(R.integer.intYellowDiff)) { // YELLOW
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.colorBigButtonYellow));
        }else { // GREEN
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.colorBigButtonGreen));
        }

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,20, 0, 20);

        convertView.setLayoutParams(lp);
        // Return the completed view to render on screen
        return convertView;
    }
}