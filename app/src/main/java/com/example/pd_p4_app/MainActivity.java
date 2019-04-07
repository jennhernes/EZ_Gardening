package com.example.pd_p4_app;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static AppDatabase db;

    private User dangerPlant; // the plant with the smallest difference between current humidity and minimum humidity

    private GestureDetectorCompat gestureObject;

    private View view; // the window
    private Toolbar myToolbar; // custom action bar
    private ActionBar actionbar; // action bar object, to be set to myToolBar
    private Drawable drawerToggle; // the nav drawer icon
    private DrawerLayout mDrawerLayout; // the layout for the nav drawer

    private Button buttonBigCircle; // the large circular button in the middle of the screen
    private Button buttonAddPlant; // the 'Add' button at the bottom of the screen

    // onCreate is called every time the activity starts
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "plantdb").allowMainThreadQueries().build();
        // load the activity layout as well as the nav drawer and the toolbar
        setContentView(R.layout.activity_main);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        // Add an onClickListener to the Add Plant button to react when the user presses the button
        buttonAddPlant = findViewById(R.id.buttonAddPlant);
        buttonAddPlant.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Switch to AddPlantActivity
                // removes AddPlantActivity from the stack once the user finishes with it
                Intent intent = new Intent(MainActivity.this, AddPlantActivity.class);
                startActivityForResult(intent, RESULT_OK);
            }
        });

        // Add an onClickListener to the circular button to react when the user presses the button
        buttonBigCircle = findViewById(R.id.buttonBigCircle);
        buttonBigCircle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Switch to ListActivity
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });

        // Create a common gesture listener object
        // This will be used to respond to swipe gestures
        gestureObject = new GestureDetectorCompat(this, new MainActivity.DetectSwipeGestureListener());
        View mainLayout = findViewById(R.id.mainLayout);

        // Set the touch listener for the main view to be our custom gesture listener
        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureObject.onTouchEvent(event)) {
                    return true;
                }
                return false;
            }
        });

        // Set the colour for the hamburger icon in the top left of the screen
        drawerToggle = myToolbar.getNavigationIcon();
        drawerToggle.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);

        // Add a NavigationItemSelectedListener so that the options in the nav drawer can be pressed
        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent;
                switch(id)
                {
                    case R.id.nav_settings:
                        // Switch to SettingsActivity
                        // removes SettingsActivity from the stack once the user finishes with it
                        intent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivityForResult(intent, RESULT_OK);
                        return true;

                    case R.id.nav_help:
                        // Switch to HelpActivity
                        // removes HelpActivity from the stack once the user finishes with it
                        intent = new Intent(MainActivity.this, HelpActivity.class);
                        startActivityForResult(intent, RESULT_OK);
                        return true;

                    default:
                        return true;
                }
            }
        });

        updateHomeScreen();
    }

    // Modify the look of the activity based on the humidity level of the plants that are
    // being tracked in the app. The colour scheme will be red, yellow, green or grey and
    // the text on the circular plant gives the user critical information
    public void updateHomeScreen() {
        view = this.getWindow().getDecorView();
        if (db.userDao().countUsers() == 0) { // GREY, no plants in app
            buttonBigCircle.setText(R.string.noPlantsAdded);
            buttonBigCircle.getBackground().setColorFilter(getResources().getColor(R.color.colorCircleButtonGrey), PorterDuff.Mode.SRC_ATOP);

            buttonAddPlant.getBackground().setColorFilter(getResources().getColor(R.color.colorAddButtonGrey), PorterDuff.Mode.SRC_ATOP);

            myToolbar.setBackgroundColor(getResources().getColor(R.color.colorAddButtonGrey));

            view.setBackgroundColor(getResources().getColor(R.color.colorHomeBackgroundGrey));

        } else {
            // at least one plant, sort them to get the most dehydrated plant at the beginning of the list
            List<User> users = db.userDao().getSortedUsers();
            dangerPlant = users.get(0);
            int humidityDifference = dangerPlant.getPlantCurrentHumidity() - dangerPlant.getPlantMinHumidity();

            String displayName = dangerPlant.getPlantName();
            if (displayName.length() > getResources().getInteger(R.integer.intDisplayNameLength)) {
                displayName = displayName.substring(0, getResources().getInteger(R.integer.intDisplayNameLength)) + "...";
            }
            // RED, there is a plant below acceptable humidity levels
            if (humidityDifference < getResources().getInteger(R.integer.intRedDiff)) {
                buttonBigCircle.setText(displayName + "\nneeds water\nimmediately!\n\n" +
                        "Humidity level at\n" + dangerPlant.getPlantCurrentHumidity() + "%");
                buttonBigCircle.getBackground().setColorFilter(getResources().getColor(R.color.colorCircleButtonRed), PorterDuff.Mode.SRC_ATOP);

                buttonAddPlant.getBackground().setColorFilter(getResources().getColor(R.color.colorAddButtonRed), PorterDuff.Mode.SRC_ATOP);

                myToolbar.setBackgroundColor(getResources().getColor(R.color.colorAddButtonRed));

                view.setBackgroundColor(getResources().getColor(R.color.colorHomeBackgroundRed));

                // YELLOW, worst off plant is close to minimum acceptable humidity level
            } else if (humidityDifference < getResources().getInteger(R.integer.intYellowDiff)) {
                buttonBigCircle.setText(displayName + "\nwill need to be watered soon.\n\n" +
                        "Humidity level at\n" + dangerPlant.getPlantCurrentHumidity() + "%");
                buttonBigCircle.getBackground().setColorFilter(getResources().getColor(R.color.colorCircleButtonYellow), PorterDuff.Mode.SRC_ATOP);

                buttonAddPlant.getBackground().setColorFilter(getResources().getColor(R.color.colorAddButtonYellow), PorterDuff.Mode.SRC_ATOP);

                myToolbar.setBackgroundColor(getResources().getColor(R.color.colorAddButtonYellow));

                view.setBackgroundColor(getResources().getColor(R.color.colorHomeBackgroundYellow));

                // GREEN, all plants are watered
            } else {
                buttonBigCircle.setText(R.string.allPlantsWatered);
                buttonBigCircle.getBackground().setColorFilter(getResources().getColor(R.color.colorCircleButtonGreen), PorterDuff.Mode.SRC_ATOP);

                buttonAddPlant.getBackground().setColorFilter(getResources().getColor(R.color.colorAddButtonGreen), PorterDuff.Mode.SRC_ATOP);

                myToolbar.setBackgroundColor(getResources().getColor(R.color.colorAddButtonGreen));

                view.setBackgroundColor(getResources().getColor(R.color.colorHomeBackgroundGreen));
            }
        }
    }

    // Inflate the toggle view menu item so it will show up in the app
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toggle_menu_main, menu);
        return true;
    }

    // React to the user pressing one of the action bar items here
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // open the nav drawer
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_toggle_main: // switch to ListActivity
                Intent intent = new Intent(this, ListActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Pass activity on touch event to the gesture detector.
        this.gestureObject.onTouchEvent(event);
        // Return true to tell android OS that event has been consumed, do not pass it to other event listeners.
        return super.onTouchEvent(event);
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
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
            }

            if((deltaYAbs >= MIN_SWIPE_DISTANCE_Y) && (deltaYAbs <= MAX_SWIPE_DISTANCE_Y))
            {
                if(deltaY > 0) {
                    Log.d(DEBUG_TAG,"Swipe up");
                }else { // refresh data in app
                    FetchData process = new FetchData();
                    process.execute(MainActivity.this);
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
