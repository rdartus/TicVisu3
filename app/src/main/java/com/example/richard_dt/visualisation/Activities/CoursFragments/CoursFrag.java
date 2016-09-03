package com.example.richard_dt.visualisation.Activities.CoursFragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telecom.Connection;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.richard_dt.visualisation.Activities.ClassroomDetail;
import com.example.richard_dt.visualisation.Activities.MainActivity;
import com.example.richard_dt.visualisation.Helper.DownloadImageTask;
import com.example.richard_dt.visualisation.Helper.Indicateur;
import com.example.richard_dt.visualisation.Helper.Item;
import com.example.richard_dt.visualisation.Helper.ItemAdapter;
import com.example.richard_dt.visualisation.R;
import com.example.richard_dt.visualisation.gsApiClass.Classroom;
import com.example.richard_dt.visualisation.gsApiClass.Cours;
import com.example.richard_dt.visualisation.gsApiClass.DispoStaff;
import com.example.richard_dt.visualisation.gsApiClass.Staff;

import junit.framework.Assert;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * Created by Richard-DT on 18/05/2016.
 *
 */
public class CoursFrag extends Fragment {

    private SimpleDateFormat sdf;
    private ImageView level_img;
    private ListView lv;
    private ListView lvTest;
    private Calendar cal = Calendar.getInstance();

    public CoursFrag() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        AdapterView.OnItemClickListener suggestionOnclickListener =new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ArrayList<Staff> stafflist= (ArrayList<Staff>) MainActivity.getStaff(((Item<String>) lv.getAdapter().getItem(position)).getId(),getContext());
                startActivity(new Intent(getContext(), ClassroomDetail.class)
                        .putExtra("stafflist", stafflist)
                        .putExtra("classroom","staff"));
            }
        };
        AdapterView.OnItemClickListener staffOnclickListener =new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ArrayList<Staff> stafflist= (ArrayList<Staff>) MainActivity.getStaff(((Item<String>) lvTest.getAdapter().getItem(position)).getId(),getContext());
                startActivity(new Intent(getContext(), ClassroomDetail.class)
                        .putExtra("stafflist", stafflist)
                        .putExtra("classroom","staff"));
            }
        };
        Cours cours = getArguments().getParcelable("cours");
        Classroom c = getArguments().getParcelable("classroom");
        ArrayList<Staff> staffArrayList = getArguments().getParcelableArrayList("staffList");
        ArrayList<Item<String>> staffItemList = new ArrayList<>();

        if (staffArrayList != null) {
            for(Staff staff : staffArrayList){
                staffItemList.add(new Item(staff.getNomComplet(),staff.getUser_id()));
            }
        }else{
            staffArrayList= (ArrayList<Staff>) MainActivity.getStaffList(c.getClassroom_id()+"",getContext());
            for(Staff staff : staffArrayList){
                staffItemList.add(new Item(staff.getNomComplet(),staff.getUser_id()));
            }

        }
        View rootView = inflater.inflate(R.layout.fragment_cours, null);
        if(cours !=null) {
            sdf = DateFormat.is24HourFormat(getContext()) ? new SimpleDateFormat("HH:mm", Locale.getDefault()) : new SimpleDateFormat("hh a", Locale.getDefault());
            level_img = (ImageView) rootView.findViewById(R.id.imgClass);
            int duration = cours.getClass_duration();
//            String srcString= "http://design.gymsuedoise.com/USER20160406/classlevel/level_standard.png";
            String srcString= cours.getClass_level_icon_URL().replace("|","");
//            String srcString = ""+arrayUrl[0];
//            for (int i=1;i<arrayUrl.length;i++){
//                srcString += "/" + arrayUrl[i];
//            }
            String temp1 = cours.getClass_capacity() + "/" + cours.getClass_bookable_capacity();
            String temp2 = Integer.toString(duration);
            String temp3 = cours.getClass_level() + "";
            String temp4 = cours.getClass_level_text() + "";
            String[] class_Starttime = cours.getClass_starttime().split(":");
            cal.set(Calendar.HOUR_OF_DAY,Integer.valueOf(class_Starttime[0]));
            cal.set(Calendar.MINUTE,Integer.valueOf(class_Starttime[1]));
            long startTimeInMillis = cal.getTimeInMillis();
            long endTimInMillis = startTimeInMillis + Integer.valueOf(duration)*60*1000;
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTimeInMillis(endTimInMillis);
            lvTest = (ListView) rootView.findViewById(R.id.lTeacher);
            TextView tvClass_date = (TextView) rootView.findViewById(R.id.tvClass_date);
            ((TextView) rootView.findViewById(R.id.tvclass_capacity)).setText(temp1);
            TextView tvClass_duration = (TextView) rootView.findViewById(R.id.tvclass_duration);
            TextView tvClass_starttime = (TextView) rootView.findViewById(R.id.tvclass_starttime);
            TextView tvClass_endtime = (TextView) rootView.findViewById(R.id.tvClass_endtime);
            TextView tvClass_level = (TextView) rootView.findViewById(R.id.tvclass_level);
            TextView tvClass_level_text = (TextView) rootView.findViewById(R.id.tvclass_level_text);
            ImageView imgClass = (ImageView) rootView.findViewById(R.id.imgClass);

            tvClass_duration.setText("("+temp2+" "+getString(R.string.minute)+")");
            tvClass_date.setText(cours.getClass_date());
            tvClass_starttime.setText(cours.getClass_starttime());
            System.out.printf(sdf.format(cal.getTime()));
            System.out.printf(sdf.format(endCalendar.getTime()));
            tvClass_endtime.setText(sdf.format(endCalendar.getTime()));
            HashMap<String,String> hm= Cours.getIntensity();
            String classroomType = hm.get(temp3);
            tvClass_level.setText(classroomType);
            tvClass_level_text.setText(temp4);
            new DownloadImageTask(level_img)
                    .execute(srcString);
            ItemAdapter<String> testPath = new ItemAdapter<>(staffItemList, getContext());
            lvTest.setOnItemClickListener(staffOnclickListener);
            lvTest.setAdapter(testPath);

            if (cours.hasIssue()) {
                lv = (ListView) rootView.findViewById(R.id.listsug);
                lv.setOnItemClickListener(suggestionOnclickListener);
                try {
                    List<Item<String>> ProfSuggerre = Indicateur.getSuggestion(c.getClassroom_gps_lon(), c.getClassroom_gps_lat(), getContext(), cours.getClass_starttime(), cours.getClass_duration());

                    ItemAdapter<String> staffListAdapter = new ItemAdapter<>(ProfSuggerre, getContext());
                    lv.setAdapter(staffListAdapter);
                    staffListAdapter.notifyDataSetChanged();

                } catch (ExecutionException | InterruptedException | JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                View lSuggestion = rootView.findViewById(R.id.lSuggestion);
                lSuggestion.setVisibility(View.GONE);
            }


        }
        return rootView;
    }
}