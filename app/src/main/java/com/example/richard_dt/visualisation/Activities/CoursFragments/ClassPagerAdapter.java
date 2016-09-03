package com.example.richard_dt.visualisation.Activities.CoursFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.richard_dt.visualisation.Activities.MainActivity;
import com.example.richard_dt.visualisation.gsApiClass.Classroom;
import com.example.richard_dt.visualisation.gsApiClass.Staff;

import java.util.ArrayList;
import java.util.List;

public class ClassPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Classroom> test=new ArrayList<>();
    ArrayList<Staff> test2=new ArrayList<>();

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    private Boolean classroom;

    private   int NUM_ITEMS = 1;

    Bundle b;



    public void addFragmentCu(ClassroomFrag fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }
    public void addFragmentPr(StaffFragment fragment, String title){
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }


    public ClassPagerAdapter(FragmentManager fragmentManager, Bundle data,Boolean classroom) {
        super(fragmentManager);
        b = data;

        this.classroom=classroom;
        if(classroom){
            test=b.getParcelableArrayList("classroomlist");
            if (test != null) {
                NUM_ITEMS=test.size();
            }
        }else{
            test2=b.getParcelableArrayList("stafflist");
            assert test2 != null;
            NUM_ITEMS=test2.size();
        }
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
        if (classroom) {
            fragToView = new ClassroomFrag();
            b = new Bundle();
            b.putParcelable("classroom", test.get(position));
            fragToView.setArguments(b);
        } else {
                fragToView = new StaffFragment();
                b = new Bundle();
                b.putParcelable("staff", test2.get(position));
                fragToView.setArguments(b);
        }
        return fragToView;
    }


    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
