package com.example.pd_p4_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Collections;

public class AddPlantActivity extends AppCompatActivity {

    private Button buttonConfirmAdd; // the button to confirm that the user wants to add this plant to the app

    // onCreate is called every time the activity starts
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // load the activity layout
        setContentView(R.layout.activity_add_plant);

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
}
