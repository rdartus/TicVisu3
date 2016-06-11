package com.example.richard_dt.visualisation.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.richard_dt.visualisation.Activities.CoursFragments.WeekViewPagerAdapter;
import com.example.richard_dt.visualisation.R;

public class CalendarActivity extends AppCompatActivity {


    TabLayout tabLayout;
    Bundle b;


    WeekViewPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = getIntent().getExtras();


        setContentView(R.layout.activity_stat);
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager2);
        adapterViewPager = new WeekViewPagerAdapter(getSupportFragmentManager(), b, getApplicationContext());
        if (vpPager != null)
            vpPager.setAdapter(adapterViewPager);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar2);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setTitle(R.string.Titre);
        getSupportActionBar().setIcon(R.mipmap.gslogo);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout = (TabLayout) findViewById(R.id.tabs2);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(vpPager);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.settings:
                startActivity(new Intent(getApplicationContext(), settingsActivity.class));
                break;
        }
        return false;
    }
}