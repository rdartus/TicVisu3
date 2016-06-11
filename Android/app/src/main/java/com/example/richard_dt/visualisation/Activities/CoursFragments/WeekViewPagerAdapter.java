package com.example.richard_dt.visualisation.Activities.CoursFragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.richard_dt.visualisation.Activities.MainActivity;
import com.example.richard_dt.visualisation.gsApiClass.Classroom;
import com.example.richard_dt.visualisation.gsApiClass.Cours;
import com.example.richard_dt.visualisation.gsApiClass.Staff;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Richard-DT on 03/06/2016.
 */
public class WeekViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Classroom> classroomList = new ArrayList<>();

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private int NUM_ITEMS = 1;
    private int year,month,day;
    Context context;
    Bundle b;


    public void addFragmentWeekView(ClassroomFrag fragment) {
        mFragmentList.add(fragment);
    }


    public WeekViewPagerAdapter(FragmentManager fragmentManager, Bundle data, Context context) {
        super(fragmentManager);
        this.context = context;
        b = data;
        year = b.getInt("year_x");
        month = b.getInt("month_x");
        day = b.getInt("day_x");

        classroomList = b.getParcelableArrayList("classroomList");
        NUM_ITEMS = classroomList != null ? classroomList.size() : 0;

    }


    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        Fragment fragToView;
        if(classroomList.size() == 1) {
            fragToView = new CalendarFrag();
            b.putParcelable("classroom", classroomList.get(position));
            fragToView.setArguments(b);
        }
        else {
            fragToView = new CalendarFrag();
            b = new Bundle();
            b.putInt("year_x",year);
            b.putInt("month_x",month);
            b.putInt("day_x",day);
            b.putParcelable("classroom", classroomList.get(position));
            ArrayList coursList = (ArrayList<Cours>) MainActivity.getCoursList(
                    Integer.toString(classroomList.get(position).getClassroom_id()), context);
            b.putParcelableArrayList("coursList", coursList);
            b.putStringArrayList("listPossibleIntensity", (ArrayList<String>) MainActivity.getListPossibleIntensity(coursList));
//        b.putParcelableArrayList("staffList",(ArrayList<Staff>) MainActivity.getStaffList(classroomList.get(position),context));
            fragToView.setArguments(b);
        }
        return fragToView;
    }


    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return classroomList.get(position).getClassroom_name();
    }
}

