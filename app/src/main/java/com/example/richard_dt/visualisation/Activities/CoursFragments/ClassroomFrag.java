package com.example.richard_dt.visualisation.Activities.CoursFragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.richard_dt.visualisation.Activities.CalendarActivity;
import com.example.richard_dt.visualisation.Activities.MainActivity;
import com.example.richard_dt.visualisation.Helper.ClassParameters;
import com.example.richard_dt.visualisation.Helper.DownloadImageTask;
import com.example.richard_dt.visualisation.R;
import com.example.richard_dt.visualisation.gsApiClass.Classroom;
import com.example.richard_dt.visualisation.gsApiClass.Cours;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Richard-DT on 18/05/2016.
 */
public class ClassroomFrag extends Fragment implements View.OnClickListener{

    private TextView tvClassName;
    private TextView tvClassroomAdress;
    private TextView tvClassZip;
    private TextView tvClassrommCity;
    private TextView tvClassrommCoordinates;
    private TextView tvClassrommServices;
    private TextView tvClassrommType;
    private TextView tvClassrommCapacity;
    private TextView tvClassrommDescription;

    private ImageView Iv;
    private Button b;

    private ClassParameters classParameters;
    public ClassroomFrag () {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fragment_classroom,null);
        Classroom classroom = getArguments().getParcelable("classroom");
        classParameters = new ClassParameters(classroom);
        Iv=(ImageView) rootView.findViewById(R.id.imgClassroom);


        b=(Button) rootView.findViewById(R.id.goCalFromCr) ;
        b.setOnClickListener(this);
        
        if(classroom != null) {
            String srcString= classroom.getClassroom_photo_url().replace("|","");
            tvClassroomAdress = (TextView) rootView.findViewById(R.id.tvclassroomAdresse);
            tvClassroomAdress.setText(classroom.getClassroom_address() == null ? "" : classroom.getClassroom_address());
            tvClassName = (TextView) rootView.findViewById(R.id.tvclassroomName);
            tvClassName.setText(classroom.getClassroom_name() == null ? "" : classroom.getClassroom_name());
            tvClassZip= (TextView) rootView.findViewById(R.id.tvclassroomZip);
            tvClassZip.setText(classroom.getClassroom_zip() == null ? "" : classroom.getClassroom_zip());
            tvClassrommCity= (TextView) rootView.findViewById(R.id.tvClassroomCity);
            tvClassrommCity.setText(classroom.getClassroom_city() == null ? "" : classroom.getClassroom_city());
            tvClassrommCoordinates= (TextView) rootView.findViewById(R.id.tvClassroomCoordinates);
            float longitude = classroom.getClassroom_gps_lon();
            float latitude = classroom.getClassroom_gps_lat();
            tvClassrommCoordinates.setText("long :" +longitude + ", lat :"+latitude);
            tvClassrommServices = (TextView) rootView.findViewById(R.id.tvClassroomServices);
            String parking = classroom.isClassroom_feature_parking()?"Parking,":"";
            String locker = classroom.isClassroom_feature_locker()?"Casier,":"";
            String douche = classroom.isClassroom_feature_shower()?"Douche,":"";
            String credit = classroom.isClassroom_feature_credit()?"Banque,":"";
            tvClassrommServices.setText(parking+locker+douche+credit);
            tvClassrommType = (TextView) rootView.findViewById(R.id.tvClassroomType);
            HashMap<String,String> hm= Cours.getIntensity();
            String classroomType = hm.get(Integer.toString(classroom.getClassroom_type()));
            tvClassrommType.setText(classroomType);
            tvClassrommCapacity =(TextView) rootView.findViewById(R.id.tvClassroomCapacity);
            tvClassrommCapacity.setText(""+classroom.getClassroom_capacity());
            tvClassrommDescription = (TextView) rootView.findViewById(R.id.tvClassroomDescription);
            tvClassrommDescription.setText(Html.fromHtml(classroom.getClassroom_description()));
            new DownloadImageTask(Iv)
                    .execute(srcString);
        }
        return rootView;
    }

//
//    public static Drawable LoadImageFromWeb(String url) {
//        try {
//            InputStream is = (InputStream) new URL(url).getContent();
//            Drawable d = Drawable.createFromStream(is, "src name");
//            return d;
//        } catch (Exception e) {
//            return null;
//        }
//    }

    @Override
    public void onClick(View v) {

        startActivity(new Intent(getContext(), CalendarActivity.class)
                .putExtra("classParameters",classParameters));

    }
}

