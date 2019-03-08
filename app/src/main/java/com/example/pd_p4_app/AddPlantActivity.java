package com.example.pd_p4_app;

import android.app.Application;
import android.content.Intent;
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

public class AddPlantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        Button buttonConfirmAdd = findViewById(R.id.buttonConfirmAdd);
        final Application myApp = this.getApplication();

        buttonConfirmAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String plantName = ((EditText)findViewById(R.id.editTextPlantName)).getText().toString();
                int plantMinHumidity = Integer.parseInt(((EditText)findViewById(R.id.editTextPlantMinHumidity)).getText().toString());
                ((MyApplication)getApplication()).addPlant(plantName, 50, plantMinHumidity);
                Intent intent = new Intent(AddPlantActivity.this, ListActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }
}
