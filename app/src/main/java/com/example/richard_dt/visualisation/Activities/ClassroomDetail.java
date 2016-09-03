package com.example.richard_dt.visualisation.Activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.richard_dt.visualisation.Activities.CoursFragments.ClassPagerAdapter;
import com.example.richard_dt.visualisation.Activities.CoursFragments.ClassroomFrag;
import com.example.richard_dt.visualisation.Activities.CoursFragments.StaffFragment;
import com.example.richard_dt.visualisation.R;
import com.example.richard_dt.visualisation.gsApiClass.Classroom;
import com.example.richard_dt.visualisation.gsApiClass.Staff;

import java.util.ArrayList;

public class ClassroomDetail extends AppCompatActivity {


    Bundle b =null;
    ClassPagerAdapter adapterViewPager;
    TabLayout tabLayout;
    String test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.classroom_detail_pager);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar6);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        b = getIntent().getExtras();

       test=b.getString("classroom");

        if(test!=null){

       if(test.equals("classroom")) {

           ArrayList<Classroom> c = b.getParcelableArrayList("classroomlist");

           b = new Bundle();

           b.putParcelableArrayList("classroomlist", c);


           ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager3);
           adapterViewPager = new ClassPagerAdapter(getSupportFragmentManager(), b, true);
           if (c != null) {
               for (int i = 0; i < c.size(); i++) {
                   adapterViewPager.addFragmentCu(new ClassroomFrag(), c.get(i).getClassroom_name());

               }
           }
           if (vpPager != null) {
               vpPager.setAdapter(adapterViewPager);
           }

           tabLayout = (TabLayout) findViewById(R.id.tabs);
           if (tabLayout != null) {
               tabLayout.setupWithViewPager(vpPager);
           }

       }else if(test.equals("staff")){

           ArrayList<Staff> stafflist = b.getParcelableArrayList("stafflist");

           b = new Bundle();

           b.putParcelableArrayList("stafflist", stafflist);



           ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager3);
           adapterViewPager = new ClassPagerAdapter(getSupportFragmentManager(), b, false);

           if (stafflist != null) {
               for (int i = 0; i < stafflist.size(); i++) {
                   adapterViewPager.addFragmentPr(new StaffFragment(), stafflist.get(i).getNomComplet() );

               }
           }
           if (vpPager != null) {
               vpPager.setAdapter(adapterViewPager);
           }

           tabLayout = (TabLayout) findViewById(R.id.tabs);
           if (tabLayout != null) {
               tabLayout.setupWithViewPager(vpPager);
           }

       }
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
