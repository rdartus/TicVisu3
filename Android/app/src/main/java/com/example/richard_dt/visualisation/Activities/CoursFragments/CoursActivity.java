package com.example.richard_dt.visualisation.Activities.CoursFragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.richard_dt.visualisation.Helper.Testeur;
import com.example.richard_dt.visualisation.R;
import com.example.richard_dt.visualisation.gsApiClass.Classroom;
import com.example.richard_dt.visualisation.gsApiClass.Cours;

public class CoursActivity extends AppCompatActivity {



    Bundle b =null;
    FragmentPagerAdapter adapterViewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = getIntent().getExtras();
        Cours c = b.getParcelable("cours");
//        Classroom classroom= b.getParcelable("classroom");
//        b = new Bundle();
//        b.putParcelable("cours", c);
//        b.putParcelable("classroom", classroom);
        setContentView(R.layout.fragments_viewer_cours);

        if(findViewById(R.id.lStatut)!=null && c != null){
            if(!c.hasIssue() ) {
                findViewById(R.id.lStatut).setBackgroundColor(Color.parseColor("#87d288"));
            }
            else {
                findViewById(R.id.lStatut).setBackgroundColor(getResources().getColor(R.color.event_color_02));
            }
        }
        if(findViewById(R.id.tvStatutHeader)!=null)

            if(!c.hasIssue() ) {
                ((TextView)findViewById(R.id.tvStatutHeader)).setText(getString(R.string.classSane));
            }
            else {
                ((TextView)findViewById(R.id.tvStatutHeader)).setText(Testeur.test(c)[0]);
            }

        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new CoursPagerAdapter(getSupportFragmentManager(),b,getApplicationContext());
        if(vpPager!=null)
            vpPager.setAdapter(adapterViewPager);


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar5);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setTitle(R.string.Titre);
        getSupportActionBar().setIcon(R.mipmap.gslogo);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(vpPager);
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
