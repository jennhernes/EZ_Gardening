package com.example.pd_p4_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar myToolbar = findViewById(R.id.settingsToolbar);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Toast toastUrl = Toast.makeText(SettingsActivity.this, "URL cannot be an empty string.", Toast.LENGTH_SHORT);
        toastUrl.setGravity(Gravity.CENTER,0,0);

        Button confirmSettings = findViewById(R.id.buttonConfirmSettings);
        confirmSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newUrl = ((EditText)findViewById(R.id.editTextNewUrl)).getText().toString();

                if (newUrl.equals("")) {
                    toastUrl.show();
                    return;
                }

                FetchData.urlString = newUrl;

                Toast toastEditSuccess = Toast.makeText(SettingsActivity.this,
                        "URL successfully changed.", Toast.LENGTH_SHORT);
                toastEditSuccess.setGravity(Gravity.CENTER,0,0);
                toastEditSuccess.show();

                // Switch to previous activity
                finish();
            }
        });
    }
}
