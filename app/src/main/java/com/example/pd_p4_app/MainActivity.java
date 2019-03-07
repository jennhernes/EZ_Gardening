package com.example.pd_p4_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

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

        ((MyApplication)this.getApplication()).setPlant("Clara", 50, 62);
        dangerPlant = ((MyApplication)this.getApplication()).getPlant();
        bigButton = findViewById(R.id.bigButton);
        bigButton.setText(dangerPlant.getName() + " is in danger!\nHumidity level at\n\n" + dangerPlant.getCurrentHumidity());
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
