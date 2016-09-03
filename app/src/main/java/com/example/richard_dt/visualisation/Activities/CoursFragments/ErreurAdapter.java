package com.example.richard_dt.visualisation.Activities.CoursFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class ErreurAdapter extends FragmentPagerAdapter {

    private static  int NUM_ITEMS = 2;
    private Bundle b;


    public ErreurAdapter(FragmentManager fragmentManager, Bundle b) {
        super(fragmentManager);
        this.b=b;

    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragToView;
        switch (position) {
            case 0:
                fragToView = new CoursFrag ();
                fragToView.setArguments(b);
                return fragToView;
            case 1:
                fragToView = new ClassroomFrag ();
                fragToView.setArguments(b);
                return fragToView;


            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if (position == 0) {
            return "Détail du cours";
        } else if( position==1 ){
            return "Détail de la salle";
        }else {return  "Liste d'erreurs";

        }

    }
}
