package com.example.pd_p4_app;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddPlantActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Button buttonConfirmAdd; // the button to confirm that the user wants to addUser this plant to the app

    // onCreate is called every time the activity starts
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // load the activity layout
        setContentView(R.layout.activity_add_plant);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        setSupportActionBar(myToolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        Drawable drawerToggle = myToolbar.getNavigationIcon();
        drawerToggle.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);

        final Toast toastId = Toast.makeText(AddPlantActivity.this, "Plant ID must be a number (without decimals).", Toast.LENGTH_SHORT);
        toastId.setGravity(Gravity.CENTER,0,0);

        final Toast toastName = Toast.makeText(AddPlantActivity.this, "Plants must have a name.", Toast.LENGTH_SHORT);
        toastName.setGravity(Gravity.CENTER,0,0);

        final Toast toastHumidity = Toast.makeText(AddPlantActivity.this, "Humidity must be a number between 0 and 100.", Toast.LENGTH_SHORT);
        toastHumidity.setGravity(Gravity.CENTER,0,0);

        // Add an onClickListener to the confirmation button to react when the user presses the button
        buttonConfirmAdd = findViewById(R.id.buttonConfirmAdd);
        buttonConfirmAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Extract the text given by the user
                int uid;
                int plantMinHumidity = -1;

                try {
                    uid = Integer.parseInt(((EditText) findViewById(R.id.editTextUid)).getText().toString());
                } catch (Exception e) {
                    toastId.show();
                    return;
                }
                if (MainActivity.db.userDao().getUserById(uid) != null) {
                    Toast toastDuplicateId = Toast.makeText(AddPlantActivity.this,
                            "There is already a plant with this ID.", Toast.LENGTH_SHORT);
                    toastDuplicateId.setGravity(Gravity.CENTER, 0, 0);
                    toastDuplicateId.show();
                    return;
                } else {

                    String plantName = ((EditText) findViewById(R.id.editTextPlantName)).getText().toString();

                    if (plantName.equals("")) {
                        toastName.show();
                        return;
                    }

                    String plantMinHumidityString = ((EditText) findViewById(R.id.editTextPlantMinHumidity)).getText().toString();

                    // new minimum humidity set
                    if (!plantMinHumidityString.equals("")) {
                        // Check if a number is given.
                        // If something other than a number was entered, send the user a message that
                        // the humidity must be a number between 0 and 100 and do not update the app.
                        try {
                            plantMinHumidity = Integer.parseInt(((EditText) findViewById(R.id.editTextPlantMinHumidity)).getText().toString());
                        } catch (Exception e) {
                            toastHumidity.show();
                            return;
                        }

                        // Check if the humidity is between 0 and 100.
                        // If the humidity is not a valid number, send the user a message that the humidity
                        // must be a number between 0 and 100 and do not update the app.
                        if (plantMinHumidity > 100 || plantMinHumidity < 0) {
                            toastHumidity.show();
                            return;
                        }
                    } else {
                        toastHumidity.show();
                        return;
                    }

                    // Add the new plant to the database
                    User user = new User();
                    user.setUid(uid);
                    user.setPlantName(plantName);
                    user.setPlantCurrentHumidity(40);
                    user.setPlantMinHumidity(plantMinHumidity);
                    MainActivity.db.userDao().addUser(user);

                    Toast toastEditSuccess = Toast.makeText(AddPlantActivity.this,
                            "Plant added successfully.", Toast.LENGTH_SHORT);
                    toastEditSuccess.setGravity(Gravity.CENTER, 0, 0);
                    toastEditSuccess.show();

                    // Switch to ListActivity
                    Intent intent = new Intent(AddPlantActivity.this, ListActivity.class);
                    finish();
                    startActivity(intent);
                }
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
