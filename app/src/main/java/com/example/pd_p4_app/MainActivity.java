package com.example.pd_p4_app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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

import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Button bigButton;
    private Plant dangerPlant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        bigButton = findViewById(R.id.bigButton);

        if (((MyApplication)getApplication()).plants.size() == 0) {
            bigButton.setText("\nThere are no plants logged in the app. Add a plant using the button below.");
        } else {
            Collections.sort(((MyApplication)getApplication()).plants, Plant.HumdityDiffComparator);
            dangerPlant = ((MyApplication)this.getApplication()).getPlantAt(0);
            if (dangerPlant.getCurrentHumidity() - dangerPlant.getThreshold() < 0) {
                bigButton.setText("\n" + dangerPlant.getName() + " needs water immediately!\n\n" +
                        "Humidity level at\n" + dangerPlant.getCurrentHumidity());
                bigButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            } else if (dangerPlant.getCurrentHumidity() - dangerPlant.getThreshold() < 10) {
                bigButton.setText(dangerPlant.getName() + "\nis getting dry.\n\n" +
                        "Humidity level at\n" + dangerPlant.getCurrentHumidity());
                bigButton.getBackground().setColorFilter(0xffd435, PorterDuff.Mode.SRC_ATOP);
            } else {
                bigButton.setText("All plants are watered");
                bigButton.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
            }
        }

        Button addPlant = findViewById(R.id.buttonAddPlant);
        addPlant.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddPlantActivity.class);
                startActivityForResult(intent, RESULT_OK);
            }
        });

        Button buttonChangeView = findViewById(R.id.bigButton);
        buttonChangeView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toggle_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_toggle_main:
                Intent intent = new Intent(this, ListActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
