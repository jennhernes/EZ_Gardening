package com.example.pd_p4_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    public static PlantsAdapter adapter; // the custom adapter object

    private Toolbar myToolbar; // custom action bar
    private ActionBar actionbar; // action bar object, to be set to myToolBar
    private Drawable drawerToggle; // the nav drawer icon
    private DrawerLayout mDrawerLayout; // the layout for the nav drawer

    private Button buttonAddPlant; // the 'Add' button at the bottom of the screen

    // This is the gesture detector compat instance.
    private GestureDetectorCompat gestureObject;

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

        // Set the colour for the hamburger icon in the top left of the screen
        drawerToggle = myToolbar.getNavigationIcon();
        drawerToggle.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);

        // Add an onClickListener to the Add Plant button to react when the user presses the button
        buttonAddPlant = findViewById(R.id.buttonAddPlant);
        buttonAddPlant.getBackground().setColorFilter(getResources().getColor(R.color.colorAddButtonGreen),
                PorterDuff.Mode.SRC_ATOP);
        buttonAddPlant.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Switch to AddPlantActivity
                // removes AddPlantActivity from the stack once the user finishes with it
                Intent intent = new Intent(ListActivity.this, AddPlantActivity.class);
                startActivityForResult(intent, RESULT_OK);
            }
        });

        // Create a common gesture listener object
        // This will be used to respond to swipe gestures
        gestureObject = new GestureDetectorCompat(this, new DetectSwipeGestureListener());
        View listLayout = findViewById(R.id.listLayout);
        listLayout.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureObject.onTouchEvent(event)) {
                    return true;
                }
                return false;
            }
        });

        // Need to also set the listener for the custom adapter view
        View innerList = findViewById(R.id.plant_list);
        innerList.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureObject.onTouchEvent(event)) {
                    return true;
                }
                return false;
            }
        });

        // Add a NavigationItemSelectedListener so that the options in the nav drawer can be pressed
        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent;
                switch (id) {
                    case R.id.nav_settings:
                        // Switch to SettingsActivity
                        // removes SettingsActivity from the stack once the user finishes with it
                        intent = new Intent(ListActivity.this, SettingsActivity.class);
                        startActivityForResult(intent, RESULT_OK);
                        return true;

                    case R.id.nav_help:
                        // Switch to HelpActivity
                        // removes HelpActivity from the stack once the user finishes with it
                        intent = new Intent(ListActivity.this, HelpActivity.class);
                        startActivityForResult(intent, RESULT_OK);
                        return true;

                    default:
                        return true;
                }
            }
        });

        // Display the plants in a list
        // PlantsAdapter is a custom AdapterView that allows me to customize the look
        // of the list however I want
        List<User> users = MainActivity.db.userDao().getSortedUsers();
        adapter = new PlantsAdapter(this, users);
        ListView listView = findViewById(R.id.plant_list);
        listView.setAdapter(adapter);

        // Add an onItemClickListener to react when the user long presses on a list item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                // display a Dialog which allows the user to Delete or Edit a plant, or cancel to
                // return to the list
                AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this, R.style.DialogTheme);
                builder.setTitle(R.string.modifyPlantTitle);
                builder.setPositiveButton(R.string.modifyPlantOptionCancel, null)
                        .setNegativeButton(R.string.modifyPlantOptionEdit, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ListActivity.this, EditPlantActivity.class);
                                User toEdit = adapter.getItem(position);
                                intent.putExtra("uid", toEdit.getUid());
                                startActivity(intent);
                            }
                        })
                        .setNeutralButton(R.string.modifyPlantOptionDelete, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // use the PlantAdapter to remove the item
                                User toRemove = adapter.getItem(position);
                                MainActivity.db.userDao().deleteUser(toRemove);
                                adapter.remove(toRemove);
                            }
                        })
                        .show();

            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Pass activity on touch event to the gesture detector.
        this.gestureObject.onTouchEvent(event);
        // Return true to tell android OS that event has been consumed, do not pass it to other event listeners.
        return super.onTouchEvent(event);
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

    private class DetectSwipeGestureListener extends GestureDetector.SimpleOnGestureListener {

        public String DEBUG_TAG = "Gesture";

        // Minimum x and y axis swipe distance.
        private int MIN_SWIPE_DISTANCE_X = 100;
        private int MIN_SWIPE_DISTANCE_Y = 100;

        // Maximum x and y axis swipe distance.
        private int MAX_SWIPE_DISTANCE_X = 1000;
        private int MAX_SWIPE_DISTANCE_Y = 1000;

        // This method is invoked when a swipe gesture occurs.
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            // Get swipe delta value in x
            float deltaX = e1.getX() - e2.getX();

            // Get swipe delta value in y
            float deltaY = e1.getY() - e2.getY();

            // Get absolute value.
            float deltaXAbs = Math.abs(deltaX);
            float deltaYAbs = Math.abs(deltaY);

            // If the swipe was larger than the minimum distance, but smaller than the maximum,
            // process the event
            if((deltaXAbs >= MIN_SWIPE_DISTANCE_X) && (deltaXAbs <= MAX_SWIPE_DISTANCE_X))
            {
                Log.d(DEBUG_TAG,"Swipe left/right");
                // switch to MainActivity
                Intent intent = new Intent(ListActivity.this, MainActivity.class);
                startActivity(intent);
            }

            if((deltaYAbs >= MIN_SWIPE_DISTANCE_Y) && (deltaYAbs <= MAX_SWIPE_DISTANCE_Y))
            {
                if(deltaY > 0) {
                    Log.d(DEBUG_TAG,"Swipe up");
                }else { // refresh data in app
                    FetchData process = new FetchData();
                    process.execute(ListActivity.this);
                }
            }
            return true;
        }

        // It is necessary to return true from onDown for the onFling event to register
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        // Invoked when single tap screen.
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.d(DEBUG_TAG,"Single tap occurred.");
            return true;
        }

        // Invoked when double tap screen.
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.d(DEBUG_TAG,"Double tap occurred.");
            return true;
        }
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
        currentHumidity.setText(Integer.toString(plant.getPlantCurrentHumidity()));
        minHumidity.setText(Integer.toString(plant.getPlantMinHumidity()));

        int humidityDifference = plant.getPlantCurrentHumidity() - plant.getPlantMinHumidity();
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
