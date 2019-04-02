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

import java.util.List;

public class EditPlantActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Button buttonConfirmEdit; // the button to confirm that the user wants to addUser this plant to the app

    // onCreate is called every time the activity starts
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // load the activity layout
        setContentView(R.layout.activity_edit_plant);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        setSupportActionBar(myToolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        Drawable drawerToggle = myToolbar.getNavigationIcon();
        drawerToggle.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);

        final Toast toastHumidity = Toast.makeText(EditPlantActivity.this, "Humidity must be a number between 0 and 100.", Toast.LENGTH_SHORT);
        toastHumidity.setGravity(Gravity.CENTER,0,0);

        // Add an onClickListener to the confirmation button to react when the user presses the button
        buttonConfirmEdit = findViewById(R.id.buttonConfirmEdit);
        buttonConfirmEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Extract the text given by the user
                String plantName = ((EditText)findViewById(R.id.editTextNewPlantName))
                                        .getText().toString();
                String plantMinHumidityString = ((EditText)findViewById(R.id.editTextNewPlantMinHumidity))
                                                    .getText().toString();

                // Add the new plant to the database
                User user = MainActivity.db.userDao().
                        getUserById(getIntent().getIntExtra("uid", 0));

                if (!plantName.equals("")) {
                    user.setPlantName(plantName);
                }

                // new minimum humidity set
                if (!plantMinHumidityString.equals("")) {
                    int plantMinHumidity;
                    // Check if a number is given.
                    // If something other than a number was entered, send the user a message that
                    // the humidity must be a number between 0 and 100 and do not update the app.
                    try {
                        plantMinHumidity = Math.round(Float.parseFloat(plantMinHumidityString));
                    } catch (Exception e) {
                        toastHumidity.show();
                        return;
                    }

                    // Check if the humidity is between 0 and 100.
                    // If the humidity is not a valid number, send the user a message that the humidity
                    // must be a number between 0 and 100 and do not update the app.
                    if (plantMinHumidity < 100 && plantMinHumidity > 0) {

                        user.setPlantMinHumidity(Integer.toString(plantMinHumidity));
                    } else {
                        toastHumidity.show();
                        return;
                    }
                }

                MainActivity.db.userDao().updateUser(user);
                Toast toastEditSuccess = Toast.makeText(EditPlantActivity.this,
                        "Data successfully changed in the database", Toast.LENGTH_SHORT);
                toastEditSuccess.setGravity(Gravity.CENTER,0,0);
                toastEditSuccess.show();
                // Switch to ListActivity
                Intent intent = new Intent(EditPlantActivity.this, ListActivity.class);
                finish();
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
