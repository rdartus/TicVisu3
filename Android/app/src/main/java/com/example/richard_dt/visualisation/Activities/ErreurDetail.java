package com.example.richard_dt.visualisation.Activities;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.richard_dt.visualisation.Activities.CoursFragments.ErreurAdapter;
import com.example.richard_dt.visualisation.R;

public class ErreurDetail  extends AppCompatActivity {


    Bundle b = null;
    ErreurAdapter adapterViewPager;
    TabLayout tabLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.erreur_detail_pager);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar7);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        b = getIntent().getExtras();





        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager4);
        adapterViewPager = new ErreurAdapter(getSupportFragmentManager(), b);

        if (vpPager != null) {
            vpPager.setAdapter(adapterViewPager);
        }

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(vpPager);
        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;


        }
        return false;
    }

}
