package com.example.pd_p4_app;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static TextView data;
    public static AppDatabase db;

    private View view; // the window
    private Toolbar myToolbar; // custom action bar
    private ActionBar actionbar; // action bar object, to be set to myToolBar
    private Drawable drawerToggle; // the nav drawer icon
    private DrawerLayout mDrawerLayout; // the layout for the nav drawer
    private Button buttonBigCircle; // the large circular button in the middle of the screen
    private Button buttonAddPlant; // the 'Add' button at the bottom of the screen
    private User dangerPlant; // the plant with the smallest difference between current humidity and minimum humidity

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

        view = this.getWindow().getDecorView();
        drawerToggle = myToolbar.getNavigationIcon();

        // Modify the look of the activity based on the humidity level of the plants that are
        // being tracked in the app. The colour scheme will be red, yellow, green or grey and
        // the text on the circular plant gives the user critical information
        if (db.userDao().countUsers() == 0) { // GREY, no plants in app
            buttonBigCircle.setText(R.string.noPlantsAdded);
            buttonBigCircle.getBackground().setColorFilter(getResources().getColor(R.color.colorCircleButtonGrey), PorterDuff.Mode.SRC_ATOP);

            buttonAddPlant.getBackground().setColorFilter(getResources().getColor(R.color.colorAddButtonGrey), PorterDuff.Mode.SRC_ATOP);

            drawerToggle.setColorFilter(getResources().getColor(R.color.colorAddButtonGrey), PorterDuff.Mode.SRC_ATOP);

            view.setBackgroundColor(getResources().getColor(R.color.colorHomeBackgroundGrey));

        } else {
            // at least one plant, sort them to get the most dehydrated plant at the beginning of the list
            List<User> users = db.userDao().getSortedUsers();
            dangerPlant = users.get(0);
            int humidityDifference = Integer.parseInt(dangerPlant.getPlantCurrentHumidity()) - Integer.parseInt(dangerPlant.getPlantMinHumidity());

            // RED, there is a plant below acceptable humidity levels
            if (humidityDifference < getResources().getInteger(R.integer.intRedDiff)) {
                buttonBigCircle.setText(dangerPlant.getPlantName() + "\nneeds water\nimmediately!\n\n" +
                        "Humidity level at\n" + dangerPlant.getPlantCurrentHumidity() + "%");
                buttonBigCircle.getBackground().setColorFilter(getResources().getColor(R.color.colorCircleButtonRed), PorterDuff.Mode.SRC_ATOP);

                buttonAddPlant.getBackground().setColorFilter(getResources().getColor(R.color.colorAddButtonRed), PorterDuff.Mode.SRC_ATOP);

                drawerToggle.setColorFilter(getResources().getColor(R.color.colorAddButtonRed), PorterDuff.Mode.SRC_ATOP);

                view.setBackgroundColor(getResources().getColor(R.color.colorHomeBackgroundRed));

            // YELLOW, worst off plant is close to minimum acceptable humidity level
            } else if (humidityDifference < getResources().getInteger(R.integer.intYellowDiff)) {
                buttonBigCircle.setText(dangerPlant.getPlantName() + "\nis getting dry.\n\n" +
                        "Humidity level at\n" + dangerPlant.getPlantCurrentHumidity() + "%");
                buttonBigCircle.getBackground().setColorFilter(getResources().getColor(R.color.colorCircleButtonYellow), PorterDuff.Mode.SRC_ATOP);

                buttonAddPlant.getBackground().setColorFilter(getResources().getColor(R.color.colorAddButtonYellow), PorterDuff.Mode.SRC_ATOP);

                drawerToggle.setColorFilter(getResources().getColor(R.color.colorAddButtonYellow), PorterDuff.Mode.SRC_ATOP);

                view.setBackgroundColor(getResources().getColor(R.color.colorHomeBackgroundYellow));

            // GREEN, all plants are watered
            } else {
                buttonBigCircle.setText(R.string.allPlantsWatered);
                buttonBigCircle.getBackground().setColorFilter(getResources().getColor(R.color.colorCircleButtonGreen), PorterDuff.Mode.SRC_ATOP);

                buttonAddPlant.getBackground().setColorFilter(getResources().getColor(R.color.colorAddButtonGreen), PorterDuff.Mode.SRC_ATOP);

                drawerToggle.setColorFilter(getResources().getColor(R.color.colorAddButtonGreen), PorterDuff.Mode.SRC_ATOP);

                view.setBackgroundColor(getResources().getColor(R.color.colorHomeBackgroundGreen));
            }
        }

        Button click = (Button) findViewById(R.id.buttonFetchData);

        click.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                fetchData process = new fetchData();
                process.execute();
            }
        });


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


}
