package com.example.pd_p4_app;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Collections;

public class EditPlantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plant);

        Button buttonConfirmAdd = findViewById(R.id.buttonConfirmEdit);

        buttonConfirmAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String plantName = ((EditText)findViewById(R.id.editTextEditPlantName)).getText().toString();
                int plantMinHumidity = Integer.parseInt(((EditText)findViewById(R.id.editTextEditPlantMinHumidity)).getText().toString());
                ((MyApplication)getApplication()).addPlant(plantName, plantMinHumidity, 80);
                Collections.sort(((MyApplication)getApplication()).plants, Plant.HumdityDiffComparator);
                Intent intent = new Intent(EditPlantActivity.this, ListActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }
}
