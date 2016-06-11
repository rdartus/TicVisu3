package com.example.richard_dt.visualisation.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.richard_dt.visualisation.Activities.CoursFragments.ViewPagerDetails;
import com.example.richard_dt.visualisation.Helper.Indicateur;
import com.example.richard_dt.visualisation.Helper.Parser;
import com.example.richard_dt.visualisation.Helper.serialArrayArray;
import com.example.richard_dt.visualisation.R;
import com.example.richard_dt.visualisation.gsApiClass.Cours;
import com.example.richard_dt.visualisation.gsApiClass.Region;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class statActivity extends AppCompatActivity {


    Gson gson;

    ViewPagerDetails viewPagerDetails;
    TabLayout tabLayout;

    Bundle b;



    private ArrayList<ArrayList> Clist = new ArrayList<>();
    private ArrayList<ArrayList> Rlist = new ArrayList<>();
    private ArrayList<ArrayList> Countrylist = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar2);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setTitle("Gym Suédoise");
        getSupportActionBar().setIcon(R.mipmap.gslogo);

        gson=new Gson();

        NotifyError();

        b=new Bundle();

        serialArrayArray Clistserial=new serialArrayArray(Clist);
        serialArrayArray Rlistserial=new serialArrayArray(Rlist);
        serialArrayArray Countrylistserial=new serialArrayArray(Countrylist);

        b.putSerializable("clist",  Clistserial);
        b.putSerializable("Rlist", Rlistserial);
        b.putSerializable("Countrylist",  Countrylistserial);

        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager2);
        viewPagerDetails = new ViewPagerDetails(getSupportFragmentManager(),b);
        if (vpPager != null) {
            vpPager.setAdapter(viewPagerDetails);
        }
        tabLayout = (TabLayout) findViewById(R.id.tabs2);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(vpPager);
        }


    }    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stat, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case  R.id.settings2 :
                startActivity(new Intent(getApplicationContext(),settingsActivity.class));
                break;


        }

        return super.onOptionsItemSelected(item);
    }

    public void NotifyError() {

        String Cid = "0";
        String Lid = "0";
        String Rcc = "0";
        String Rid = "0";

        //Liste des cours erronés

        Parser Ptest = new Parser(this.getApplicationContext());

        try {
            Ptest.execute(Ptest.EqualQuery("class", "class_has_issue", "true")).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if (Ptest.getReturned()!=null && Ptest.getReturned().length()!=0) {
            for (int i = 0; i < Ptest.getReturned().length(); i++) {
                try {
                    Cours c = gson.fromJson(String.valueOf(Ptest.getReturned().get(i)), Cours.class);
                    Cid = Cid + "," + c.getClassroom_id(); //ClassroomIdIteration
                    Lid = Lid + "," + c.getLocation_id(); //LocationId
                    Rcc = Rcc + "," + c.getClassroom_country_code(); //ClassroomCountryCode
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //Cid =Erreur par classoom

            Clist = Indicateur.ErreurIteration(Cid);
            Countrylist = Indicateur.ErreurIteration(Rcc);

            Parser P2 = new Parser(this.getApplicationContext());
            try {
                P2.execute(P2.ListQuery("region", "region_country_code", Rcc)).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < P2.getReturned().length(); i++) {
                Region R = null;
                try {
                    R = gson.fromJson(String.valueOf(P2.getReturned().get(i)), Region.class);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                for (int k = 0; k < Clist.get(0).size(); k++) {
                    if (R.getClassroom_list().contains(Double.valueOf((String) Clist.get(0).get(k)))) {
                        Rid = Rid + "," + R.getRegion_id();
                    }
                }

            }
            Rlist = Indicateur.ErreurIteration(Rid);
        }

    }
}
