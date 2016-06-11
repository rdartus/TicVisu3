package com.example.richard_dt.visualisation.Activities.CoursFragments;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.richard_dt.visualisation.Helper.Indicateur;
import com.example.richard_dt.visualisation.Helper.Item;
import com.example.richard_dt.visualisation.Helper.ItemAdapter;
import com.example.richard_dt.visualisation.R;
import com.example.richard_dt.visualisation.gsApiClass.Classroom;
import com.example.richard_dt.visualisation.gsApiClass.Cours;
import com.example.richard_dt.visualisation.gsApiClass.Staff;

import org.json.JSONException;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Richard-DT on 06/06/2016.
 */
public class StaffInfoFrag extends Fragment {


    Staff staff;
    TextView tvStaffName;
        TextView tvStaffRole;
        TextView tvStaffEmail ;
    ImageView imgStaff;

        ListView lv;
        public StaffInfoFrag() {
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



            staff = getArguments().getParcelable("Staff");

            View rootView = inflater.inflate(R.layout.fragment_staffinfo, null);
            if(staff!=null) {
                tvStaffEmail = (TextView)rootView.findViewById(R.id.lStaffInfo_StaffEmail);
                tvStaffName= (TextView)rootView.findViewById(R.id.lStaffInfo_StaffName);
                tvStaffRole= (TextView)rootView.findViewById(R.id.lStaffInfo_StaffRole);

                tvStaffEmail.setText(staff.getUser_adresse_mail());
                tvStaffName.setText(staff.getNomComplet());
                tvStaffRole.setText(staff.getStaff_issue_type());

                imgStaff = (ImageView)rootView.findViewById(R.id.imgStaffInfo);
                Drawable d= null;
                if (staff != null) {
                    d = StaffFragment.LoadImageFromWeb(staff.getStaff_photo_large_url());
                }
                if (d!=null)
                    imgStaff.setImageDrawable(d);

            }
            return rootView;
        }

}
