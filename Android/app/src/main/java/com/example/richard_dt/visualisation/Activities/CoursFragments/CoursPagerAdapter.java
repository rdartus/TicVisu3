package com.example.richard_dt.visualisation.Activities.CoursFragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.richard_dt.visualisation.R;
import com.example.richard_dt.visualisation.gsApiClass.Classroom;
import com.example.richard_dt.visualisation.gsApiClass.Cours;

/**
 * Created by Richard-DT on 18/05/2016.
 */
public class CoursPagerAdapter extends FragmentPagerAdapter {

    private static  int NUM_ITEMS = 2;
    Bundle b;
    Context context;

    public CoursPagerAdapter(FragmentManager fragmentManager, Bundle data ,Context context) {
        super(fragmentManager);
        b = data;
        this.context = context;
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
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                fragToView = new CoursFrag();
                fragToView.setArguments(b);
                return fragToView;
            case 1: // Fragment # 0 - This will show FirstFragment different title
                fragToView = new ClassroomFrag();
                fragToView.setArguments(b);
                return fragToView;
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return context.getString(R.string.classe);
            case 1:
                return context.getString(R.string.classroom);
            default:
                return "undefined tab";
        }

    }

}
// public class CoursPagerAdapter extends Fragment {
//
//    Fragment frag;
//    FragmentTransaction fragTransaction;
//    Bundle b;
//    Cours cours;
//    Classroom classroom;
//
//    public CoursPagerAdapter() {
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.activity_cours, container, false);
//        Button bCours = (Button) view.findViewById(R.id.bTabCours);
//        Button bClass = (Button) view.findViewById(R.id.bTabClassroom);
//
//        View lStatut = view.findViewById(R.id.lStatut);
//        lStatut.setBackgroundColor(Color.parseColor("#87d288"));
//        TextView tvStatut = (TextView)view.findViewById(R.id.tvStatutHeader);
//        tvStatut.setText("Cours sans erreur");
//
//        cours = getArguments().getParcelable("cours");
//        classroom = getArguments().getParcelable("classroom");
//
//        b = new Bundle();
//        b.putParcelable("cours",cours);
//        frag = new CoursFrag();
//        frag.setArguments(b);
//        fragTransaction = getFragmentManager().beginTransaction().add(R.id.container, frag);
//        fragTransaction.commit();
//
//
//
//        bCours.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                b = new Bundle();
//                b.putParcelable("cours",cours);
//                frag = new CoursFrag();
//                frag.setArguments(b);
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//            }
//        });
//        bClass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                b.putParcelable("classroom", classroom);
//                frag = new ClassroomFrag();
//                frag.setArguments(b);
//                fragTransaction = getFragmentManager().beginTransaction().replace(R.id.container, frag);
//                fragTransaction.commit();
//
//            }
//        });
//        return view;
//    }
//}
