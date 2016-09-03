package com.example.richard_dt.visualisation.Activities.CoursFragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.example.richard_dt.visualisation.Activities.MainActivity;
import com.example.richard_dt.visualisation.Helper.CalendarLengthDialog;
import com.example.richard_dt.visualisation.Helper.Parser;
import com.example.richard_dt.visualisation.Helper.Testeur;
import com.example.richard_dt.visualisation.Helper.pref;
import com.example.richard_dt.visualisation.R;
import com.example.richard_dt.visualisation.gsApiClass.Classroom;
import com.example.richard_dt.visualisation.gsApiClass.Cours;
import com.example.richard_dt.visualisation.gsApiClass.DispoStaff;
import com.example.richard_dt.visualisation.gsApiClass.Staff;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class StaffFragment extends Fragment implements WeekView.EventClickListener,WeekView.EventLongPressListener,MonthLoader.MonthChangeListener,WeekView.EmptyViewLongPressListener, View.OnClickListener{

    Staff staff;
    ImageView Iv;
    TextView name,email,adress,status,role;
    LinearLayout lt;
    private com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog;
    private ArrayList<String> path = new ArrayList<>();
    pref p;
    private AlertDialog filterDialog;
    private AlertDialog calendarDialog;

    private WeekView mWV;
    ArrayList<Cours> coursHost=new ArrayList<>();

    ArrayList<Cours> coursTeacher=new ArrayList<>();


    ImageButton ibNextWeek, ibPreviousWeek, ibToday, ibCalendarLength;

    private com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener dsl = new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.set(Calendar.YEAR, year);
            selectedCalendar.set(Calendar.MONTH, monthOfYear);
            selectedCalendar.set(Calendar.DATE, dayOfMonth);
            mWV.goToMidlleCalendarDate(selectedCalendar);
            mWV.goToHour(p.getInt("startHour"));
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fragment_staff,null);
        p = new pref(this.getContext());
        AlertDialog.Builder filterBuilder = new AlertDialog.Builder(getActivity());

        Iv=(ImageView) rootView.findViewById(R.id.photo);

        status=(TextView) rootView.findViewById(R.id.statusStaff);

        name=(TextView) rootView.findViewById(R.id.staff_name);
        email=(TextView) rootView.findViewById(R.id.staff_email);
        adress=(TextView)rootView.findViewById(R.id.staff_adress);
        role=(TextView)rootView.findViewById(R.id.staff_role);

        ibNextWeek = (ImageButton) rootView.findViewById(R.id.btNextWeek2);
        ibPreviousWeek = (ImageButton) rootView.findViewById(R.id.btPreviousWeek2);
        ibToday = (ImageButton) rootView.findViewById(R.id.btGoTo2);
        ibCalendarLength = (ImageButton) rootView.findViewById(R.id.dateLength2);
        ibPreviousWeek.setOnClickListener(this);
        ibToday.setOnClickListener(this);
        ibNextWeek.setOnClickListener(this);
        ibCalendarLength.setOnClickListener(this);


        lt=(LinearLayout) rootView.findViewById(R.id.LinearStatus);

        mWV = (WeekView) rootView.findViewById(R.id.weekViewProf);

        mWV.setOnEventClickListener(this);
        mWV.setMonthChangeListener(this);
        mWV.setEventLongPressListener(this);
        mWV.setNumberOfVisibleDays(7);

        mWV.setZoom(70);
        mWV.goToHour(p.getInt("startHour"));
        mWV.lockScroll();
        filterDialog = filterBuilder.create();


        AlertDialog.Builder calendarLengthBuilder = new CalendarLengthDialog(getContext(), ibNextWeek, ibPreviousWeek,p.getInt("startHour"),mWV);
        calendarDialog = calendarLengthBuilder.create();

        datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                dsl,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setCancelable(false);
        datePickerDialog.setTitle(getString(R.string.mdtp_select_day));




        p = new pref(this.getContext());
        this.getContext().getSharedPreferences("mypref", 0).getString("hour", "defautlt");



        staff = getArguments().getParcelable("staff");

        name.setText(staff.getNomComplet());
        email.setText(staff.getUser_adresse_mail());
        adress.setText(staff.getUser_adresse());


        if(staff.isTeacher()) {
            role.setText("Professeur");

            Parser p = new Parser(getContext());

            try {
                p.execute("dispo/prof/"+staff.getUser_id()).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            String idCours = "";
            try {
                idCours = (String) p.getReturned1().get("userids");


            } catch (JSONException e) {
                e.printStackTrace();
            }

            Gson gson=new Gson();
            Parser P = new Parser(getContext());
            try {
                P.execute(P.ListQuery("class", "class_id", idCours)).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < P.getReturned().length(); i++) {
                try {

                    coursTeacher.add(gson.fromJson(String.valueOf(P.getReturned().get(i)), Cours.class));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        if(staff.isHost()){
            if(role.getText().equals("staff_role")){
            role.setText("Animateur");}
            else{
                String test=role.getText()+"Animateur";
                role.setText(test);
            }
            Parser p2=new Parser(getContext());
            try {
                p2.execute("dispo/host/"+staff.getUser_id()).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            String idCours = "";
            try {
                idCours = (String) p2.getReturned1().get("userids");


            } catch (JSONException e) {
                e.printStackTrace();
            }


            Gson gson=new Gson();
            Parser P2 = new Parser(getContext());
            try {
                P2.execute(P2.ListQuery("class", "class_id", idCours)).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < P2.getReturned().length(); i++) {
                try {

                    coursHost.add(gson.fromJson(String.valueOf(P2.getReturned().get(i)), Cours.class));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }



        for(int i=0;i<staff.getUser_dipsonibilities().size();i++)
        {staff.getUser_dipsonibilities().get(i).CalendarSet();}

        Drawable d= null;
        if (staff != null) {
            d = LoadImageFromWeb(staff.getStaff_photo_large_url());
        }

        String error=Testeur.testProf(staff);
        status.setText(error);
        if(!error.equals("OK")){
            lt.setBackgroundColor(getResources().getColor(R.color.event_color_02));
        }


        if(d!=null){Iv.setImageDrawable(d);}
        else{Iv.setImageResource(R.drawable.noimage);}

        return rootView;
    }

    public static Drawable LoadImageFromWeb(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            return Drawable.createFromStream(is, "src name");
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void onEmptyViewLongPress(Calendar time) {

    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        Classroom classroom=null;
        Gson gson=new Gson();
        if (event.getClass() == Cours.class) {
            Cours C=(Cours)event;
            Parser parser=new Parser(getContext());
            try {
                parser.execute(parser.AddQuery(parser.EqualQuery("classroom","classroom_id",C.getClassroom_id()+""),parser.EqualQuery("classroom","classroom_id",C.getClassroom_country_code()))).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            try {
                classroom=gson.fromJson(String.valueOf(parser.getReturned().get(0)),Classroom.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            ArrayList<Staff> staffListCoursSelected = new ArrayList<>();
            if (((Cours)event).getTeacher_list() != null
                    && ((Cours)event).getTeacher_list().size() != 0
                    &&((Cours)event).getHost_list() != null
                    && ((Cours)event).getHost_list().size() != 0) {
                for(Object sId : ((Cours)event).getHost_list())
                {
                    staffListCoursSelected.addAll((ArrayList<Staff>)MainActivity.getStaff((String)sId, getContext()));
                }
                for(Object sId : ((Cours)event).getTeacher_list())
                {
                    staffListCoursSelected.addAll((ArrayList<Staff>)MainActivity.getStaff((String)sId, getContext()));
                }
            }
            startActivity(new Intent(getContext(), CoursActivity.class)
                    .putExtra("classroom", classroom)
                    .putExtra("staffList", staffListCoursSelected)
                    .putExtra("cours", (Parcelable) event));

        }
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public List< ? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        ArrayList events=new ArrayList<>() ;

        for(DispoStaff dispo : staff.getUser_dipsonibilities())
        if (dispo.getStartTime().isSet(Calendar.MONTH) && (dispo.getStartTime().get(Calendar.MONTH) == newMonth)) {

                events.add(dispo);}

        for(Cours cours :coursTeacher){
            cours.CalendarSet();
            if (cours.getStartTime().isSet(Calendar.MONTH) && (cours.getStartTime().get(Calendar.MONTH) == newMonth)) {

                events.add(cours);
            }
        }
        for(Cours test :coursHost){
            test.CalendarSet();
            if (test.getStartTime().isSet(Calendar.MONTH) && (test.getStartTime().get(Calendar.MONTH) == newMonth)) {

                    events.add(test);
                }
        }


        return events;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btGoTo2:
                try {
                    final Activity activity = (Activity) getContext();
                    datePickerDialog.show(activity.getFragmentManager(), "dat");
                }catch (Exception e)
                {
                    Toast.makeText(getContext(), ""+e.toString(), Toast.LENGTH_LONG).show();
                }
                return;

            case R.id.btNextWeek2:
                mWV.goToNextDays();
                mWV.goToHour(p.getInt("startHour"));
                return;
            case R.id.btPreviousWeek2:
                mWV.goToPreviousDays();
                mWV.goToHour(p.getInt("startHour"));
                return;


            case R.id.dateLength2:
                calendarDialog.show();
                break;
        }

    }
}
