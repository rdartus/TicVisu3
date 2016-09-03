package com.example.richard_dt.visualisation.Activities.CoursFragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.richard_dt.visualisation.Activities.MainActivity;
import com.example.richard_dt.visualisation.Helper.ClassParameters;
import com.example.richard_dt.visualisation.gsApiClass.Classroom;
import com.example.richard_dt.visualisation.gsApiClass.Cours;
import com.example.richard_dt.visualisation.gsApiClass.Staff;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Richard-DT on 03/06/2016.
 */
public class WeekViewPagerAdapter extends FragmentPagerAdapter {

//    ArrayList<Classroom> classroomList = new ArrayList<>();
    ArrayList<ClassParameters> classParametersArrayList = new ArrayList<>();

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private int NUM_ITEMS = 1;
    private long millis;
    Context context;
    Bundle b;


    public void addFragmentWeekView(ClassroomFrag fragment) {
        mFragmentList.add(fragment);
    }


    public WeekViewPagerAdapter(FragmentManager fragmentManager, Bundle data, Context context) {
        super(fragmentManager);
        this.context = context;
        b = data;
        millis = b.getLong("millis");

        classParametersArrayList = b.getParcelableArrayList("classParametersArrayList");
        if(classParametersArrayList==null){
            NUM_ITEMS = 0;
            classParametersArrayList=new ArrayList<>();
            classParametersArrayList.add((ClassParameters)b.getParcelable("classParameters"));
        }
        NUM_ITEMS = classParametersArrayList != null ? classParametersArrayList.size() : 0;

    }


    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
//    @Override
//    public Fragment getItem(int position) {
//        Fragment fragToView;
//        if(classroomList.size() == 1) {
//            fragToView = new CalendarFrag();
//            b.putParcelable("classroom", classroomList.get(position));
//            fragToView.setArguments(b);
//        }
//        else {
//            fragToView = new CalendarFrag();
//            b = new Bundle();
//            b.putInt("year_x",year);
//            b.putInt("month_x",month);
//            b.putInt("day_x",day);
//            b.putParcelable("classroom", classroomList.get(position));
//            ArrayList coursList = (ArrayList<Cours>) MainActivity.getCoursList(
//                    Integer.toString(classroomList.get(position).getClassroom_id()), context);
//            b.putParcelableArrayList("coursList", coursList);
//            b.putStringArrayList("listPossibleIntensity", (ArrayList<String>) MainActivity.getListPossibleIntensity(coursList));
////        b.putParcelableArrayList("staffList",(ArrayList<Staff>) MainActivity.getStaffList(classroomList.get(position),context));
//            fragToView.setArguments(b);
//        }
//        return fragToView;
//    }
    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        Fragment fragToView;
            fragToView = new CalendarFrag();
            b = new Bundle();
            b.putLong("millis",millis);

            b.putParcelable("classParameters", classParametersArrayList.get(position));

//            b.putParcelable("classroom", classParametersArrayList.get(position).getClassroom());
//            ArrayList coursList = (ArrayList<Cours>) MainActivity.getCoursList(
//                    Integer.toString(
//                            classParametersArrayList.get(position).getClassroom().getClassroom_id()), context);
//            b.putParcelableArrayList("coursList", coursList);
//            b.putStringArrayList("listPossibleIntensity", (ArrayList<String>) MainActivity.getListPossibleIntensity(coursList));
            fragToView.setArguments(b);

        return fragToView;
    }


    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return classParametersArrayList.get(position).getClassroom().getClassroom_name();
    }
}

