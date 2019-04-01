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
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    private Toolbar myToolbar; // custom action bar
    private ActionBar actionbar; // action bar object, to be set to myToolBar
    private Drawable drawerToggle; // the nav drawer icon
    private DrawerLayout mDrawerLayout; // the layout for the nav drawer
    private Button buttonAddPlant; // the 'Add' button at the bottom of the screen

    // onCreate is called every time the activity starts
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // load the activity layout as well as the nav drawer and the toolbar
        setContentView(R.layout.activity_list);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        // Add an onClickListener to the Add Plant button to react when the user presses the button
        buttonAddPlant = findViewById(R.id.buttonAddPlant);
        buttonAddPlant.getBackground().setColorFilter(getResources().getColor(R.color.colorAddButtonGreen), PorterDuff.Mode.SRC_ATOP);
        buttonAddPlant.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Switch to AddPlantActivity
                // removes AddPlantActivity from the stack once the user finishes with it
                Intent intent = new Intent(ListActivity.this, AddPlantActivity.class);
                startActivityForResult(intent, RESULT_OK);
            }
        });

        // Set the colour for the hamburger icon in the top left of the screen
        drawerToggle = myToolbar.getNavigationIcon();
        drawerToggle.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);

        // Display the plants in a list
        // PlantsAdapter is a custom AdapterView that allows me to customize the look
        // of the list however I want
        List<User> users = MainActivity.db.userDao().getSortedUsers();
        final PlantsAdapter adapter = new PlantsAdapter(this, users);
        ListView listView = findViewById(R.id.plant_list);
        listView.setAdapter(adapter);
        // Add an onItemLongClickListener to react when the user long presses on a list item

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                // display a Dialog which allows the user to Delete or Edit a plant, or cancel to
                // return to the list
                AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
                builder.setTitle("Modify plant")
                        .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ListActivity.this, EditPlantActivity.class);
                                intent.putExtra("pos", position);
                                // TODO: start EditPlantActivity here
                            }
                        })
                        .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // use the PlantAdapter to remove the item
                                User toRemove = adapter.getItem(position);
                                MainActivity.db.userDao().deleteUser(toRemove);
                                adapter.remove(toRemove);

                            }
                        })
                        .setNeutralButton("Cancel", null)						//Do nothing on no
                        .show();

            }
        });
    }

    // Inflate the toggle view menu item so it will show up in the app
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toggle_menu_list, menu);
        return true;
    }

    // React to the user pressing one of the action bar items here
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // open the nav drawer
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_toggle_list: // switch to MainActivity
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}

// Custom Adapter to display the list of plants
class PlantsAdapter extends ArrayAdapter<User> {
    public PlantsAdapter(Context context, List<User> plants) {
        super(context, 0, plants);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Get the data item for this position
        User plant = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        // Find the views to use for data population
        TextView plantName = convertView.findViewById(R.id.plantName);
        TextView currentHumidity = convertView.findViewById(R.id.currentHumidity);
        TextView minHumidity = convertView.findViewById(R.id.plantMinHumidity);

        // Set the text values
        plantName.setText(plant.getPlantName());
        currentHumidity.setText(plant.getPlantCurrentHumidity());
        minHumidity.setText(plant.getPlantMinHumidity());

        int humidityDifference = Integer.parseInt(plant.getPlantCurrentHumidity()) - Integer.parseInt(plant.getPlantMinHumidity());
        // Set the background colour based on the difference between current humidity and minimum humidity
        if (humidityDifference < getContext().getResources().getInteger(R.integer.intRedDiff)) { // RED
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.colorCircleButtonRed));
        } else if (humidityDifference < getContext().getResources().getInteger(R.integer.intYellowDiff)) { // YELLOW
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.colorCircleButtonYellow));
        }else { // GREEN
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.colorCircleButtonGreen));
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
