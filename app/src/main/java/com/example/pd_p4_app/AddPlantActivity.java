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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Collections;

public class AddPlantActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Button buttonConfirmAdd; // the button to confirm that the user wants to add this plant to the app

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

        // Add an onClickListener to the confirmation button to react when the user presses the button
        buttonConfirmAdd = findViewById(R.id.buttonConfirmAdd);
        buttonConfirmAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Extract the text given by the user
                String plantName = ((EditText)findViewById(R.id.editTextPlantName)).getText().toString();
                int plantMinHumidity = Integer.parseInt(((EditText)findViewById(R.id.editTextPlantMinHumidity)).getText().toString());
                // Add the new plant to the list and resort the list
                ((MyApplication)getApplication()).addPlant(plantName, plantMinHumidity, 80);
                Collections.sort(((MyApplication)getApplication()).plants, Plant.HumdityDiffComparator);
                // Switch to ListActivity
                Intent intent = new Intent(AddPlantActivity.this, ListActivity.class);
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
