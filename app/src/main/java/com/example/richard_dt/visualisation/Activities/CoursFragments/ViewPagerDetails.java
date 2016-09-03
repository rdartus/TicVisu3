package com.example.richard_dt.visualisation.Activities.CoursFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.richard_dt.visualisation.Activities.MainFrag;


public class ViewPagerDetails extends FragmentPagerAdapter {

    private static  int NUM_ITEMS = 3;
//    private static  int NUM_ITEMS = 4;
    private Bundle b;


    public ViewPagerDetails(FragmentManager fragmentManager, Bundle b) {
        super(fragmentManager);
        this.b=b;

    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragToView = null;
        switch (position) {
            case 0:
                fragToView = new MainFrag();
                fragToView.setArguments(b);
                return  fragToView;
            case 1:
                fragToView = new SelectInformationFrag();
                fragToView.setArguments(b);
                return fragToView;
//            case 2:
//                fragToView = new SelectProfFrag();
//                return fragToView;
            case 2:
                fragToView = new ListingErreurFrag();
                fragToView.setArguments(b);
                return  fragToView;
        }
        return  fragToView;



    }

    @Override
    public CharSequence getPageTitle(int position) {

        if (position == 0) {
            return "Calendrier";
        } else if( position==1 ){
            return "Fiches d'Information";
        }else if(position==2){return  "Liste d'erreurs";}
//        else if(position==2){return  "Fiches staff";}

        return null;
    }

}